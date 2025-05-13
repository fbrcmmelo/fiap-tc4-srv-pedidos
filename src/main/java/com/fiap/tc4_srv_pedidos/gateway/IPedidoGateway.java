package com.fiap.tc4_srv_pedidos.gateway;

import com.fiap.tc4_srv_pedidos.domain.Pedido;

public interface IPedidoGateway {

    void salvarPedido(Pedido pedido);

    Pedido buscarPedidoPorSolicitacaoPagamentoId(String solicitacaoId);
}
