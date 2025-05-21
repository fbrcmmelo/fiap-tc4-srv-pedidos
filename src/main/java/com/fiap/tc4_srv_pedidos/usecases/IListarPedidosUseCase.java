package com.fiap.tc4_srv_pedidos.usecases;

import com.fiap.tc4_srv_pedidos.domain.Pedido;

import java.util.List;

public interface IListarPedidosUseCase {

    List<Pedido> listarPedidos();
}
