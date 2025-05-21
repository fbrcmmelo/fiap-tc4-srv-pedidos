package com.fiap.tc4_srv_pedidos.usecases;

import com.fiap.tc4_srv_pedidos.controller.SolicitacaoPagamentoOut;
import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.domain.ProdutoPedido;
import com.fiap.tc4_srv_pedidos.domain.StatusPedidoEnum;
import com.fiap.tc4_srv_pedidos.gateway.IPedidoGateway;
import com.fiap.tc4_srv_pedidos.gateway.clients.estoque.IEstoqueGateway;
import com.fiap.tc4_srv_pedidos.gateway.clients.pagamento.IPagamentoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class AtualizarPedidoUseCaseTest {

    private IPedidoGateway pedidoGateway;
    private IPagamentoGateway pagamentoGateway;
    private IEstoqueGateway estoqueGateway;
    private AtualizarPedidoUseCase useCase;

    @BeforeEach
    void setUp() {
        pedidoGateway = mock(IPedidoGateway.class);
        pagamentoGateway = mock(IPagamentoGateway.class);
        estoqueGateway = mock(IEstoqueGateway.class);
        useCase = new AtualizarPedidoUseCase(pedidoGateway, pagamentoGateway, estoqueGateway);
    }

    @Test
    void deveAtualizarStatusPedidoNormal() {
        // Arrange
        String solicitacaoId = "id123";
        Pedido pedido = mock(Pedido.class);
        SolicitacaoPagamentoOut solicitacao = new SolicitacaoPagamentoOut("", StatusPedidoEnum.ABERTO);

        when(pedidoGateway.buscarPedidoPorSolicitacaoPagamentoId(solicitacaoId)).thenReturn(pedido);
        when(pagamentoGateway.buscarSolicitacaoPagamento(solicitacaoId)).thenReturn(solicitacao);

        // Act
        useCase.atualizar(solicitacaoId);

        // Assert
        verify(pedido).atualizarStatus(StatusPedidoEnum.ABERTO);
        verify(pedidoGateway).salvarPedido(pedido);
    }

    @Test
    void deveReverterEstoqueParaPagamentoInvalido() {
        // Arrange
        String solicitacaoId = "id456";
        Pedido pedido = mock(Pedido.class);
        // Criando produtos fictícios
        var produto = mock(ProdutoPedido.class);
        when(produto.sku()).thenReturn("sku1");
        when(produto.quantidade()).thenReturn(1L);
        when(pedido.getProdutoPedidos()).thenReturn(List.of(produto));

        SolicitacaoPagamentoOut solicitacao = new SolicitacaoPagamentoOut("", StatusPedidoEnum.FECHADO_SEM_SUCESSO);

        when(pedidoGateway.buscarPedidoPorSolicitacaoPagamentoId(solicitacaoId)).thenReturn(pedido);
        when(pagamentoGateway.buscarSolicitacaoPagamento(solicitacaoId)).thenReturn(solicitacao);

        // Act
        useCase.atualizar(solicitacaoId);

        // Assert
        verify(estoqueGateway).incrementarEstoque("sku1", 1);
        verify(pedido).atualizarStatus(StatusPedidoEnum.FECHADO_SEM_SUCESSO);
        verify(pedidoGateway).salvarPedido(pedido);
    }

}

