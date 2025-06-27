package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.api.converter.GrupoModelConverter;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.GrupoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CadastroGrupoService {

    @Autowired
    GrupoRepository grupoRepository;

    @Autowired
    GrupoModelConverter grupoModelConverter;

    public List<Grupo> listarGrupos() {
        return grupoRepository.findAll();
    }

    public Grupo buscarGrupoPorId(Long grupoId) {
        return grupoRepository.findById(grupoId).get();
    }

    public Grupo buscarOuFalhar(Long grupoId) {

        Optional<Grupo> grupo = grupoRepository.findById(grupoId);
        if (grupo.isPresent())
            return grupo.get();

        throw new GrupoNaoEncontradoException("Grupo não encontrado!");
    }

    @Transactional
    public Grupo incluir(GrupoInput grupoInput) {

        Grupo grupo = grupoModelConverter.converterToModel(grupoInput);
        return grupoRepository.save(grupo);
    }

    @Transactional
    public Grupo atualizar(Long grupoId, GrupoInput grupoInput) {

        Grupo grupoAtual = buscarOuFalhar(grupoId);
        grupoModelConverter.convertGrupoInputToModel(grupoInput, grupoAtual);

        return grupoRepository.save(grupoAtual);

    }

    @Transactional
    public void excluir(Long grupoId) {

        Grupo grupo = buscarOuFalhar(grupoId);

        grupoRepository.delete(grupo);
    }
}
