package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.converter.GrupoModelConverter;
import com.algaworks.algafood.api.converter.GrupoModelObjectConverter;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.service.CadastroGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/grupo")
public class GrupoController {

    @Autowired
    CadastroGrupoService cadastroGrupo;

    @Autowired
    GrupoModelConverter grupoModelConverter;

    @Autowired
    GrupoModelObjectConverter grupoModelObjectConverter;

    @GetMapping
    public List<GrupoModel> listarGrupos() {
        return grupoModelConverter.toCollectionModel(cadastroGrupo.listarGrupos());
    }

    @GetMapping("/{grupoId}")
    public GrupoModel buscarGrupo(@PathVariable Long grupoId) {
        return grupoModelConverter.toModel(cadastroGrupo.buscarOuFalhar(grupoId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoModel adicionar(@RequestBody @Valid GrupoInput grupoInput) {

        Grupo grupo = grupoModelObjectConverter.toDomainObject(grupoInput);

        return grupoModelConverter.toModel(cadastroGrupo.salvar(grupo));
    }

    @PutMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.OK)
    public GrupoModel atualizar(@PathVariable Long grupoId, @RequestBody @Valid GrupoInput grupoInput) {

        Grupo grupoAtual = cadastroGrupo.buscarOuFalhar(grupoId);

        grupoModelObjectConverter.copyToDomainObject(grupoInput, grupoAtual);

        return grupoModelConverter.toModel(cadastroGrupo.salvar(grupoAtual));
    }

    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long grupoId) {
        cadastroGrupo.excluir(grupoId);
    }

}
