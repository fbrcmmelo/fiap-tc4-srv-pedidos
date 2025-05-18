package com.fiap.tc4_srv_pedidos.gateway.clients.pagamento;

import com.fiap.tc4_srv_pedidos.controller.SolicitacaoPagamentoOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PagamentoGatewayImpl implements IPagamentoGateway {

    private final IPagamentoClient client;

    @Override
    public SolicitacaoPagamentoOut buscarSolicitacaoPagamento(String solicitacaoId) {
        try {
            return this.client.buscarSolicitacaoPorId(solicitacaoId);
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao buscar solicitacao de pagamento, erro: " + e.getMessage());
        }
    }

    @Override
    public SolicitacaoPagamentoOut solicitar(SolicitacaoPagamentoIn solicitacaoPagamentoIn) {
        try {
            return this.client.solicitar(solicitacaoPagamentoIn);
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao solicitar pagamento, erro: " + e.getMessage());
        }
    }
}
