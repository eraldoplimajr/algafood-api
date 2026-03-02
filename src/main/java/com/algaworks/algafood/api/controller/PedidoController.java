package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.converter.PedidoConverter;
import com.algaworks.algafood.api.converter.PedidoModelObjectConverter;
import com.algaworks.algafood.api.converter.PedidoResumoModelConverter;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.service.EmissaoPedidoService;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private EmissaoPedidoService cadastroPedido;

    @Autowired
    private PedidoConverter pedidoConverter;

    @Autowired
    private PedidoResumoModelConverter pedidoResumoModelConverter;

    @Autowired
    private PedidoModelObjectConverter pedidoModelObjectConverter;

/*
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public MappingJacksonValue listar(@RequestParam(required = false) String campos) {

        List<Pedido> pedidos = cadastroPedido.listarTodos();
        List<PedidoResumoModel> pedidosModel = pedidoResumoModelConverter.toCollectionModel(pedidos);

        MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosModel);

        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());

        if (StringUtils.isNotBlank(campos)) {
            filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(",")));
        }

        pedidosWrapper.setFilters(filterProvider);

        return pedidosWrapper;
    }
*/

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PedidoResumoModel> listar() {
        return pedidoResumoModelConverter.toCollectionModel(cadastroPedido.listarTodos());
    }

    @GetMapping("/{codigoPedido}")
    @ResponseStatus(HttpStatus.OK)
    public PedidoModel buscar(@PathVariable String codigoPedido) {
        return pedidoConverter.toModel(cadastroPedido.buscarOuFalhar(codigoPedido));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel emitir(@RequestBody @Valid PedidoInput pedidoInput) {

        Pedido pedidoNovo = pedidoModelObjectConverter.toDomainObject(pedidoInput);

        pedidoNovo = cadastroPedido.emitirPedido(pedidoNovo);

        return pedidoConverter.toModel(pedidoNovo);
    }

}
