package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import com.algaworks.algafood.domain.model.Restaurante;

public interface RestauranteRepositoryQueries {
	
	public List<Restaurante> find(String nome, BigDecimal taxaInicial, BigDecimal taxaFinal);
	
	public List<Restaurante> findComFreteGratis(String nome);

}