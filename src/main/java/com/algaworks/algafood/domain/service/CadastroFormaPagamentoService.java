package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CadastroFormaPagamentoService {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<FormaPagamento> listar () {
        return formaPagamentoRepository.findAll();
    }

    @Transactional
    public FormaPagamento salvar(FormaPagamento formaPagamento) {

        return formaPagamentoRepository.save(formaPagamento);

    }

    @Transactional
    public void excluir(Long formaPagamentoId) {

        try{

            formaPagamentoRepository.deleteById(formaPagamentoId);
            formaPagamentoRepository.flush();

        }catch (EmptyResultDataAccessException exception){
            throw new FormaPagamentoNaoEncontradaException(formaPagamentoId);
        }

    }

    public FormaPagamento buscarOuFalhar(Long formaPagamentoId) {
        return formaPagamentoRepository.findById(formaPagamentoId).orElseThrow(() ->
                new FormaPagamentoNaoEncontradaException(formaPagamentoId));
    }


}
