package com.fiap.tc4_srv_pedidos.usecases;

import com.fiap.tc4_srv_pedidos.controller.FazerPedidoRequest;
import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.gateway.IFazerPedidoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FazerPedidoUseCase implements IFazerPedidoUseCase {

    private final IFazerPedidoGateway gateway;

    @Override
    public void fazerPedido(FazerPedidoRequest request) {
        final var pedido = new Pedido(request.clienteId(), request.cartaoCliente(), request.produtosPedido());
        this.gateway.criarPedido(pedido);
    }
}
