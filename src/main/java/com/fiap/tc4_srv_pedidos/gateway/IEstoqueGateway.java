package com.fiap.tc4_srv_pedidos.gateway;

public interface IEstoqueGateway {

    void incrementarEstoque(String produtoId, int quantidade);
}
