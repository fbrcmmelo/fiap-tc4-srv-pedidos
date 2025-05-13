package com.fiap.tc4_srv_pedidos.gateway.clients;

public record ProdutoEstoqueRequest(String produtoId, int quantidade) {
}
