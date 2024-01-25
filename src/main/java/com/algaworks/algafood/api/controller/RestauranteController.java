package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.algaworks.algafood.api.converter.RestauranteModelConverter;
import com.algaworks.algafood.api.converter.RestauranteModelObjectConverter;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.CozinhaIdInput;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;

import com.algaworks.algafood.core.validation.ValidacaoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private SmartValidator validator;

	@Autowired
	private RestauranteModelConverter restauranteModelConverter;

	@Autowired
	private RestauranteModelObjectConverter restauranteModelObjectConverter;
	
	@GetMapping
	public List<RestauranteModel> listar() {
		return restauranteModelConverter.toCollectionModel(restauranteRepository.findAll());
	}
	
	@GetMapping("/{restauranteId}")
	public RestauranteModel buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

		return restauranteModelConverter.toModel(restaurante);

	}

	@PostMapping
	public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
		try {

			Restaurante restaurante = restauranteModelObjectConverter.toDomainObject(restauranteInput);

			return restauranteModelConverter.toModel(cadastroRestaurante.salvar(restaurante));
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@PutMapping("/{restauranteId}")
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel atualizar(@PathVariable Long restauranteId, @RequestBody @Valid RestauranteInput restauranteInput) {

		try {
			Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);

			restauranteModelObjectConverter.copyToDomainObject(restauranteInput, restauranteAtual);

			return restauranteModelConverter.toModel(cadastroRestaurante.salvar(restauranteAtual));
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}

	}
	
	@PatchMapping("/{restauranteId}")
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos, HttpServletRequest request) {

		Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);
				
		merge(campos, restauranteAtual, request);
		validate(restauranteAtual, "restaurante");

		RestauranteInput restauranteInput = new RestauranteInput();
		restauranteInput.setNome(restauranteAtual.getNome());
		restauranteInput.setTaxaFrete(restauranteAtual.getTaxaFrete());

		CozinhaIdInput cozinha = new CozinhaIdInput();
		cozinha.setId(restauranteAtual.getCozinha().getId());

		restauranteInput.setCozinha(cozinha);
		
		return atualizar(restauranteId, restauranteInput);
	}
	

	@DeleteMapping("/{restauranteId}")
	public ResponseEntity<?> excluir(@PathVariable Long restauranteId) {
		
		try {
			
			cadastroRestaurante.excluir(restauranteId);		
			
			return ResponseEntity.noContent().build();
			
		}catch(EntidadeNaoEncontradaException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId){
		cadastroRestaurante.ativar(restauranteId);
	}

	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId){
		cadastroRestaurante.inativar(restauranteId);
	}

	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
		
		ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(request);
		
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			
			Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
			
			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);
				
				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
				
				ReflectionUtils.setField(field, restauranteDestino, novoValor);	
			});
			
		} catch(IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, servletServerHttpRequest);
		}
		
	}

	private void validate(Restaurante restaurante, String objectName) {
		
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);		
		validator.validate(restaurante, bindingResult);
		
		if(bindingResult.hasErrors()) {
			throw new ValidacaoException(bindingResult);
		}
		
	}

}
