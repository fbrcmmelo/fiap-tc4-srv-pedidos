package com.fiap.tc4_srv_pedidos.usecases;

import com.fiap.tc4_srv_pedidos.domain.Pedido;

public interface IGerarPedidoUseCase {
    void gerar(Pedido pedido);
}
