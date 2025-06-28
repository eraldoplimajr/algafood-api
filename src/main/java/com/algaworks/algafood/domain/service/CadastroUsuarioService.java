package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.SenhaNaoCoincideException;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CadastroUsuarioService {

    private static final String MSG_USUARIO_EM_USO = "Usuário de código %d não pode ser removido, pois está em uso";

    @Autowired
    UsuarioRepository usuarioRepository;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void excluir(Long usuarioId) {

        try{
            usuarioRepository.deleteById(usuarioId);
            usuarioRepository.flush();
        }catch(EmptyResultDataAccessException e) {
            throw new UsuarioNaoEncontradoException(usuarioId);
        }catch(DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format(MSG_USUARIO_EM_USO, usuarioId));
        }
    }

    public Usuario buscarOuFalhar(Long usuarioId) {
        return usuarioRepository.findById(usuarioId).orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
    }

    @Transactional
    public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {

        Usuario usuario = buscarOuFalhar(usuarioId);

        if (usuario.senhaAtualNaoCoincide(senhaAtual)) {
            throw new SenhaNaoCoincideException();
        }

        usuario.setSenha(novaSenha);
    }

}
