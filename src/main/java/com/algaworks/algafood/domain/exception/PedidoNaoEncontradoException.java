package com.algaworks.algafood.domain.exception;

public class PedidoNaoEncontradoException extends NegocioException{

    public PedidoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public PedidoNaoEncontradoException(Long pedidoId){
        this(String.format("Não existe cadastro de pedido com o número %s", pedidoId));
    }

}
