package com.fiap.tc4_srv_pedidos.gateway.clients.produto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class ProdutoGatewayImplTest {

    private IProdutoClient client;
    private ProdutoGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        client = mock(IProdutoClient.class);
        gateway = new ProdutoGatewayImpl(client);
    }

    @Test
    void testObterDadosProduto_Success() {
        // Arrange
        String produtoId = "prod123";
        Produto expectedProduto = new Produto("1", "desc", 10.9); // Instantiate as per your class
        when(client.obterDados(produtoId)).thenReturn(expectedProduto);

        // Act
        Produto resultado = gateway.obterDadosProduto(produtoId);

        // Assert
        verify(client).obterDados(produtoId);
        assertThat(resultado).isEqualTo(expectedProduto);
    }

    @Test
    void testObterDadosProduto_ClientThrowsException() {
        // Arrange
        String produtoId = "prod456";
        doThrow(new RuntimeException("Erro no client"))
                .when(client).obterDados(produtoId);

        // Act & Assert
        assertThatThrownBy(() -> gateway.obterDadosProduto(produtoId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Falha ao buscar dados do produto");
    }
}
