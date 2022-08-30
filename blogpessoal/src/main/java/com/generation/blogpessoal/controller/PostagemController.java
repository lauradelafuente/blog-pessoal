package com.generation.blogpessoal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.model.Postagem;

@RestController //deﬁne o tipo da Classe, que receberá requisições que serão compostas por:URL,verbo,corpo(contém os dados do db)
@RequestMapping("/postagens") //definir a URL (endereço) padrão do Recurso
@CrossOrigin(origins = "*", allowedHeaders = "*") //indica que a Classe controladora permitirá o recebimento de requisições realizadas de fora do domínio (front end consegue acessar)
public class PostagemController {
		
		@Autowired //define quais Classes serão instanciadas e em quais lugares serão Injetadas quando houver necessidade.
		private PostagemRepository postagemRepository;
		
		@GetMapping //indica que o Método getAll(), responderá a todas as requisições do tipo HTTP GET, enviadas no endereço http://localhost:8080/postagens/.
		public ResponseEntity<List<Postagem>> getAll(){ // ResponseEntity responderá a Requisição HTTP (HTTP Request), com uma Resposta HTTP;ListPostagem será retornado um Objeto da Classe List (Collection), contendo todos os Objetos da Classe Postagem
			return ResponseEntity.ok(postagemRepository.findAll()); //retornará todos os Objetos da Classe Postagem persistidos no Banco de dados ListPostagem
		}
	}


