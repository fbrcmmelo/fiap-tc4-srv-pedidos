package com.fiap.tc4_srv_pedidos.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.*;

@Getter
@NoArgsConstructor
public class Pedido {

    private String id;
    private String clienteId;
    private DadosCartaoCliente dadosCartao;
    private List<ProdutoPedido> produtoPedidos;
    private StatusPedidoEnum status;
    private UUID transacaoId;
    private Instant criadoEm;
    private Instant atualizadoEm;
    private Instant deletadoEm;

    public Pedido(String clienteId, DadosCartaoCliente dadosCartao, List<ProdutoPedido> produtoPedidos) {
        Objects.requireNonNull(clienteId, "Id do cliente não pode ser nulo");
        Objects.requireNonNull(dadosCartao, "Dados cartao do cliente não pode ser nulo");

        final var produtoList = Optional.ofNullable(produtoPedidos).orElse(new ArrayList<>());

        if (produtoList.isEmpty()) {
            throw new IllegalArgumentException("Lista de produtos do pedido não pode estar vazia");
        }

        this.clienteId = clienteId;
        this.dadosCartao = dadosCartao;
        this.produtoPedidos = produtoPedidos;
    }
}
