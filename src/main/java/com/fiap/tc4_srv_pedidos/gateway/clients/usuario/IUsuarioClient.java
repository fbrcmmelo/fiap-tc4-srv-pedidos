package com.fiap.tc4_srv_pedidos.gateway.clients.usuario;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "clientes", url = "http://localhost:8085")
public interface IUsuarioClient {

    @GetMapping("/api/clientes/{usuarioId}")
    Usuario obterDados(@PathVariable String usuarioId);
}
