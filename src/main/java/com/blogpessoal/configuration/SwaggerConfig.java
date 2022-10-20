package com.blogpessoal.configuration;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Configuration // anotation
public class SwaggerConfig {
	// criar método tipo nome
	@Bean // deixa o método instanciável, pode mexer na estrutura, igual a @Service
	public OpenAPI SpringBlogPessoalApi() {
		return new OpenAPI()// acessa a estrutura do Openapi e
				.info(new Info()// acessa info, e cria uma nova
						.title("Roberta Ribeiro").description("Me conheça melhor e veja meus projetos")
						.version("v0.0.1")
						.license(new License().name("Generation Brasil").url("https://brazil.generation.org/"))
						.contact(new Contact().name("Roberta Ribeiro").url("https://github.com/HarukaIonaSao")
								.email("robertaribeiro004@gmail.com")))
				.externalDocs(new ExternalDocumentation()
						.description("Card portifólio")
						.url("https://harukaionasao.github.io/Card/")
						.description("Projeto Serra da Capivara")
						.url("https://github.com/HarukaIonaSao/SerraDaCapijava")
						.description("Linkedin")
						.url("https://www.linkedin.com/in/roberta-ribeiro-ela-she-b5521a4b/"));

	}

	@Bean
	public OpenApiCustomiser customerGlobalHeaderOpenApiCustomiser() {

		return openApi -> {// -> atribuir Path(caminho) depois vai mapear e acessar o item (endpoints) e
							// depois configura
			openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {

				ApiResponses apiResponses = operation.getResponses();

				// substitui a msg de erro do response entity no documento

				apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
				apiResponses.addApiResponse("201", createApiResponse("Objeto Persistido!"));
				apiResponses.addApiResponse("204", createApiResponse("Objeto Excluído!"));
				apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição!"));
				apiResponses.addApiResponse("401", createApiResponse("Acesso Não Autorizado!"));
				apiResponses.addApiResponse("404", createApiResponse("Objeto Não Encontrado!"));
				apiResponses.addApiResponse("500", createApiResponse("Erro na Aplicação!"));

			}));
		};
	}

	private ApiResponse createApiResponse(String message) {

		return new ApiResponse().description(message);

	}

}
