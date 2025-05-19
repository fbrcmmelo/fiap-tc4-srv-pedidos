package com.fiap.tc4_srv_pedidos.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProdutoPedidoTest {

    @Test
    void criaProdutoPedidoComDadosValidos() {
        ProdutoPedido produtoPedido = new ProdutoPedido("sku123", 5L);
        assertEquals("sku123", produtoPedido.sku());
        assertEquals(5L, produtoPedido.quantidade());
    }

    @Test
    void lancaExcecaoQuandoSkuNulo() {
        NullPointerException ex = assertThrows(NullPointerException.class, () ->
                new ProdutoPedido(null, 5L)
        );
        assertEquals("Sku produto não pode ser nulo", ex.getMessage());
    }

    @Test
    void lancaExcecaoQuandoQuantidadeNula() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProdutoPedido("sku123", null)
        );
        assertEquals("Quantidade de produto inválida", ex.getMessage());
    }

    @Test
    void lancaExcecaoQuandoQuantidadeMenorQueUm() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProdutoPedido("sku123", 0L)
        );
        assertEquals("Quantidade de produto inválida", ex.getMessage());
    }

    @Test
    void lancaExcecaoQuandoQuantidadeNegativa() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProdutoPedido("sku123", -1L)
        );
        assertEquals("Quantidade de produto inválida", ex.getMessage());
    }

}