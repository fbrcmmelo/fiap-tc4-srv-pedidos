package com.fiap.tc4_srv_pedidos.usecases;

public class SemEstoqueException extends RuntimeException {

    public SemEstoqueException(String message) {
        super(message);
    }

}
