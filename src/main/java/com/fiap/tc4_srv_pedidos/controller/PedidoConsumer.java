package com.fiap.tc4_srv_pedidos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.usecases.IAtualizarPedidoUseCase;
import com.fiap.tc4_srv_pedidos.usecases.IGerarPedidoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoConsumer {

    private final IGerarPedidoUseCase gerarPedidoUseCase;
    private final IAtualizarPedidoUseCase atualizarPedidoUseCase;

    public void gerarPedido(String mensagem) {
        try {
            final var pedido = new ObjectMapper().readValue(mensagem, Pedido.class);
            this.gerarPedidoUseCase.gerar(pedido);
        } catch (Exception e) {
            log.error("Falha ao gerar pedido, erro: {}", e.getMessage());
        }
    }

    @Bean("consumer/solicitacao-atualizada")
    public Consumer<SolicitacaoPagamentoOut> solicitacaoAtualizada() {
        return solicitacao -> {
            try {
                this.atualizarPedidoUseCase.atualizar(solicitacao.solicitacaoId());
            } catch (Exception e) {
                log.error("Erro ao processar notificação: {}", e.getMessage());
            }
        };
    }
}
