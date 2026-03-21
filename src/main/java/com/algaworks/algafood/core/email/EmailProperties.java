package com.algaworks.algafood.core.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Getter
@Setter
@Component
@ConfigurationProperties("algafood.email")
public class EmailProperties {

    @NotNull
    private String remetente;

    private TipoEmail impl = TipoEmail.FAKE;

    private Sandbox sandbox = new Sandbox();

    public enum TipoEmail {

        FAKE, SMTP, SANDBOX

    }

    @Getter
    @Setter
    public class Sandbox {

        private String destinatario;

    }

}
