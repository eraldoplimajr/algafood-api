package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.converter.PermissaoConverter;
import com.algaworks.algafood.api.converter.PermissaoObjectConverter;
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.api.model.input.PermissaoInput;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.service.CadastroPermissaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/permissao")
public class PermissaoController {

    @Autowired
    private CadastroPermissaoService cadastroPermissao;

    @Autowired
    private PermissaoConverter permissaoConverter;

    @Autowired
    private PermissaoObjectConverter permissaoObjectConverter;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PermissaoModel> listar() {
        return permissaoConverter.toCollectionModel(cadastroPermissao.listarPermissoes());
    }

    @GetMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.OK)
    public PermissaoModel buscar(@PathVariable Long permissaoId) {
        return permissaoConverter.toModel(cadastroPermissao.buscarOuFalhar(permissaoId));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PermissaoModel incluir(@RequestBody @Valid PermissaoInput permissaoInput) {
        Permissao permissao = permissaoObjectConverter.toDomainObject(permissaoInput);
        return permissaoConverter.toModel(cadastroPermissao.adicionar(permissao));
    }


    @DeleteMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.OK)
    public void excluir(@PathVariable Long permissaoId) {
        cadastroPermissao.remover(permissaoId);
    }
}
