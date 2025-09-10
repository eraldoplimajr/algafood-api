package com.algaworks.algafood.api.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoModel {

    private Long produtoId;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Boolean ativo;

}
