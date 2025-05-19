package com.fiap.tc4_srv_pedidos.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DadosCartaoClienteTest {

    @Test
    void criaInstanciaQuandoNumeroValido() {
        DadosCartaoCliente cartao = new DadosCartaoCliente("1234567890123456");
        Assertions.assertEquals("1234567890123456", cartao.numero());
    }

    @Test
    void lancaExcecaoQuandoNumeroNulo() {
        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DadosCartaoCliente(null);
        });
        Assertions.assertEquals("Número do cartão inválido", ex.getMessage());
    }

    @Test
    void lancaExcecaoQuandoNumeroVazio() {
        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DadosCartaoCliente("");
        });
        Assertions.assertEquals("Número do cartão inválido", ex.getMessage());
    }

    @Test
    void lancaExcecaoQuandoNumeroApenasEspacos() {
        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new DadosCartaoCliente("   ");
        });
        Assertions.assertEquals("Número do cartão inválido", ex.getMessage());
    }

}