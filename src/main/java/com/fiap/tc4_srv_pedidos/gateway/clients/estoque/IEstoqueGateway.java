package com.fiap.tc4_srv_pedidos.gateway.clients.estoque;

public interface IEstoqueGateway {

    void incrementarEstoque(String produtoId, int quantidade);

    void baixarEstoque(String produtoId, int quantidade);
}
