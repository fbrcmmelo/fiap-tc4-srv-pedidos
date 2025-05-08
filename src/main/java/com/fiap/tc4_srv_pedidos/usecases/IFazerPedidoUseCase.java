package com.fiap.tc4_srv_pedidos.usecases;

import com.fiap.tc4_srv_pedidos.controller.FazerPedidoRequest;

public interface IFazerPedidoUseCase {
    void fazerPedido(FazerPedidoRequest request);
}
