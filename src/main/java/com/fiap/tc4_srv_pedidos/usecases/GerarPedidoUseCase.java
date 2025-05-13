package com.fiap.tc4_srv_pedidos.usecases;

import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.gateway.IPedidoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GerarPedidoUseCase implements IGerarPedidoUseCase {

    private final IPedidoGateway gateway;

    @Override
    public void gerar(Pedido requisicao) {
        final var pedido =
                new Pedido(requisicao.getClienteId(), requisicao.getDadosCartao(), requisicao.getProdutoPedidos());
        // consumir servico buscar dados produto sobre lista de produtosPedidos

        // consumir servico buscar dados cliente sobre clienteId

        // consumir servico baixar estoque produtos

        // consumir servico gerar transacao

        this.gateway.salvarPedido(pedido);
    }
}
