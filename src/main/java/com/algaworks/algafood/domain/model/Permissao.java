package com.algaworks.algafood.domain.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
public class Permissao {
	
	@EqualsAndHashCode.Include
	@Id
	private Long id;
	
	private String nome;
	
	private String descricao;

}
