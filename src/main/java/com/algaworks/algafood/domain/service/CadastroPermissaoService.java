package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.PermissaoNaoEncontradaException;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CadastroPermissaoService {

    @Autowired
    private PermissaoRepository permissaoRepository;

    public List<Permissao> listarPermissoes() {
        return permissaoRepository.findAll();
    }

    public Permissao buscarOuFalhar(Long permissaoId) {
        return permissaoRepository.findById(permissaoId).orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
    }

    public Permissao adicionar(Permissao permissao) {
        return permissaoRepository.save(permissao);
    }

    public void remover(Long permissaoId) {
        Permissao permissao = buscarOuFalhar(permissaoId);
        permissaoRepository.delete(permissao);
    }

}
