package com.fiap.tc4_srv_pedidos.gateway.clients.usuario;

public record Endereco(String rua,
                       String numero,
                       String endereco,
                       String complemento,
                       String cep,
                       String cidade) {
}
