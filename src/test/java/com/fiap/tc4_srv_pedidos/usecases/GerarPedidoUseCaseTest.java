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
        when(solicitacao.status()).thenReturn(StatusPedidoEnum.ABERTO);
        when(solicitacao.solicitacaoId()).thenReturn("trans123");
        when(pagamentoGateway.solicitar(any())).thenReturn(solicitacao);

        // Act
        useCase.gerar(requisicao);

        // Assert
        verify(estoqueGateway).baixarEstoque("sku1", 2);
        verify(gateway).salvarPedido(any(Pedido.class));
    }

    @Test
    void deveLancarErroAoBaixarEstoque() {
        // Arrange
        Pedido requisicao = mock(Pedido.class);
        ProdutoPedido produtoPedido = mock(ProdutoPedido.class);
        when(requisicao.getClienteId()).thenReturn("1L");
        when(requisicao.getDadosCartao()).thenReturn(new DadosCartaoCliente("numero"));
        when(requisicao.getProdutoPedidos()).thenReturn(List.of(produtoPedido));
        when(produtoPedido.sku()).thenReturn("sku1");
        when(produtoPedido.quantidade()).thenReturn(2L);

        doThrow(new RuntimeException("Erro estoque"))
                .when(estoqueGateway).baixarEstoque(anyString(), anyInt());
        when(usuarioGateway.obterDadosUsuario(anyString())).thenReturn(mock(Usuario.class));

        // Act & Assert
        assertThatThrownBy(() -> useCase.gerar(requisicao))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Erro estoque");
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
        when(pagamentoGateway.solicitar(any())).thenThrow(new RuntimeException("Erro pagamento"));

        // Act & Assert
        assertThatThrownBy(() -> useCase.gerar(requisicao))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Erro pagamento");
    }
}
