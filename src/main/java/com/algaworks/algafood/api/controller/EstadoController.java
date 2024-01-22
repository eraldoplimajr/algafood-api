package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import com.algaworks.algafood.api.converter.EstadoModelConverter;
import com.algaworks.algafood.api.converter.EstadoModelObjectConverter;
import com.algaworks.algafood.api.model.EstadoModel;
import com.algaworks.algafood.api.model.input.EstadoInput;
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

import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoController {
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CadastroEstadoService cadastroEstado;

	@Autowired
	private EstadoModelConverter estadoModelConverter;

	@Autowired
	private EstadoModelObjectConverter estadoModelObjectConverter;
	
	@GetMapping
	public List<EstadoModel> listar(){
		return estadoModelConverter.toCollectionModel(estadoRepository.findAll());
	}
	
	@GetMapping("/{estadoId}")
	public EstadoModel buscar(@PathVariable Long estadoId) {
		Estado estado = cadastroEstado.buscarOuFalhar(estadoId);

		return estadoModelConverter.toModel(estado);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Estado adicionar(@RequestBody @Valid EstadoInput estadoInput) {
		Estado estado = estadoModelObjectConverter.toDomainObject(estadoInput);

		return cadastroEstado.salvar(estado);
	}
	
	@PutMapping("/{estadoId}")
	public Estado atualizar(@PathVariable Long estadoId, @RequestBody @Valid EstadoInput estadoInput) {

		Estado estadoAtual = cadastroEstado.buscarOuFalhar(estadoId);

		estadoModelObjectConverter.copyToDomainObject(estadoInput, estadoAtual);

		return cadastroEstado.salvar(estadoAtual);
		
	}
	
	@DeleteMapping("/{estadoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long estadoId) {
		cadastroEstado.remover(estadoId);			
	}

}
