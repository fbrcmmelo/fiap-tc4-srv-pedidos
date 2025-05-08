package com.fiap.tc4_srv_pedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class Tc4SrvPedidosApplication {

    public static void main(String[] args) {
        SpringApplication.run(Tc4SrvPedidosApplication.class, args);
    }

}
