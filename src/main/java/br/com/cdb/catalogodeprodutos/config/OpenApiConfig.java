package br.com.cdb.catalogodeprodutos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Catálogo de Produtos API",
                description = "API para gerenciamento de produtos e categorias. " +
                        "Permite consultas, criação, atualização e remoção de produtos.",
                contact = @Contact(
                        name = "Lidiane Mendes",
                        email = "lidyaraujo64@gmail.com",
                        url = "https://github.com/LidianeMendess"
                )
        )
)
public class OpenApiConfig {

}