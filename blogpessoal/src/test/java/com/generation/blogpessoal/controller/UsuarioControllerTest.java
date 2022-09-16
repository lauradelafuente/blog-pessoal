package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate; //enviar as requisições para a aplicação (acesso aos verbos http)
	
	@Autowired 
	private UsuarioService usuarioService; // persistie os objetos no Banco de dados de testes (h2) com a senha criptografada.
	
	@Autowired 
	private UsuarioRepository usuarioRepository;//limpa o Banco de dados de testes e cadastra um usuario padrão 
	
	@BeforeAll
	void start() { //apaga todos os dados da tabela e cri o usuário root@root
		usuarioRepository.deleteAll();
		
		usuarioService.cadastrarUsuario(new Usuario(0L,"Root","root@root.com","rootroot"," "));
	}
	
	@Test
	@DisplayName("Cadastrar um usuário")
	public void deveCriarUmUsuario() {
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>
		(new Usuario(0L,"Paulo Antunes","paulo@email.com.br","13465278","https://i.imgur.com/JR7kUFU.jpg")); //equivalente ao POST: Transforma os Atributos num objeto da Classe Usuario
		ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange //exchange envia a requisição HTTP e responseentity recebe a resposta, define a resposta do que foi "persistido" no db
		("/usuarios/cadastrar",HttpMethod.POST,corpoRequisicao,Usuario.class); //parametros para enviar a requisição: URI(endpoint),método HTTP,Obj HttpEntity,ResponseBody(Usuario.class)
		assertEquals(HttpStatus.CREATED,corpoResposta.getStatusCode()); //verifica se o status http da resposta foi 201 Created
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());//assertEquals checa se persiste no db;getBody faz o acesso a requisição e resposta 
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
	}
	
	@Test
	@DisplayName("Não deve permitir duplicação do usuário")
	public void naoDeveDuplicarUsuario() { //cadastra dois usuarios iguais e espera a resposta BAD REQUEST
		usuarioService.cadastrarUsuario(new Usuario //persiste um objeto no db
			(0L,"Maria da Silva","maria_silva@email.com.br","13465278","https://i.imgur.com/T12NIp9.jpg"));
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario> //cria a requisição
			(new Usuario(0L,"Maria da Silva","maria_silva@email.com.br","13465278","https://i.imgur.com/T12NIp9.jpg"));
		ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange
			("/usuarios/cadastrar",HttpMethod.POST,corpoRequisicao,Usuario.class);
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
	}
	
	@Test
	@DisplayName("Atualizar um usuário")
	public void deveAtualizarUmUsuario() {
		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario
			(new Usuario(0L,"Juliana Andrews", "juliana_andrews@email.com.br", "juliana123", "https://i.imgur.com/yDRVeK7.jpg"));
		Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(), //usuarioupdate atualiza os dados persistidos no usuarioCreate
			"Juliana Andrews Ramos", "juliana_ramos@email.com.br", "juliana123" , "https://i.imgur.com/yDRVeK7.jpg");
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);
		ResponseEntity<Usuario> corpoResposta = testRestTemplate //efetua login com usuario e senha valida para fazer os testes (Spring Security)
			.withBasicAuth("root@root.com", "rootroot") //autentica o usuario e a senha
			.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class); 
			assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
			assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
			assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
	}
	
	@Test
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() {
		usuarioService.cadastrarUsuario(new Usuario
			(0L,"Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123", "https://i.imgur.com/5M2p5Wb.jpg"));
		usuarioService.cadastrarUsuario(new Usuario
			(0L,"Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123", "https://i.imgur.com/Sk5SjWE.jpg"));
		ResponseEntity<String> resposta = testRestTemplate
			.withBasicAuth("root@root.com", "rootroot")
			.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
			assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
}
