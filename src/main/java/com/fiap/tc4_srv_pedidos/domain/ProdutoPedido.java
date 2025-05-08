package com.fiap.tc4_srv_pedidos.domain;

import java.util.Objects;
import java.util.Optional;

public record ProdutoPedido(String sku, Long quantidade) {

    public ProdutoPedido {
        Objects.requireNonNull(sku, "Sku produto não pode ser nulo");

        final var quantidadeParam = Optional.ofNullable(quantidade).orElse(0L);

        if (quantidadeParam < 1L) {
            throw new IllegalArgumentException("Quantidade de produto inválida");
        }
    }
}
