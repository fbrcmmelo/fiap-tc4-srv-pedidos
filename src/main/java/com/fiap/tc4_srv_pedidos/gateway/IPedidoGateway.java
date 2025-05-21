package com.fiap.tc4_srv_pedidos.gateway;

import com.fiap.tc4_srv_pedidos.domain.Pedido;

import java.util.List;

public interface IPedidoGateway {

    void salvarPedido(Pedido pedido);

    Pedido buscarPedidoPorSolicitacaoPagamentoId(String solicitacaoId);

    List<Pedido> listarPedidos();

}
