package com.fiap.tc4_srv_pedidos.controller;

import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.domain.StatusPedidoEnum;

import java.time.Instant;

public record PedidoDTO(
        String clienteId,
        String transacaoId,
        StatusPedidoEnum status,
        String descricaoStatus,
        Instant criadoEm
) {
    public static PedidoDTO from(Pedido pedido) {
        return new PedidoDTO(
                pedido.getClienteId(),
                pedido.getTransacaoId(),
                pedido.getStatus(),
                pedido.getDescricaoStatus(),
                pedido.getCriadoEm()
        );
    }
}
