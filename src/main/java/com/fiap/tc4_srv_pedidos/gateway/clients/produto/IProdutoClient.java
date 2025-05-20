package com.fiap.tc4_srv_pedidos.gateway.clients.produto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "produtos", url = "http://localhost:8086")
public interface IProdutoClient {

    @GetMapping("/produtos/{produtoId}")
    Produto obterDados(@PathVariable String produtoId);
}
