package com.fiap.tc4_srv_pedidos.gateway.jpa;

import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.domain.ProdutoPedido;
import com.fiap.tc4_srv_pedidos.domain.StatusPedidoEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Document("pedidos")
public class PedidoEntityJpa {

    @Id
    private String id;
    private String clienteId;
    private List<ProdutoPedido> produtoPedidos;
    private StatusPedidoEnum status;
    private UUID transacaoId;
    private Instant criadoEm;
    private Instant atualizadoEm;
    private Instant deletadoEm;

    public PedidoEntityJpa(Pedido pedido) {
        this.id = pedido.getId();
        this.clienteId = pedido.getClienteId();
        this.produtoPedidos = pedido.getProdutoPedidos();
        this.status = pedido.getStatus();
        this.transacaoId = pedido.getTransacaoId();
        this.criadoEm = pedido.getCriadoEm();
        this.atualizadoEm = pedido.getAtualizadoEm();
        this.deletadoEm = pedido.getDeletadoEm();
    }
}
