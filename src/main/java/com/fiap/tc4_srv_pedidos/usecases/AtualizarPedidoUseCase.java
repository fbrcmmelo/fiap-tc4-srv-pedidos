package com.fiap.tc4_srv_pedidos.usecases;

import com.fiap.tc4_srv_pedidos.domain.StatusPedidoEnum;
import com.fiap.tc4_srv_pedidos.gateway.IPedidoGateway;
import com.fiap.tc4_srv_pedidos.gateway.clients.estoque.IEstoqueGateway;
import com.fiap.tc4_srv_pedidos.gateway.clients.pagamento.IPagamentoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AtualizarPedidoUseCase implements IAtualizarPedidoUseCase {

    private final IPedidoGateway pedidoGateway;
    private final IPagamentoGateway pagamentoGateway;
    private final IEstoqueGateway estoqueGateway;

    @Override
    public void atualizar(String solicitacaoId) {
        var pedido = this.pedidoGateway.buscarPedidoPorSolicitacaoPagamentoId(solicitacaoId);
        final var solicitacao = this.pagamentoGateway.buscarSolicitacaoPagamento(solicitacaoId);

        pedido.atualizarStatus(solicitacao.statusPagamento());

        if (Arrays.asList(
                StatusPedidoEnum.FECHADO_SEM_SUCESSO,
                StatusPedidoEnum.FECHADO_SEM_CREDITO).contains(solicitacao.statusPagamento())) {

            // revertendo quantidade estoque para pagamentos invalidos
            try {
                for (var produto : pedido.getProdutoPedidos()) {
                    this.estoqueGateway.incrementarEstoque(produto.sku(), produto.quantidade().intValue());
                }
            } catch (Exception e) {
                throw new IllegalCallerException("Falha ao tentar rever estoque produtos, apos pagamento invalido");
            }
        }

        this.pedidoGateway.salvarPedido(pedido);
    }
}

