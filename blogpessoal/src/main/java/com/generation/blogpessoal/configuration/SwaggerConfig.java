package com.generation.blogpessoal.configuration;

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

@Configuration // indica que a Classe é do tipo configuração
public class SwaggerConfig {

	@Bean
	public OpenAPI springBlogPessoalOpenAPI() { //Cria um Objeto da Classe OpenAPI, que gera a documentação no Swagger utilizando a especificação OpenAPI.
		return new OpenAPI()
				.info(new Info() // Insere as informações sobre a API (Nome do projeto (Title), Descrição e Versão)
					.title("Projeto Blog Pessoal")
					.description("Projeto Blog Pessoal - Generation Brasil")
					.version("v0.0.1")
				.license(new License() // Insere as informações referentes a licença da API (Nome e Link)
					.name("Laura De La Fuente")
					.url("https://www.linkedin.com/in/laura-delafuente/"))
				.contact(new Contact()// Insere as informações de contato da pessoa Desenvolvedora
					.name("Laura De La Fuente")
					.url("https://github.com/lauradelafuente")
					.email("laura.delafuente@outlook.com.br")))
				.externalDocs(new ExternalDocumentation() //Insere as informações referentes a Documentações Externas
					.description("Github")
					.url("https://github.com/lauradelafuente"));
	}
	
	@Bean
	public OpenApiCustomiser customerGlobalHeaderOpenApiCustomiser() { //permite personalizar o Swagger, baseado na Especificação OpenAPI.
		return openApi -> {
			openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> { //1 looping - leitura de todos os recursos(getPaths), que retorna o caminho de cada endpoint. 2 looping - identifica o método HTTP(eadOperations)
				ApiResponses apiResponses = operation.getResponses();//Cria um Objeto da Classe ApiResponses, que receberá as Respostas HTTP de cada endpoint(Paths) através do Método getResponses().
				
				apiResponses.addApiResponse("200", createApiResponse("Sucesso!")); //Adiciona respostas no endpoint  
				apiResponses.addApiResponse("201", createApiResponse("Objeto Persistido!"));
				apiResponses.addApiResponse("204", createApiResponse("Objeto Excluído!"));
				apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição!"));
				apiResponses.addApiResponse("401", createApiResponse("Acesso Não Autorizado!"));
				apiResponses.addApiResponse("404", createApiResponse("Objeto Não Encontrado!"));
				apiResponses.addApiResponse("500", createApiResponse("Erro na Aplicação!"));
				
			}));
		};
	}
	private ApiResponse createApiResponse(String message) { //adiciona uma descrição em cada Resposta HTTP
		return new ApiResponse().description(message);
	}
}
