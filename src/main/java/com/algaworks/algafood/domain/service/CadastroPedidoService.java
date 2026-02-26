package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.PedidoNaoEncontradoException;
import com.algaworks.algafood.domain.model.*;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CadastroPedidoService {

    private static final Logger log = LoggerFactory.getLogger(CadastroPedidoService.class);
    private final String MSG_PEDIDO_EM_USO = "O pedido de código %d não pode ser excluído pois está em uso";

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestaurante;

    @Autowired
    private CadastroUsuarioService cadastroUsuario;

    @Autowired
    private CadastroCidadeService cadastroCidade;

    @Autowired
    private CadastroProdutoService cadastroProduto;

    @Autowired
    private CadastroFormaPagamentoService cadastroFormaPagamento;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Transactional
    public Pedido emitirPedido(Pedido pedido) {

        validarDadosPedido(pedido);
        validarItensPedido(pedido);

        FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(pedido.getFormaPagamento().getId());
        if(!pedido.getRestaurante().getFormasPagamento().contains(formaPagamento)) {
            throw new NegocioException(String.format("A forma de pagamento %s não é aceita pelo restaurante de código %d",
                    formaPagamento.getDescricao(), pedido.getRestaurante().getId()));
        }

        return pedidoRepository.save(pedido);
    }

    public Pedido buscarOuFalhar(Long pedidoId) {
        return pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
    }

    private Pedido validarDadosPedido(Pedido pedido) {

        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(pedido.getRestaurante().getId());
        Usuario usuario = cadastroUsuario.buscarOuFalhar(pedido.getCliente().getId());
        Cidade cidade = cadastroCidade.buscarOuFalhar(pedido.getEnderecoEntrega().getCidade().getId());

        pedido.setRestaurante(restaurante);
        pedido.setCliente(usuario);
        pedido.getEnderecoEntrega().setCidade(cidade);
        pedido.setTaxaFrete(restaurante.getTaxaFrete());

        return pedido;
    }

    private void validarItensPedido(Pedido pedido) {

        BigDecimal totalPedido = BigDecimal.ZERO;
        for(ItemPedido itemPedido : pedido.getItens()) {
            Produto produto = cadastroProduto.buscarOuFalhar(itemPedido.getProduto().getId(), pedido.getRestaurante().getId());

            BigDecimal precoProduto = produto.getPreco();
            log.info(String.format("Preço do produto %s = %.2f", produto.getNome(), produto.getPreco()));
            Integer quantidade = itemPedido.getQuantidade();
            log.info("A quantidade desse produto é: " + quantidade);
            totalPedido = totalPedido.add(precoProduto.multiply(new BigDecimal(quantidade)));
            log.info(String.format("O valor total do pedido está sendo %.2f", totalPedido));

        }
        /*
        pedido.getItens().forEach(i -> {
            Produto produto = cadastroProduto.buscarOuFalhar(i.getProduto().getId(), pedido.getRestaurante().getId());
            pedido.setValorTotal(pedido.getValorTotal().add(produto.getPreco().multiply(new BigDecimal(i.getQuantidade()))));
        } );
        */
        pedido.setSubtotal(totalPedido);
        log.info(String.format("O subtotaldo pedido é: %.2f", pedido.getSubtotal()));
        log.info(String.format("O valor da taxa frete do restaurante é: %.2f", pedido.getRestaurante().getTaxaFrete()));
        totalPedido = pedido.getRestaurante().getTaxaFrete().add(totalPedido);
        log.info(String.format("O valor total do pedido é: %.2f", totalPedido));
        pedido.setValorTotal(totalPedido);
        pedido.setValorTotal(totalPedido);
        log.info(String.format("O valor FINAL do pedido é: %.2f", pedido.getValorTotal()));


    }


}
