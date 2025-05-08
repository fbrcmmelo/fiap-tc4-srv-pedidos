package com.fiap.tc4_srv_pedidos.gateway;

import com.fiap.tc4_srv_pedidos.domain.Pedido;

public interface IFazerPedidoGateway {

    void criarPedido(Pedido pedido);
}
