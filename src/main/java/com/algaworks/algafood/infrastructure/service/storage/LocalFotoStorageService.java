package com.algaworks.algafood.infrastructure.service.storage;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.service.FotoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalFotoStorageService implements FotoStorageService {

    @Override
    public InputStream recuperar(String nomeArquivo) {

        try {
            Path arquivoPath = getArquivoPath(nomeArquivo);

            return Files.newInputStream(arquivoPath);

        } catch (Exception e) {
            throw new StorageException("Não foi possível recuperar o arquivo.", e);
        }
    }

    @Autowired
    private StorageProperties storageProperties;

    @Override
    public void armazenar(NovaFoto novaFoto) {

        try {

            Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());

            FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));

        } catch (Exception e) {
            throw new StorageException("Não foi possível armazenar o arquivo.", e);
        }

    }

    @Override
    public void remover(String nomeArquivo) {

        try {
            Path arquivoPath = getArquivoPath(nomeArquivo);

            Files.deleteIfExists(arquivoPath);

        } catch (Exception e) {
            throw new StorageException("Não foi possível excluir arquivo.", e);
        }
    }

    private Path getArquivoPath(String nomeArquivo) {
        return storageProperties.getLocal().getDiretorioFotos().resolve(Path.of(nomeArquivo));
    }

}
