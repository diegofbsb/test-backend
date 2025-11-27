package com.test.backend.exception;

public class ClienteNaoEncontradoException extends RuntimeException{

    public ClienteNaoEncontradoException(Integer id) {
        super("Cliente não encontrado: " + id);
    }
}
