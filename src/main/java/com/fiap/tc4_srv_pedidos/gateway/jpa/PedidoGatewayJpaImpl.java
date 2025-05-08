package com.fiap.tc4_srv_pedidos.gateway.jpa;

import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.gateway.IFazerPedidoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PedidoGatewayJpaImpl implements IFazerPedidoGateway {

    private final PedidoJpaRepository repository;

    @Override
    public void criarPedido(Pedido pedido) {
        this.repository.save(new PedidoEntityJpa(pedido));
    }
}
