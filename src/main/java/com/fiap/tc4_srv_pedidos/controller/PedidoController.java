package com.fiap.tc4_srv_pedidos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.usecases.IAtualizarPedidoUseCase;
import com.fiap.tc4_srv_pedidos.usecases.IGerarPedidoUseCase;
import com.fiap.tc4_srv_pedidos.usecases.IListarPedidosUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidoController {

    private final IListarPedidosUseCase listarUseCase;

    public List<PedidoDTO> listarPedidos() {
        return this.listarUseCase.listarPedidos()
                .stream()
                .map(PedidoDTO::from)
                .toList();
    }
}
