package com.fiap.tc4_srv_pedidos.usecases;

import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.gateway.IPedidoGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


class ListarUseCaseImplTest {

    @InjectMocks
    private ListarUseCaseImpl useCase;
    @Mock
    private IPedidoGateway pedidoGateway;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void listarPedidos_DeveRetornarListaDePedidosQuandoExistemPedidos() {
        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();
        when(pedidoGateway.listarPedidos()).thenReturn(List.of(pedido1, pedido2));

        List<Pedido> pedidos = useCase.listarPedidos();

        assertThat(pedidos).hasSize(2);
        assertThat(pedidos).containsExactly(pedido1, pedido2);
    }

    @Test
    void listarPedidos_DeveRetornarListaVaziaQuandoNaoExistemPedidos() {
        when(pedidoGateway.listarPedidos()).thenReturn(List.of());

        List<Pedido> pedidos = useCase.listarPedidos();

        assertThat(pedidos).isEmpty();
    }

}