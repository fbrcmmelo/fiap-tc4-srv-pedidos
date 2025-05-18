package com.fiap.tc4_srv_pedidos.gateway.clients.usuario;

import com.fiap.tc4_srv_pedidos.domain.DadosCartaoCliente;

public record Usuario(String usuarioId, String nome, String email, DadosCartaoCliente dadosCartaoCliente) {
}
