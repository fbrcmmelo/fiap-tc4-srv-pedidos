package com.fiap.tc4_srv_pedidos.gateway.clients.pagamento;

import com.fiap.tc4_srv_pedidos.controller.SolicitacaoPagamentoOut;

public interface IPagamentoGateway {
    SolicitacaoPagamentoOut buscarSolicitacaoPagamento(String solicitacaoId);

    SolicitacaoPagamentoOut solicitar(SolicitacaoPagamentoIn solicitacaoPagamentoIn);
}
