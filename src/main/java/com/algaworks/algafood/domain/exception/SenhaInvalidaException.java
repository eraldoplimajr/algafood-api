package com.algaworks.algafood.domain.exception;

public class SenhaInvalidaException extends NegocioException{

    public SenhaInvalidaException() {
        super("Senha atual informada não coincide com a senha do usuário");
    }
}
