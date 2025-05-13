package com.fiap.tc4_srv_pedidos.gateway.clients;

import com.fiap.tc4_srv_pedidos.controller.SolicitacaoPagamentoOut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "pagamento", url = "http://localhost:8084")
public interface IPagamentoClient {

    @GetMapping("/pagamento/{solicitacaoId}")
    SolicitacaoPagamentoOut buscarSolicitacaoPorId(@PathVariable String solicitacaoId);
}
