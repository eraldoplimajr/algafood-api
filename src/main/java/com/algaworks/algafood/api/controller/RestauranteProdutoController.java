package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.converter.ProdutoModelConverter;
import com.algaworks.algafood.api.converter.ProdutoObjectConverter;
import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.api.model.input.ProdutoInput;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos")
public class RestauranteProdutoController {

    @Autowired
    CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    CadastroProdutoService cadastroProdutoService;

    @Autowired
    ProdutoModelConverter produtoModelConverter;

    @Autowired
    ProdutoObjectConverter produtoObjectConverter;

    @GetMapping
    public List<ProdutoModel> listar(@PathVariable Long restauranteId) {
        Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
        return produtoModelConverter.toCollectionModel(restaurante.getProdutos());
    }

    @GetMapping("/{produtoId}")
    public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        Produto produto = cadastroProdutoService.buscar(restauranteId, produtoId);
        return produtoModelConverter.toModel(produto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoModel incluir(@PathVariable Long restauranteId, @RequestBody @Valid ProdutoInput produtoInput) {

        Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);

        Produto produto = produtoObjectConverter.toDomainObject(produtoInput);
        produto.setRestaurante(restaurante);
        produto = cadastroProdutoService.incluir(produto);

        return produtoModelConverter.toModel(produto);
    }

    @PutMapping("/{produtoId}")
    public ProdutoModel alterar(@PathVariable Long restauranteId, @PathVariable Long produtoId, @RequestBody @Valid ProdutoInput produtoInput) {

        Produto produtoAtual = cadastroProdutoService.buscarOuFalhar(produtoId, restauranteId);

        produtoObjectConverter.copyToDomainObject(produtoInput, produtoAtual);

        Produto produto = cadastroProdutoService.incluir(produtoAtual);

        return produtoModelConverter.toModel(produto);

    }

}
