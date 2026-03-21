package com.algaworks.algafood.core.email;

import com.algaworks.algafood.core.email.EmailProperties.TipoEmail;
import com.algaworks.algafood.domain.service.EnvioEmailService;
import com.algaworks.algafood.infrastructure.service.email.FakeEnvioEmailService;
import com.algaworks.algafood.infrastructure.service.email.SmtpEnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

    @Autowired
    private EmailProperties emailProperties;

    @Bean
    public EnvioEmailService envioEmailService() {
        if(TipoEmail.SMTP.equals(emailProperties.getImpl())) {
            return new SmtpEnvioEmailService();
        } else {
            return new FakeEnvioEmailService();
        }
    }

}
