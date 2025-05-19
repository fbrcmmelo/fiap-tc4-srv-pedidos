package com.fiap.tc4_srv_pedidos.gateway.clients.estoque;

import com.fiap.tc4_srv_pedidos.gateway.clients.produto.ProdutoEstoqueRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class EstoqueGatewayImplTest {

    private IEstoqueClient client;
    private EstoqueGatewayImpl estoqueGateway;

    @BeforeEach
    void setUp() {
        client = mock(IEstoqueClient.class);
        estoqueGateway = new EstoqueGatewayImpl(client);
    }

    @Test
    void testIncrementarEstoque_Success() {
        // Arrange
        String produtoId = "prod123";
        int quantidade = 10;

        // Act
        estoqueGateway.incrementarEstoque(produtoId, quantidade);

        // Assert
        verify(client, times(1)).incrementarEstoque(any(ProdutoEstoqueRequest.class));
    }

    @Test
    void testIncrementarEstoque_ClientThrowsException() {
        // Arrange
        String produtoId = "prod123";
        int quantidade = 5;
        doThrow(new RuntimeException("Erro no client"))
                .when(client).incrementarEstoque(any());

        // Act & Assert
        assertThatThrownBy(() -> estoqueGateway.incrementarEstoque(produtoId, quantidade))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Falha ao incrementar estoque");
    }

    @Test
    void testBaixarEstoque_Success() {
        // Arrange
        String produtoId = "prod456";
        int quantidade = 3;

        // Act
        estoqueGateway.baixarEstoque(produtoId, quantidade);

        // Assert
        verify(client, times(1)).baixarEstoque(any(ProdutoEstoqueRequest.class));
    }

    @Test
    void testBaixarEstoque_ClientThrowsException() {
        // Arrange
        String produtoId = "prod456";
        int quantidade = 7;
        doThrow(new RuntimeException("Erro no client"))
                .when(client).baixarEstoque(any());

        // Act & Assert
        assertThatThrownBy(() -> estoqueGateway.baixarEstoque(produtoId, quantidade))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Falha ao baixar estoque");
    }
}
