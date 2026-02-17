package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.converter.FormaPagamentoConverter;
import com.algaworks.algafood.api.converter.FormaPagamentoObjectConverter;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.input.FormaPagamentoInput;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.service.CadastroFormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/formas-pagamentos")
public class FormaPagamentoController {

    @Autowired
    private CadastroFormaPagamentoService formaPagamentoService;

    @Autowired
    private FormaPagamentoConverter formaPagamentoConverter;

    @Autowired
    private FormaPagamentoObjectConverter formaPagamentoObjectConverter;

    @GetMapping
    public List<FormaPagamentoModel> listar() {
        return formaPagamentoConverter.toCollectionModel(formaPagamentoService.listar());
    }

    @GetMapping("/{formaPagamentoId}")
    public FormaPagamentoModel buscar(@PathVariable Long formaPagamentoId) {
        return formaPagamentoConverter.toModel(formaPagamentoService.buscarOuFalhar(formaPagamentoId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoModel adicionar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {

        FormaPagamento formaPagamento = formaPagamentoObjectConverter.toDomainObject(formaPagamentoInput);

        return formaPagamentoConverter.toModel(formaPagamentoService.salvar(formaPagamento));

    }

    @PutMapping("/{formaPagamentoId}")
    public FormaPagamentoModel atualizar(@PathVariable Long formaPagamentoId, @RequestBody @Valid  FormaPagamentoInput formaPagamentoInput) {

        FormaPagamento formaPagamento = formaPagamentoService.buscarOuFalhar(formaPagamentoId);

        formaPagamentoObjectConverter.copyToDomainObject(formaPagamentoInput, formaPagamento);

        return formaPagamentoConverter.toModel(formaPagamentoService.salvar(formaPagamento));

    }

    @DeleteMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long formaPagamentoId) {
        formaPagamentoService.excluir(formaPagamentoId);
    }
}
