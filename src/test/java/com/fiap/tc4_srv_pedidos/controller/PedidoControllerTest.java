package com.fiap.tc4_srv_pedidos.controller;

import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.usecases.IListarPedidosUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class PedidoControllerTest {

    @InjectMocks
    private PedidoController controller;
    @Mock
    private IListarPedidosUseCase listarUseCase;

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
    void listarPedidos_DeveRetornarListaDePedidosDTOQuandoExistemPedidos() {
        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();
        when(listarUseCase.listarPedidos()).thenReturn(List.of(pedido1, pedido2));

        List<PedidoDTO> pedidos = controller.listarPedidos();

        assertThat(pedidos)
                .hasSize(2);
        assertThat(pedidos).
                containsExactly(PedidoDTO.from(pedido1), PedidoDTO.from(pedido2));
    }

    @Test
    void listarPedidos_DeveRetornarListaVaziaDePedidosDTOQuandoNaoExistemPedidos() {
        when(listarUseCase.listarPedidos()).thenReturn(List.of());

        List<PedidoDTO> pedidos = controller.listarPedidos();

        assertThat(pedidos).isEmpty();
    }

}