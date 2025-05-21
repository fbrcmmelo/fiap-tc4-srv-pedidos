package com.fiap.tc4_srv_pedidos.controller;

import com.fiap.tc4_srv_pedidos.usecases.IListarPedidosUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidoController {

    private final IListarPedidosUseCase listarUseCase;

    @GetMapping
    public List<PedidoDTO> listarPedidos() {
        return this.listarUseCase.listarPedidos()
                .stream()
                .map(PedidoDTO::from)
                .toList();
    }
}
