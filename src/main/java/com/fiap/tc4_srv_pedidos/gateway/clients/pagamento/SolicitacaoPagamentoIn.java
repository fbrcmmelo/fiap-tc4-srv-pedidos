package com.fiap.tc4_srv_pedidos.gateway.clients.pagamento;

public record SolicitacaoPagamentoIn(
        String valor,
        String numeroCartao
) {
}
