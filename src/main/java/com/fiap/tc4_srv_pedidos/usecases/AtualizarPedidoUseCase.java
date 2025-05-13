package com.fiap.tc4_srv_pedidos.usecases;

import com.fiap.tc4_srv_pedidos.domain.StatusPedidoEnum;
import com.fiap.tc4_srv_pedidos.gateway.IEstoqueGateway;
import com.fiap.tc4_srv_pedidos.gateway.IPagamentoGateway;
import com.fiap.tc4_srv_pedidos.gateway.IPedidoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

        pedido.atualizarStatus(solicitacao.status());

        if (StatusPedidoEnum.FECHADO_SEM_SUCESSO.equals(solicitacao.status()) ||
                StatusPedidoEnum.SEM_ESTOQUE.equals(solicitacao.status())) {

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
