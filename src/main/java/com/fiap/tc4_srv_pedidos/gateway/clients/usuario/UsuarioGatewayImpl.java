package com.fiap.tc4_srv_pedidos.gateway.clients.usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UsuarioGatewayImpl implements IUsuarioGateway {

    private final IUsuarioClient client;

    @Override
    public Usuario obterDadosUsuario(String solicitacaoId) {
        try {
            return this.client.obterDados(solicitacaoId);
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao buscar dados do usuario, erro: " + e.getMessage());
        }
    }
}
