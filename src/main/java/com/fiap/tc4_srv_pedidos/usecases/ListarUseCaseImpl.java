package com.fiap.tc4_srv_pedidos.usecases;

import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.gateway.IPedidoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListarUseCaseImpl implements IListarPedidosUseCase{

    private final IPedidoGateway pedidoGateway;

    @Override
    public List<Pedido> listarPedidos() {
        return pedidoGateway.listarPedidos();
    }
}
