package com.fiap.tc4_srv_pedidos.gateway.jpa;

import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.domain.ProdutoPedido;
import com.fiap.tc4_srv_pedidos.domain.StatusPedidoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@Document("pedidos")
public class PedidoEntityJpa {

    @Id
    private String id;
    private String clienteId;
    private List<ProdutoPedido> produtoPedidos;
    private StatusPedidoEnum status;
    private String transacaoId;
    private Instant criadoEm;
    private Instant atualizadoEm;
    private Instant deletadoEm;
    private String descricaoErro;

    public PedidoEntityJpa(Pedido pedido) {
        this.id = pedido.getId();
        this.clienteId = pedido.getClienteId();
        this.produtoPedidos = pedido.getProdutoPedidos();
        this.status = pedido.getStatus();
        this.transacaoId = pedido.getTransacaoId();
        this.criadoEm = pedido.getCriadoEm();
        this.atualizadoEm = pedido.getAtualizadoEm();
        this.deletadoEm = pedido.getDeletadoEm();
        this.descricaoErro = pedido.getDescricaoStatus();
    }

    public Pedido toPedido() {
        return new Pedido(this);
    }

}
