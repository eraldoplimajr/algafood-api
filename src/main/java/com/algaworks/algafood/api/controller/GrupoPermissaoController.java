package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.converter.PermissaoConverter;
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.domain.service.CadastroGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("grupo/{grupoId}/permissoes")
public class GrupoPermissaoController {

    @Autowired
    CadastroGrupoService cadastroGrupo;

    @Autowired
    PermissaoConverter permissaoConverter;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PermissaoModel> listar(@PathVariable Long grupoId) {
        return permissaoConverter.toCollectionModel(cadastroGrupo.listarPermissoes(grupoId));
    }

    @DeleteMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
        cadastroGrupo.desassociarPermissao(grupoId, permissaoId);
    }

    @PutMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
        cadastroGrupo.associarPermissao(grupoId, permissaoId);
    }

}
