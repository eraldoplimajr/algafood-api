package com.algaworks.algafood.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.repository.CozinhaRepository;

@RestController
@RequestMapping("/teste")
public class TesteController {
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	/*
	 * @GetMapping("cozinhas/porNome") public List<Cozinha>
	 * buscarPorNome(@RequestParam("nome") String nome) { return
	 * cozinhaRepository.consultarPorNome(nome);
	 * 
	 * }
	 */ 
}
