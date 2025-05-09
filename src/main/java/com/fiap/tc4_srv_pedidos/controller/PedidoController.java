package com.fiap.tc4_srv_pedidos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.usecases.IGerarPedidoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoController {

    private final IGerarPedidoUseCase gerarPedidoUseCase;

    public void gerarPedido(String mensagem) {
        try {
            final var pedido = new ObjectMapper().readValue(mensagem, Pedido.class);
            this.gerarPedidoUseCase.gerar(pedido);
        } catch (Exception e) {
            log.error("Falha ao gerar pedido, erro: {}", e.getMessage());
        }
    }
}
