package com.fiap.tc4_srv_pedidos.gateway.clients;

import com.fiap.tc4_srv_pedidos.gateway.IEstoqueGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EstoqueGatewayImpl implements IEstoqueGateway {

    private final IEstoqueClient client;

    @Override
    public void incrementarEstoque(String produtoId, int quantidade) {
        this.client.incrementarEstoque(new ProdutoEstoqueRequest(produtoId, quantidade));
    }
}
