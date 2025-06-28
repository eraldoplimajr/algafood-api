package com.algaworks.algafood.domain.exception;

public class SenhaNaoCoincideException extends NegocioException{

    public SenhaNaoCoincideException() {
        super("Senha atual informada não coincide com a senha do usuário");
    }
}
