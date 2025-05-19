package com.fiap.tc4_srv_pedidos.gateway.clients.usuario;

import com.fiap.tc4_srv_pedidos.domain.DadosCartaoCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class UsuarioGatewayImplTest {

    private IUsuarioClient client;
    private UsuarioGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        client = mock(IUsuarioClient.class);
        gateway = new UsuarioGatewayImpl(client);
    }

    @Test
    void testObterDadosUsuario_Success() {
        // Arrange
        String solicitacaoId = "user123";
        Usuario expectedUsuario = new Usuario("1", "nome", "email", new DadosCartaoCliente("numero"));
        when(client.obterDados(solicitacaoId)).thenReturn(expectedUsuario);

        // Act
        Usuario resultado = gateway.obterDadosUsuario(solicitacaoId);

        // Assert
        verify(client).obterDados(solicitacaoId);
        assertThat(resultado).isEqualTo(expectedUsuario);
    }

    @Test
    void testObterDadosUsuario_ClientThrowsException() {
        // Arrange
        String solicitacaoId = "user456";
        doThrow(new RuntimeException("Erro no client"))
                .when(client).obterDados(solicitacaoId);

        // Act & Assert
        assertThatThrownBy(() -> gateway.obterDadosUsuario(solicitacaoId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Falha ao buscar dados do usuario");
    }
}
