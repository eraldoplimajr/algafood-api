package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import com.algaworks.algafood.api.converter.CidadeModelConverter;
import com.algaworks.algafood.api.converter.CidadeModelObjectConverter;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroCidadeService cadastroCidade;

	@Autowired
	private CidadeModelConverter cidadeModelConverter;

	@Autowired
	private CidadeModelObjectConverter cidadeModelObjectConverter;
		
	@GetMapping
	public List<CidadeModel> listar() {
		return cidadeModelConverter.toCollectionModel(cidadeRepository.findAll());
	}
	
	@GetMapping("/{cidadeId}")
	public CidadeModel buscar(@PathVariable Long cidadeId) {
		return cidadeModelConverter.toModel(cadastroCidade.buscarOuFalhar(cidadeId));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
		
		try {
			Cidade cidade = cidadeModelObjectConverter.toDomainObject(cidadeInput);

			 return cidadeModelConverter.toModel(cadastroCidade.salvar(cidade));

		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@PutMapping("/{cidadeId}")
	public Cidade atualizar(@PathVariable Long cidadeId, @RequestBody @Valid CidadeInput cidadeInput) {
		
		try {

			Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);

			cidadeModelObjectConverter.copyToDomainObject(cidadeInput, cidadeAtual);
			
			return cadastroCidade.salvar(cidadeAtual);
			
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}		

	}
	
	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cidadeId) {		
		cadastroCidade.excluir(cidadeId);
	}

}
