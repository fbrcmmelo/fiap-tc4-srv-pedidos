package com.fiap.tc4_srv_pedidos.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class PedidoTest {

    @Test
    void criaPedidoComDadosValidos() {
        DadosCartaoCliente cartao = new DadosCartaoCliente("1234567890123456");
        ProdutoPedido produto = new ProdutoPedido("prod1", 2L);
        Pedido pedido = new Pedido("cliente1", cartao, List.of(produto));
        Assertions.assertEquals("cliente1", pedido.getClienteId());
        Assertions.assertEquals(cartao, pedido.getDadosCartao());
        Assertions.assertEquals(1, pedido.getProdutoPedidos().size());
    }

    @Test
    void lancaExcecaoQuandoClienteIdNulo() {
        DadosCartaoCliente cartao = new DadosCartaoCliente("1234567890123456");
        ProdutoPedido produto = new ProdutoPedido("prod1", 2L);
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class, () ->
                new Pedido(null, cartao, List.of(produto))
        );
        Assertions.assertEquals("Id do cliente não pode ser nulo", ex.getMessage());
    }

    @Test
    void lancaExcecaoQuandoDadosCartaoNulo() {
        ProdutoPedido produto = new ProdutoPedido("prod1", 2L);
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class, () ->
                new Pedido("cliente1", null, List.of(produto))
        );
        Assertions.assertEquals("Dados cartao do cliente não pode ser nulo", ex.getMessage());
    }

    @Test
    void lancaExcecaoQuandoListaProdutosVazia() {
        DadosCartaoCliente cartao = new DadosCartaoCliente("1234567890123456");
        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class, () ->
                new Pedido("cliente1", cartao, Collections.emptyList())
        );
        Assertions.assertEquals("Lista de produtos do pedido não pode estar vazia", ex.getMessage());
    }

    @Test
    void lancaExcecaoQuandoListaProdutosNula() {
        DadosCartaoCliente cartao = new DadosCartaoCliente("1234567890123456");
        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class, () ->
                new Pedido("cliente1", cartao, null)
        );
        Assertions.assertEquals("Lista de produtos do pedido não pode estar vazia", ex.getMessage());
    }

    @Test
    void atualizaStatusComValorValido() {
        DadosCartaoCliente cartao = new DadosCartaoCliente("1234567890123456");
        ProdutoPedido produto = new ProdutoPedido("prod1", 2L);
        Pedido pedido = new Pedido("cliente1", cartao, List.of(produto));
        pedido.atualizarStatus(StatusPedidoEnum.FECHADO_COM_SUCESSO);
        Assertions.assertEquals(StatusPedidoEnum.FECHADO_COM_SUCESSO, pedido.getStatus());
        Assertions.assertNotNull(pedido.getAtualizadoEm());
    }

    @Test
    void lancaExcecaoQuandoAtualizaStatusComNulo() {
        DadosCartaoCliente cartao = new DadosCartaoCliente("1234567890123456");
        ProdutoPedido produto = new ProdutoPedido("prod1", 2L);
        Pedido pedido = new Pedido("cliente1", cartao, List.of(produto));
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class, () ->
                pedido.atualizarStatus(null)
        );
        Assertions.assertEquals("Status do pedido não pode ser nulo", ex.getMessage());
    }

    @Test
    void setaTransacaoIdComValorValido() {
        DadosCartaoCliente cartao = new DadosCartaoCliente("1234567890123456");
        ProdutoPedido produto = new ProdutoPedido("prod1", 2L);
        Pedido pedido = new Pedido("cliente1", cartao, List.of(produto));
        pedido.registrarTransacaoId("transacao123");
        Assertions.assertEquals("transacao123", pedido.getTransacaoId());
        Assertions.assertNotNull(pedido.getAtualizadoEm());
    }

    @Test
    void lancaExcecaoQuandoSetaTransacaoIdNulo() {
        DadosCartaoCliente cartao = new DadosCartaoCliente("1234567890123456");
        ProdutoPedido produto = new ProdutoPedido("prod1", 2L);
        Pedido pedido = new Pedido("cliente1", cartao, List.of(produto));
        NullPointerException ex = Assertions.assertThrows(NullPointerException.class, () ->
                pedido.registrarTransacaoId(null)
        );
        Assertions.assertEquals("Solicitação não pode ser nula", ex.getMessage());
    }

    @Test
    void lancaExcecaoQuandoSetaTransacaoIdDuasVezes() {
        DadosCartaoCliente cartao = new DadosCartaoCliente("1234567890123456");
        ProdutoPedido produto = new ProdutoPedido("prod1", 2L);
        Pedido pedido = new Pedido("cliente1", cartao, List.of(produto));
        pedido.registrarTransacaoId("transacao123");
        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class, () ->
                pedido.registrarTransacaoId("transacao456")
        );
        Assertions.assertEquals("Transação já foi setada", ex.getMessage());
    }

}