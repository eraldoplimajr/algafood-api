package com.algaworks.algafood;

import static org.junit.jupiter.api.Assertions.*;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;

@SpringBootTest
public class CadastroCozinhaIntegrationTests {

    @Autowired
    private CadastroCozinhaService cadastroCozinha;

    @Test
    public void deveAtribuirId_QuandoCadastrarCozinhaComDadosCorretos() {

        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome("Chinesa");

        novaCozinha = cadastroCozinha.salvar(novaCozinha);

        assertNotNull(novaCozinha);
        assertNotNull(novaCozinha.getId());

    }

    @Test
    public void deveFalhar_QuandoCadastrarCozinhaSemNome() {

        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome(null);

        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            cadastroCozinha.salvar(novaCozinha);
        });

    }

    @Test
    public void deveFalhar_QuandoExcluirCozinhaEmUso() {

        Cozinha novaCozinha = new Cozinha();

        Exception exception = assertThrows(CozinhaNaoEncontradaException.class, () -> {
            cadastroCozinha.excluir(100L);
        });

    }

    @Test
    public void deveFalhar_QuandoExcluirCozinhaInexistente() {

        Cozinha novaCozinha = new Cozinha();

        Exception exception = assertThrows(EntidadeEmUsoException.class, () -> {
            cadastroCozinha.excluir(1L);
        });

    }

}
