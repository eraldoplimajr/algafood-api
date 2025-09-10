package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.ProdutoRespository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CadastroProdutoService {

    @Autowired
    ProdutoRespository produtoRespository;

    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    CadastroRestauranteService cadastroRestauranteService;

    public Produto buscar(Long restauranteId, Long produtoId) {

        Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
        Optional<Produto> optionalProduto = restaurante.buscar(produtoId);

        return optionalProduto.orElseThrow(() -> new  ProdutoNaoEncontradoException(produtoId));
    }

    @Transactional
    public Produto incluir(Restaurante restaurante, Produto produto) {
        restaurante.adicionarProduto(produto);
        produto.setRestaurante(restaurante);
        return produtoRespository.save(produto);
    }
}
