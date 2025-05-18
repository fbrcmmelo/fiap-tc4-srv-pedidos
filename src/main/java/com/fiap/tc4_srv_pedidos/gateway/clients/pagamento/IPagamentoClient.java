package com.fiap.tc4_srv_pedidos.gateway.clients.pagamento;

import com.fiap.tc4_srv_pedidos.controller.SolicitacaoPagamentoOut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "pagamento", url = "http://localhost:8084")
public interface IPagamentoClient {

    @GetMapping("/pagamento/{solicitacaoId}")
    SolicitacaoPagamentoOut buscarSolicitacaoPorId(@PathVariable String solicitacaoId);

    @PostMapping("/pagamento")
    SolicitacaoPagamentoOut solicitar(@RequestBody SolicitacaoPagamentoIn solicitacaoPagamentoIn);
}
