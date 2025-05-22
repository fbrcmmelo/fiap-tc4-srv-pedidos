package com.fiap.tc4_srv_pedidos.usecases;

import com.fiap.tc4_srv_pedidos.controller.SolicitacaoPagamentoOut;
import com.fiap.tc4_srv_pedidos.domain.DadosCartaoCliente;
import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.domain.ProdutoPedido;
import com.fiap.tc4_srv_pedidos.domain.StatusPedidoEnum;
import com.fiap.tc4_srv_pedidos.domain.services.ObterProdutoService;
import com.fiap.tc4_srv_pedidos.gateway.IPedidoGateway;
import com.fiap.tc4_srv_pedidos.gateway.clients.estoque.IEstoqueGateway;
import com.fiap.tc4_srv_pedidos.gateway.clients.pagamento.IPagamentoGateway;
import com.fiap.tc4_srv_pedidos.gateway.clients.produto.Produto;
import com.fiap.tc4_srv_pedidos.gateway.clients.usuario.IUsuarioGateway;
import com.fiap.tc4_srv_pedidos.gateway.clients.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class GerarPedidoUseCaseTest {

    private IPedidoGateway gateway;
    private IUsuarioGateway usuarioGateway;
    private IEstoqueGateway estoqueGateway;
    private IPagamentoGateway pagamentoGateway;
    private ObterProdutoService obterProdutoService;
    private GerarPedidoUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(IPedidoGateway.class);
        usuarioGateway = mock(IUsuarioGateway.class);
        estoqueGateway = mock(IEstoqueGateway.class);
        pagamentoGateway = mock(IPagamentoGateway.class);
        obterProdutoService = mock(ObterProdutoService.class);
        useCase = new GerarPedidoUseCase(gateway, usuarioGateway, estoqueGateway, pagamentoGateway, obterProdutoService);
    }

    @Test
    void deveGerarPedidoComSucesso() {
        // Arrange
        Pedido requisicao = mock(Pedido.class);
        ProdutoPedido produtoPedido = mock(ProdutoPedido.class);
        when(requisicao.getClienteId()).thenReturn("1L");
        when(requisicao.getDadosCartao()).thenReturn(new DadosCartaoCliente("numero"));
        when(requisicao.getProdutoPedidos()).thenReturn(List.of(produtoPedido));

        when(produtoPedido.sku()).thenReturn("sku1");
        when(produtoPedido.quantidade()).thenReturn(2L);

        // Dados do usuário
        var usuario = mock(Usuario.class);
        var dadosCartao = mock(DadosCartaoCliente.class);
        when(requisicao.getDadosCartao()).thenReturn(dadosCartao);
        when(usuarioGateway.obterDadosUsuario(anyString())).thenReturn(usuario);

        // Dados do produto
        Produto produto = mock(Produto.class);
        when(produto.preco()).thenReturn(100.0);
        when(obterProdutoService.obterDadosProduto(anyList())).thenReturn(List.of(produto));

        // Solicitação de pagamento
        var solicitacao = mock(SolicitacaoPagamentoOut.class);
        when(solicitacao.statusPagamento()).thenReturn(StatusPedidoEnum.ABERTO);
        when(solicitacao.solicitacaoId()).thenReturn("trans123");
        when(pagamentoGateway.solicitar(any())).thenReturn(solicitacao);

        // Act
        useCase.gerar(requisicao);

        // Assert
        verify(estoqueGateway).baixarEstoque("sku1", 2);
        verify(gateway).salvarPedido(any(Pedido.class));
    }

    @Test
    void deveLancarErroAoBaixarEstoque_MasSalvarOPedido_ComINformacoesDoErro() {
        // Arrange
        Pedido requisicao = mock(Pedido.class);
        ProdutoPedido produtoPedido = mock(ProdutoPedido.class);
        when(requisicao.getClienteId()).thenReturn("1L");
        when(requisicao.getDadosCartao()).thenReturn(new DadosCartaoCliente("numero"));
        when(requisicao.getProdutoPedidos()).thenReturn(List.of(produtoPedido));
        when(produtoPedido.sku()).thenReturn("sku1");
        when(produtoPedido.quantidade()).thenReturn(2L);
        when(requisicao.getStatus()).thenReturn(StatusPedidoEnum.SEM_ESTOQUE);

        doThrow(new RuntimeException("Erro estoque"))
                .when(estoqueGateway).baixarEstoque(anyString(), anyInt());
        when(usuarioGateway.obterDadosUsuario(anyString())).thenReturn(mock(Usuario.class));
        doThrow(SemEstoqueException.class).when(estoqueGateway).baixarEstoque(anyString(), anyInt());

        // Act & Assert
        useCase.gerar(requisicao);

        assertThat(requisicao.getStatus())
                .isNotNull()
                .isEqualTo(StatusPedidoEnum.SEM_ESTOQUE);
        verify(estoqueGateway).baixarEstoque("sku1", 2);
        verify(gateway).salvarPedido(any(Pedido.class));
    }

    @Test
    void deveLancarErroNaSolicitacaoPagamento() {
        // Arrange
        Pedido requisicao = mock(Pedido.class);
        ProdutoPedido produtoPedido = mock(ProdutoPedido.class);
        when(requisicao.getClienteId()).thenReturn("1L");
        when(requisicao.getDadosCartao()).thenReturn(new DadosCartaoCliente("numero"));
        when(requisicao.getProdutoPedidos()).thenReturn(List.of(produtoPedido));
        when(produtoPedido.sku()).thenReturn("sku1");
        when(produtoPedido.quantidade()).thenReturn(2L);
        when(requisicao.getStatus()).thenReturn(StatusPedidoEnum.FECHADO_SEM_SUCESSO);

        // Dados do usuário
        var usuario = mock(Usuario.class);
        var dadosCartao = mock(DadosCartaoCliente.class);
        when(requisicao.getDadosCartao()).thenReturn(dadosCartao);
        when(usuarioGateway.obterDadosUsuario(anyString())).thenReturn(usuario);

        // Dados do produto
        Produto produto = mock(Produto.class);
        when(produto.preco()).thenReturn(100.0);
        when(obterProdutoService.obterDadosProduto(anyList())).thenReturn(List.of(produto));

        // Erro na pagamento
        when(pagamentoGateway.solicitar(any())).thenThrow(new IllegalStateException("Erro pagamento"));

        // Act & Assert
        useCase.gerar(requisicao);

        assertThat(requisicao.getStatus())
                .isNotNull()
                .isEqualTo(StatusPedidoEnum.FECHADO_SEM_SUCESSO);
        verify(gateway).salvarPedido(any(Pedido.class));
    }

    @Test
    void lancaExcecaoQuandoUsuarioNaoEncontrado() {
        Pedido requisicao = mock(Pedido.class);
        when(requisicao.getClienteId()).thenReturn("clienteInexistente");
        when(usuarioGateway.obterDadosUsuario(anyString())).thenReturn(null);

        assertThatThrownBy(() -> useCase.gerar(requisicao))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuário não encontrado");
        verify(gateway, never()).salvarPedido(any());
    }

    // Deve salvar pedido com descrição de erro genérico em caso de exceção inesperada
    @Test
    void salvaPedidoComDescricaoDeErroGenericoQuandoException() {
        Pedido requisicao = mock(Pedido.class);
        ProdutoPedido produtoPedido = mock(ProdutoPedido.class);
        when(requisicao.getClienteId()).thenReturn("1L");
        when(requisicao.getDadosCartao()).thenReturn(new DadosCartaoCliente("numero"));
        when(requisicao.getProdutoPedidos()).thenReturn(List.of(produtoPedido));
        when(produtoPedido.sku()).thenReturn("sku1");
        when(produtoPedido.quantidade()).thenReturn(2L);

        when(usuarioGateway.obterDadosUsuario(anyString())).thenReturn(mock(Usuario.class));
        when(obterProdutoService.obterDadosProduto(anyList())).thenReturn(List.of(mock(Produto.class)));
        doThrow(new RuntimeException("Falha inesperada")).when(estoqueGateway).baixarEstoque(anyString(), anyInt());

        useCase.gerar(requisicao);

        verify(gateway).salvarPedido(argThat(pedido ->
                pedido.getDescricaoStatus() != null &&
                        pedido.getDescricaoStatus().contains("Falha inesperada") &&
                        pedido.getStatus() == StatusPedidoEnum.FECHADO_SEM_SUCESSO
        ));
    }

    // Deve salvar pedido com descrição específica para SemEstoqueException
    @Test
    void salvaPedidoComDescricaoDeSemEstoqueException() {
        Pedido requisicao = mock(Pedido.class);
        ProdutoPedido produtoPedido = mock(ProdutoPedido.class);
        when(requisicao.getClienteId()).thenReturn("1L");
        when(requisicao.getDadosCartao()).thenReturn(new DadosCartaoCliente("numero"));
        when(requisicao.getProdutoPedidos()).thenReturn(List.of(produtoPedido));
        when(produtoPedido.sku()).thenReturn("sku1");
        when(produtoPedido.quantidade()).thenReturn(2L);

        when(usuarioGateway.obterDadosUsuario(anyString())).thenReturn(mock(Usuario.class));
        when(obterProdutoService.obterDadosProduto(anyList())).thenReturn(List.of(mock(Produto.class)));
        doThrow(new SemEstoqueException("Sem estoque")).when(estoqueGateway).baixarEstoque(anyString(), anyInt());

        useCase.gerar(requisicao);

        verify(gateway).salvarPedido(argThat(pedido ->
                pedido.getDescricaoStatus() != null &&
                        pedido.getDescricaoStatus().contains("Sem estoque") &&
                        pedido.getStatus() == StatusPedidoEnum.SEM_ESTOQUE
        ));
    }

    // Deve salvar pedido com descrição específica para IllegalStateException
    @Test
    void salvaPedidoComDescricaoDeIllegalStateException() {
        Pedido requisicao = mock(Pedido.class);
        ProdutoPedido produtoPedido = mock(ProdutoPedido.class);
        when(requisicao.getClienteId()).thenReturn("1L");
        when(requisicao.getDadosCartao()).thenReturn(new DadosCartaoCliente("numero"));
        when(requisicao.getProdutoPedidos()).thenReturn(List.of(produtoPedido));
        when(produtoPedido.sku()).thenReturn("sku1");
        when(produtoPedido.quantidade()).thenReturn(2L);

        when(usuarioGateway.obterDadosUsuario(anyString())).thenReturn(mock(Usuario.class));
        when(obterProdutoService.obterDadosProduto(anyList())).thenReturn(List.of(mock(Produto.class)));
        doNothing().when(estoqueGateway).baixarEstoque(anyString(), anyInt());
        when(pagamentoGateway.solicitar(any())).thenThrow(new IllegalStateException("Falha pagamento"));

        useCase.gerar(requisicao);

        verify(gateway, times(1)).salvarPedido(any(Pedido.class));
    }
}
