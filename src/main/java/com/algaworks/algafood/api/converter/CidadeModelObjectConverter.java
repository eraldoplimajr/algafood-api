package com.algaworks.algafood.api.converter;

import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CidadeModelObjectConverter {

    @Autowired
    private ModelMapper modelMapper;

    public Cidade toDomainObject(CidadeInput cidadeInput) {
        return modelMapper.map(cidadeInput, Cidade.class);
    }

    public void copyToDomainObject(CidadeInput cidadeInput, Cidade cidade) {

        // para evitar org.hibernate.HibernateException:
        // identifier of an instance of com.algaworks.algafood.domain.model.Estado was altered from 2 to 1
        cidade.setEstado(new Estado());

        modelMapper.map(cidadeInput, cidade);
    }

}
