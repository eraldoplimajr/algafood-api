package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.converter.UsuarioModelConverter;
import com.algaworks.algafood.api.converter.UsuarioModelObjectConverter;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.api.model.input.UsuarioInput;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    CadastroUsuarioService cadastroUsuario;

    @Autowired
    UsuarioModelConverter usuarioModelConverter;

    @Autowired
    UsuarioModelObjectConverter usuarioModelObjectConverter;

    @GetMapping
    public List<UsuarioModel> listar() {
        return usuarioModelConverter.toCollectionModel(cadastroUsuario.listar());
    }

    @GetMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioModel buscar(@PathVariable Long usuarioId) {
        return usuarioModelConverter.toModel(cadastroUsuario.buscarOuFalhar(usuarioId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioModel adicionar(@RequestBody @Valid UsuarioInput usuarioInput) {
        Usuario usuario = usuarioModelObjectConverter.toDomainObject(usuarioInput);

        return usuarioModelConverter.toModel(cadastroUsuario.salvar(usuario));
    }

    @PutMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioModel atualizar(@PathVariable Long usuarioId, @RequestBody @Valid UsuarioInput usuarioInput) {

        Usuario usuarioAtual = cadastroUsuario.buscarOuFalhar(usuarioId);

        usuarioModelObjectConverter.copyToDomainObject(usuarioInput, usuarioAtual);

        return usuarioModelConverter.toModel(cadastroUsuario.salvar(usuarioAtual));
    }

    @DeleteMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long usuarioId) {
        cadastroUsuario.excluir(usuarioId);
    }

}
