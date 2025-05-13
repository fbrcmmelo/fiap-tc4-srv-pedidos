package com.fiap.tc4_srv_pedidos.controller;

import com.fiap.tc4_srv_pedidos.domain.StatusPedidoEnum;

public record SolicitacaoPagamentoOut(String solicitacaoId, StatusPedidoEnum status) {
}
