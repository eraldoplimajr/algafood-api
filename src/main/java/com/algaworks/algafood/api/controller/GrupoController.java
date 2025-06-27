package com.algaworks.algafood.api.controller;

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

    @GetMapping
    public List<Grupo> listarGrupos() {
        return cadastroGrupo.listarGrupos();
    }

    @GetMapping("/{grupoId}")
    public ResponseEntity<Grupo> buscarGrupo(@PathVariable Long grupoId) {
        Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);
        return ResponseEntity.ok(grupo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Grupo incluirGrupo(@RequestBody @Valid GrupoInput grupoInput) {
        return cadastroGrupo.incluir(grupoInput);
    }

    @PutMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Grupo> atualizar(@PathVariable Long grupoId, @RequestBody @Valid GrupoInput grupoInput) {
        return ResponseEntity.ok(cadastroGrupo.atualizar(grupoId, grupoInput));
    }

    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirGrupo(@PathVariable Long grupoId) {
        cadastroGrupo.excluir(grupoId);
    }

}
