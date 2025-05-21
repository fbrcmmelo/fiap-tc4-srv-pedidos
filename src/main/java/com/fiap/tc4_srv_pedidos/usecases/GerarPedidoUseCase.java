package com.fiap.tc4_srv_pedidos.usecases;

import com.fiap.tc4_srv_pedidos.domain.Pedido;
import com.fiap.tc4_srv_pedidos.domain.ProdutoPedido;
import com.fiap.tc4_srv_pedidos.domain.StatusPedidoEnum;
import com.fiap.tc4_srv_pedidos.domain.services.ObterProdutoService;
import com.fiap.tc4_srv_pedidos.gateway.IPedidoGateway;
import com.fiap.tc4_srv_pedidos.gateway.clients.estoque.IEstoqueGateway;
import com.fiap.tc4_srv_pedidos.gateway.clients.pagamento.IPagamentoGateway;
import com.fiap.tc4_srv_pedidos.gateway.clients.pagamento.SolicitacaoPagamentoIn;
import com.fiap.tc4_srv_pedidos.gateway.clients.produto.Produto;
import com.fiap.tc4_srv_pedidos.gateway.clients.usuario.IUsuarioGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
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
        final var usuario = this.usuarioGateway.obterDadosUsuario(requisicao.getClienteId());

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        final var produtos = this.obterProdutoService.obterDadosProduto(
                requisicao.getProdutoPedidos().stream().map(ProdutoPedido::sku).toList()
        );

        final var pedido = new Pedido(
                requisicao.getClienteId(), requisicao.getDadosCartao(), requisicao.getProdutoPedidos()
        );

        try {

            for (final var produto : requisicao.getProdutoPedidos()) {
                this.estoqueGateway.baixarEstoque(produto.sku(), produto.quantidade().intValue());
            }

            final var valorTotal = produtos.stream().map(Produto::preco).reduce(0.0, Double::sum);

            final var solicitacao = this.pagamentoGateway.solicitar(
                    new SolicitacaoPagamentoIn(String.valueOf(valorTotal), pedido.getDadosCartao().numero())
            );

            pedido.atualizarStatus(solicitacao.status());
            pedido.registrarTransacaoId(solicitacao.solicitacaoId());
        } catch (SemEstoqueException ex) {
            log.error("Produto sem estoque, erro: {}", ex.getMessage());
            pedido.atualizarStatus(StatusPedidoEnum.SEM_ESTOQUE);
            pedido.setDescricaoStatus("Produto sem estoque, erro: " + ex.getMessage());
        } catch (IllegalStateException ex) {
            log.error("Erro ao gerar pedido, erro: {}", ex.getMessage());
            pedido.atualizarStatus(StatusPedidoEnum.FECHADO_SEM_SUCESSO);
            pedido.setDescricaoStatus("Erro ao gerar pedido, erro: " + ex.getMessage());
        }  catch (Exception ex) {
            log.error("Falha no caso de uso gerar pedido, erro: {}", ex.getMessage());
            pedido.atualizarStatus(StatusPedidoEnum.FECHADO_SEM_SUCESSO);
            pedido.setDescricaoStatus("Descricao do erro: " + ex.getMessage());
        } finally {
            this.gateway.salvarPedido(pedido);
        }
    }
}
