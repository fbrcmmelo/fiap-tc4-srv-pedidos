package com.fiap.tc4_srv_pedidos.gateway.clients.estoque;

import com.fiap.tc4_srv_pedidos.gateway.clients.produto.ProdutoEstoqueRequest;
import com.fiap.tc4_srv_pedidos.usecases.SemEstoqueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EstoqueGatewayImpl implements IEstoqueGateway {

    private final IEstoqueClient client;

    @Override
    public void incrementarEstoque(String produtoId, int quantidade) {
        try {
            this.client.incrementarEstoque(new ProdutoEstoqueRequest(produtoId, quantidade));
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao incrementar estoque, erro: " + e.getMessage());
        }
    }

    @Override
    public void baixarEstoque(String produtoId, int quantidade) {
        try {
            this.client.baixarEstoque(new ProdutoEstoqueRequest(produtoId, quantidade));
        } catch (Exception e) {
            if (e.getMessage().contains("Quantidade insuficiente em estoque")) {
                throw new SemEstoqueException("Quantidade insuficiente em estoque");
            }

            throw new IllegalStateException("Falha ao baixar estoque, erro: " + e.getMessage());
        }
    }
}
