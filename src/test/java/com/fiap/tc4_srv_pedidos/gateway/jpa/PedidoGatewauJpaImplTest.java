package com.fiap.tc4_srv_pedidos.gateway.jpa;

import com.fiap.tc4_srv_pedidos.domain.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class PedidoGatewauJpaImplTest {

    private PedidoJpaRepository repository;
    private PedidoGatewauJpaImpl gateway;

    @BeforeEach
    void setUp() {
        repository = mock(PedidoJpaRepository.class);
        gateway = new PedidoGatewauJpaImpl(repository);
    }

    @Test
    void testSalvarPedido() {
        // Arrange
        Pedido pedido = new Pedido();

        // Act
        gateway.salvarPedido(pedido);

        // Assert
        verify(repository).save(any());
    }

    @Test
    void testBuscarPedidoPorSolicitacaoPagamentoId_Sucesso() {
        // Arrange
        String solicitacaoId = "trans123";

        PedidoEntityJpa entity = new PedidoEntityJpa();
        entity.setTransacaoId(solicitacaoId);
        Pedido pedido = new Pedido();

        when(repository.findOne(any(Example.class))).thenReturn(Optional.of(entity));

        // Act
        Pedido resultado = gateway.buscarPedidoPorSolicitacaoPagamentoId(solicitacaoId);

        // Assert
        verify(repository).findOne(any(Example.class));
        assertThat(resultado).isNotNull();
    }

    @Test
    void testBuscarPedidoPorSolicitacaoPagamentoId_NaoEncontrado() {
        // Arrange
        String solicitacaoId = "transInexistente";

        when(repository.findOne(any(Example.class))).thenReturn(Optional.empty());

        // Act & assert
        assertThatThrownBy(() ->
                gateway.buscarPedidoPorSolicitacaoPagamentoId(solicitacaoId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pedido não encontrado");
    }

    @Test
    void listarPedidos_RetornaListaDePedidos() {
        // Arrange
        PedidoEntityJpa entity1 = new PedidoEntityJpa();
        PedidoEntityJpa entity2 = new PedidoEntityJpa();
        when(repository.findAll()).thenReturn(List.of(entity1, entity2));

        // Act
        List<Pedido> pedidos = gateway.listarPedidos();

        // Assert
        verify(repository).findAll();
        assertThat(pedidos).hasSize(2);
    }

    @Test
    void listarPedidos_RetornaListaVaziaQuandoNaoHaPedidos() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of());

        // Act
        List<Pedido> pedidos = gateway.listarPedidos();

        // Assert
        verify(repository).findAll();
        assertThat(pedidos).isEmpty();
    }

}
