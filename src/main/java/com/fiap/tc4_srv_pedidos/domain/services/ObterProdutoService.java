package com.fiap.tc4_srv_pedidos.domain.services;

import com.fiap.tc4_srv_pedidos.gateway.clients.produto.IProdutoGateway;
import com.fiap.tc4_srv_pedidos.gateway.clients.produto.Produto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObterProdutoService {

    private final IProdutoGateway produtoGateway;

    public List<Produto> obterDadosProduto(List<String> produtosId) {
        var listaProduto = new ArrayList<Produto>();

        try {
            for (String produtoId : produtosId) {
                if (produtoId == null || produtoId.isEmpty()) {
                    throw new IllegalArgumentException("Produto ID não pode ser nulo ou vazio");
                }

                listaProduto.add(this.produtoGateway.obterDadosProduto(produtoId));
            }
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao buscar dados do produto, erro: " + e.getMessage());
        }

        return listaProduto;
    }
}
