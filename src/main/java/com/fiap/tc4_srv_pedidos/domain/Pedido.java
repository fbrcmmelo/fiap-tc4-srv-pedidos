package com.fiap.tc4_srv_pedidos.domain;

import com.fiap.tc4_srv_pedidos.gateway.jpa.PedidoEntityJpa;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class Pedido {

    private String id;
    private String clienteId;
    private DadosCartaoCliente dadosCartao;
    private List<ProdutoPedido> produtoPedidos;
    private StatusPedidoEnum status;
    private String transacaoId;
    private String descricaoStatus;
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

    public Pedido(PedidoEntityJpa entityJpa) {
        this.id = entityJpa.getId();
        this.clienteId = entityJpa.getClienteId();
        this.produtoPedidos = entityJpa.getProdutoPedidos();
        this.status = entityJpa.getStatus();
        this.transacaoId = entityJpa.getTransacaoId();
        this.criadoEm = entityJpa.getCriadoEm();
        this.atualizadoEm = entityJpa.getAtualizadoEm();
        this.deletadoEm = entityJpa.getDeletadoEm();
        this.descricaoStatus = entityJpa.getDescricaoErro();
    }

    public void atualizarStatus(StatusPedidoEnum status) {
        Objects.requireNonNull(status, "Status do pedido não pode ser nulo");
        this.status = status;
        this.atualizadoEm = Instant.now();
    }

    public void registrarTransacaoId(String solicitacaoId) {

        if (this.status != null && this.status != StatusPedidoEnum.ABERTO) {
            throw new IllegalArgumentException("Status do pedido não pode ser diferente de ABERTO");
        }

        Objects.requireNonNull(solicitacaoId, "Solicitação não pode ser nula");

        if (this.transacaoId != null) {
            throw new IllegalArgumentException("Transação já foi setada");
        }

        this.transacaoId = solicitacaoId;
        this.atualizadoEm = Instant.now();
    }

    public void setDescricaoStatus(String descricaoStatus) {
        this.descricaoStatus = descricaoStatus;
    }
}
