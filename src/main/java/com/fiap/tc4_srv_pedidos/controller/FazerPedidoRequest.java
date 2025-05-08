package com.fiap.tc4_srv_pedidos.controller;

import com.fiap.tc4_srv_pedidos.domain.DadosCartaoCliente;
import com.fiap.tc4_srv_pedidos.domain.ProdutoPedido;

import java.util.List;

public record FazerPedidoRequest(String clienteId, DadosCartaoCliente cartaoCliente,
                                 List<ProdutoPedido> produtosPedido) {

}
