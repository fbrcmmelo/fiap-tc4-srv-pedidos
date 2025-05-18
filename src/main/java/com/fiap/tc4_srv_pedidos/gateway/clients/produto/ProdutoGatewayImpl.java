package com.fiap.tc4_srv_pedidos.gateway.clients.produto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProdutoGatewayImpl implements IProdutoGateway {

    private final IProdutoClient client;

    @Override
    public Produto obterDadosProduto(String produtoId) {
        try {
            return this.client.obterDados(produtoId);
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao buscar dados do produto, erro: " + e.getMessage());
        }
    }
}
