package com.fiap.tc4_srv_pedidos.gateway.clients.estoque;

import com.fiap.tc4_srv_pedidos.gateway.clients.produto.ProdutoEstoqueRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "estoque", url = "http://localhost:8083")
public interface IEstoqueClient {

    @PostMapping("consumer-incrementar-estoque")
    void incrementarEstoque(@RequestBody ProdutoEstoqueRequest request);

    @PostMapping("consumer-baixar-estoque")
    void baixarEstoque(@RequestBody ProdutoEstoqueRequest produtoEstoqueRequest);
}
