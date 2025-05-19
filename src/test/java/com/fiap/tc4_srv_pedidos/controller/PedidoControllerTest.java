package com.fiap.tc4_srv_pedidos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.usecases.IAtualizarPedidoUseCase;
import com.fiap.tc4_srv_pedidos.usecases.IGerarPedidoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

public class PedidoControllerTest {

    private IGerarPedidoUseCase gerarPedidoUseCase;
    private IAtualizarPedidoUseCase atualizarPedidoUseCase;
    private PedidoController controller;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        gerarPedidoUseCase = mock(IGerarPedidoUseCase.class);
        atualizarPedidoUseCase = mock(IAtualizarPedidoUseCase.class);
        controller = new PedidoController(gerarPedidoUseCase, atualizarPedidoUseCase);
        objectMapper = new ObjectMapper();
    }

    @Test
    void deveProcessarGerarPedidoComSucesso() throws Exception {
        // Arrange
        Pedido pedido = new Pedido();
        String mensagem = objectMapper.writeValueAsString(pedido);

        // Act
        controller.gerarPedido(mensagem);

        // Assert
        verify(gerarPedidoUseCase).gerar(any(Pedido.class));
    }

    @Test
    void deveRegistrarErroAoProcessarGerarPedido() throws Exception {
        // Arrange
        String mensagemInvalida = "mensagem inválida";

        // Act
        controller.gerarPedido(mensagemInvalida);

        // Assert
        verify(gerarPedidoUseCase, never()).gerar(any());
    }

    @Test
    void solicitacaoAtualizadaDeveChamarAtualizarPedido() {
        // Arrange
        String solicitacaoId = "solicitacao123";
        SolicitacaoPagamentoOut solicitacao = mock(SolicitacaoPagamentoOut.class);
        when(solicitacao.solicitacaoId()).thenReturn(solicitacaoId);

        Consumer<SolicitacaoPagamentoOut> consumer = controller.solicitacaoAtualizada();

        // Act
        consumer.accept(solicitacao);

        // Assert
        verify(atualizarPedidoUseCase).atualizar(solicitacaoId);
    }

    @Test
    void solicitacaoAtualizadaDeveRegistrarErroAoLidarComExcecao() {
        // Arrange
        SolicitacaoPagamentoOut solicitacao = mock(SolicitacaoPagamentoOut.class);
        when(solicitacao.solicitacaoId()).thenReturn("id");
        doThrow(new RuntimeException("Erro ao atualizar"))
                .when(atualizarPedidoUseCase).atualizar(anyString());

        Consumer<SolicitacaoPagamentoOut> consumer = controller.solicitacaoAtualizada();

        // Act
        consumer.accept(solicitacao);
        verify(atualizarPedidoUseCase).atualizar("id");
    }
}
