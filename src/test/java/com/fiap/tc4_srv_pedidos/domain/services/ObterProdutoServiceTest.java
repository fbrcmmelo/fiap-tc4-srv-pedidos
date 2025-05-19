package com.fiap.tc4_srv_pedidos.domain.services;

import com.fiap.tc4_srv_pedidos.gateway.clients.produto.IProdutoGateway;
import com.fiap.tc4_srv_pedidos.gateway.clients.produto.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ObterProdutoServiceTest {

    private IProdutoGateway produtoGateway;
    private ObterProdutoService obterProdutoService;

    @BeforeEach
    void setUp() {
        produtoGateway = mock(IProdutoGateway.class);
        obterProdutoService = new ObterProdutoService(produtoGateway);
    }

    @Test
    void retornaListaDeProdutosQuandoIdsValidos() {
        Produto produto1 = new Produto("1", "desc", 12.5);
        Produto produto2 = new Produto("2", "desc", 12.5);
        when(produtoGateway.obterDadosProduto("1")).thenReturn(produto1);
        when(produtoGateway.obterDadosProduto("2")).thenReturn(produto2);

        List<Produto> produtos = obterProdutoService.obterDadosProduto(Arrays.asList("1", "2"));

        assertThat(produtos).containsExactly(produto1, produto2);
        verify(produtoGateway, times(1)).obterDadosProduto("1");
        verify(produtoGateway, times(1)).obterDadosProduto("2");
    }

    @Test
    void lancaExcecaoQuandoIdNuloOuVazio() {
        List<String> produtosId = Arrays.asList("1", null, "2");

        Exception exception = assertThrows(IllegalStateException.class, () ->
                obterProdutoService.obterDadosProduto(produtosId)
        );


        assertThat(exception.getMessage()).contains("Produto ID não pode ser nulo ou vazio");
        verify(produtoGateway, times(1)).obterDadosProduto(anyString());
    }

    @Test
    void lancaExcecaoQuandoFalhaAoObterDadosProduto() {
        when(produtoGateway.obterDadosProduto("1")).thenThrow(new RuntimeException("Erro ao buscar produto"));

        Exception exception = assertThrows(IllegalStateException.class, () ->
                obterProdutoService.obterDadosProduto(Collections.singletonList("1"))
        );

        assertThat(exception.getMessage()).contains("Falha ao buscar dados do produto, erro: Erro ao buscar produto");
        verify(produtoGateway, times(1)).obterDadosProduto("1");
    }

    @Test
    void retornaListaVaziaQuandoListaIdsVazia() {
        List<Produto> produtos = obterProdutoService.obterDadosProduto(Collections.emptyList());

        assertThat(produtos).isEmpty();
        verify(produtoGateway, never()).obterDadosProduto(anyString());
    }
}
