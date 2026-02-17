package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.converter.PedidoConverter;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.domain.service.CadastroPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private CadastroPedidoService cadastroPedido;

    @Autowired
    private PedidoConverter pedidoConverter;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PedidoModel> listar() {
        return pedidoConverter.toCollectionModel(cadastroPedido.listarTodos());
    }

    @GetMapping("/{pedidoId}")
    @ResponseStatus(HttpStatus.OK)
    public PedidoModel buscar(@PathVariable Long pedidoId) {
        return pedidoConverter.toModel(cadastroPedido.buscarOuFalhar(pedidoId));
    }

}
