package com.algaworks.algafood.domain.model;

import com.algaworks.algafood.core.validation.ValorZeroIncluiDescricao;
import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@ValorZeroIncluiDescricao(valorField = "taxaFrete", descricaoField = "nome",
		descricaoObrigatoria = "Frete Grátis")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	@Column(name = "taxa_frete", nullable = false)
	private BigDecimal taxaFrete;

	@ManyToOne//(fetch = FetchType.LAZY)
	@JoinColumn(name = "cozinha_id", nullable = false)
	private Cozinha cozinha;

	@Embedded
	private Endereco endereco;

	private Boolean ativo = Boolean.TRUE;

	private Boolean aberto = Boolean.FALSE;

	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataCadastro;

	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataAtualizacao;

	@ManyToMany
	@JoinTable(name = "restaurante_forma_pagamento", 
			joinColumns = @JoinColumn(name = "restaurante_id"), 
			inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
	private Set<FormaPagamento> formasPagamento = new HashSet<>();

	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "restaurante_usuario_responsavel",
			joinColumns = @JoinColumn(name = "restaurante_id"),
			inverseJoinColumns = @JoinColumn(name = "usuario_id"))
	private Set<Usuario> responsaveis = new HashSet<>();

	public void ativar(){
		setAtivo(true);
	}

	public void inativar(){
		setAtivo(false);
	}

	private void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public void fechar() {
		setAberto(false);
	}

	public void abrir()  {
		setAberto(true);
	}

	public boolean removerFormaPagamento(FormaPagamento formaPagamento) {
		return formasPagamento.remove(formaPagamento);
	}

	public boolean adicionarFormaPagamento(FormaPagamento formaPagamento) {
		return formasPagamento.add(formaPagamento);
	}

	public Optional<Produto> buscar(Long produtoId) {
		return produtos.stream()
				.filter(prod -> prod.getId().equals(produtoId)).findFirst();
	}

	public void adicionarProduto(Produto produto) {
		produtos.add(produto);
	}

	public void removerProduto(Produto produto) {
		produtos.remove(produto);
	}

}
