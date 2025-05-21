package com.fiap.tc4_srv_pedidos.gateway.jpa;

import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.gateway.IPedidoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PedidoGatewauJpaImpl implements IPedidoGateway {

    private final PedidoJpaRepository repository;

    @Override
    public void salvarPedido(Pedido pedido) {
        this.repository.save(new PedidoEntityJpa(pedido));
    }

    @Override
    public Pedido buscarPedidoPorSolicitacaoPagamentoId(String solicitacaoId) {
        final var pedidoEntityJpa = new PedidoEntityJpa();
        pedidoEntityJpa.setTransacaoId(solicitacaoId);

        return this.repository.findOne(Example.of(pedidoEntityJpa))
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"))
                .toPedido();
    }

    @Override
    public List<Pedido> listarPedidos() {
        return this.repository.findAll()
                .stream()
                .map(PedidoEntityJpa::toPedido)
                .toList();
    }
}
