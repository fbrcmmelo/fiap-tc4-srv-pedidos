package com.fiap.tc4_srv_pedidos.controller;

import com.fiap.tc4_srv_pedidos.usecases.IFazerPedidoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoController {

    private final IFazerPedidoUseCase fazerPedidoUseCase;

    public void makeOrder(final FazerPedidoRequest request) {
        try {
            this.fazerPedidoUseCase.fazerPedido(request);
        } catch (Exception e) {
            log.error("Falha ao gerar pedido, erro: {}", e.getMessage());
        }
    }
}
