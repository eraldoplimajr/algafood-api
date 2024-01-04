package com.algaworks.algafood;

import static org.junit.jupiter.api.Assertions.*;

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

        // cenário
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome("Chinesa");

        // ação
        novaCozinha = cadastroCozinha.salvar(novaCozinha);

        // validação
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

}
