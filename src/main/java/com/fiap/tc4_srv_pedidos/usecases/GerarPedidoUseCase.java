package com.fiap.tc4_srv_pedidos.usecases;

import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.domain.ProdutoPedido;
import com.fiap.tc4_srv_pedidos.domain.services.ObterProdutoService;
import com.fiap.tc4_srv_pedidos.gateway.IPedidoGateway;
import com.fiap.tc4_srv_pedidos.gateway.clients.estoque.IEstoqueGateway;
import com.fiap.tc4_srv_pedidos.gateway.clients.pagamento.IPagamentoGateway;
import com.fiap.tc4_srv_pedidos.gateway.clients.pagamento.SolicitacaoPagamentoIn;
import com.fiap.tc4_srv_pedidos.gateway.clients.produto.Produto;
import com.fiap.tc4_srv_pedidos.gateway.clients.usuario.IUsuarioGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GerarPedidoUseCase implements IGerarPedidoUseCase {

    private final IPedidoGateway gateway;
    private final IUsuarioGateway usuarioGateway;
    private final IEstoqueGateway estoqueGateway;
    private final IPagamentoGateway pagamentoGateway;
    private final ObterProdutoService obterProdutoService;

    /**
     * Método responsável por gerar um pedido.
     *
     * @param requisicao Dados do pedido a ser gerado.
     */
    @Override
    public void gerar(Pedido requisicao) {
        final var pedido = new Pedido(
                requisicao.getClienteId(), requisicao.getDadosCartao(), requisicao.getProdutoPedidos()
        );

        final var usuario = this.usuarioGateway.obterDadosUsuario(requisicao.getClienteId());

        final var produtos = this.obterProdutoService.obterDadosProduto(
                requisicao.getProdutoPedidos().stream().map(ProdutoPedido::sku).toList()
        );

        for (final var produto : requisicao.getProdutoPedidos()) {
            this.estoqueGateway.baixarEstoque(produto.sku(), produto.quantidade().intValue());
        }

        final var valorTotal = produtos.stream().map(Produto::preco).reduce(0.0, Double::sum);

        final var solicitacao = this.pagamentoGateway.solicitar(
                new SolicitacaoPagamentoIn(String.valueOf(valorTotal), usuario.dadosCartaoCliente().numero())
        );

        pedido.atualizarStatus(solicitacao.status());
        pedido.setTransacaoId(solicitacao.solicitacaoId());

        this.gateway.salvarPedido(pedido);
    }
}
