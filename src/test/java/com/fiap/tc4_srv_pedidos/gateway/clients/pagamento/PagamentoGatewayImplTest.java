package com.fiap.tc4_srv_pedidos.gateway.clients.pagamento;

import com.fiap.tc4_srv_pedidos.controller.SolicitacaoPagamentoOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class PagamentoGatewayImplTest {

    private IPagamentoClient client;
    private PagamentoGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        client = mock(IPagamentoClient.class);
        gateway = new PagamentoGatewayImpl(client);
    }

    @Test
    void testBuscarSolicitacaoPagamento_Success() {
        // Arrange
        String solicitacaoId = "sol123";
        SolicitacaoPagamentoOut expectedResponse = mock(SolicitacaoPagamentoOut.class);
        when(client.buscarSolicitacaoPorId(solicitacaoId)).thenReturn(expectedResponse);

        // Act
        SolicitacaoPagamentoOut response = gateway.buscarSolicitacaoPagamento(solicitacaoId);

        // Assert
        verify(client).buscarSolicitacaoPorId(solicitacaoId);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    void testBuscarSolicitacaoPagamento_ClientThrowsException() {
        // Arrange
        String solicitacaoId = "sol456";
        doThrow(new RuntimeException("Erro no client"))
                .when(client).buscarSolicitacaoPorId(solicitacaoId);

        // Act & Assert
        assertThatThrownBy(() -> gateway.buscarSolicitacaoPagamento(solicitacaoId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Falha ao buscar solicitacao de pagamento");
    }

    @Test
    void testSolicitar_Success() {
        // Arrange
        SolicitacaoPagamentoIn solicitacaoIn = new SolicitacaoPagamentoIn("122", "numero");
        SolicitacaoPagamentoOut expectedResponse = mock(SolicitacaoPagamentoOut.class);
        when(client.solicitar(solicitacaoIn)).thenReturn(expectedResponse);

        // Act
        SolicitacaoPagamentoOut response = gateway.solicitar(solicitacaoIn);

        // Assert
        verify(client).solicitar(solicitacaoIn);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    void testSolicitar_ClientThrowsException() {
        // Arrange
        SolicitacaoPagamentoIn solicitacaoIn = new SolicitacaoPagamentoIn("122", "numero");
        doThrow(new RuntimeException("Erro no client"))
                .when(client).solicitar(solicitacaoIn);

        // Act & Assert
        assertThatThrownBy(() -> gateway.solicitar(solicitacaoIn))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Falha ao solicitar pagamento");
    }
}
