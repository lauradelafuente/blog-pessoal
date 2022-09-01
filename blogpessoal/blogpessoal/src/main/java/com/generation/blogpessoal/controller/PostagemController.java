package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.repository.PostagemRepository;
import com.generation.blogpessoal.repository.TemaRepository;
import com.generation.blogpessoal.model.Postagem;

@RestController //deﬁne o tipo da Classe, que receberá requisições que serão compostas por:URL,verbo,corpo(contém os dados do db)
@RequestMapping("/postagens") //definir a URL (endereço) padrão do Recurso
@CrossOrigin(origins = "*", allowedHeaders = "*") //indica que a Classe controladora permitirá o recebimento de requisições realizadas de fora do domínio (front end consegue acessar)
public class PostagemController {
		
		@Autowired //define quais Classes serão instanciadas e em quais lugares serão Injetadas quando houver necessidade, transefere responsabilidade para o repositório(injeção de dependencia)
		private PostagemRepository postagemRepository;
		
		@Autowired 
		private TemaRepository temaRepository;
		
		@GetMapping //indica que o Método getAll(), responderá a todas as requisições do tipo HTTP GET, enviadas no endereço http://localhost:8080/postagens/.
		public ResponseEntity<List<Postagem>> getAll(){ // ResponseEntity responderá a Requisição HTTP (HTTP Request), com uma Resposta HTTP;ListPostagem será retornado um Objeto da Classe List (Collection), contendo todos os Objetos da Classe Postagem
			return ResponseEntity.ok(postagemRepository.findAll()); //retornará todos os Objetos da Classe Postagem persistidos no Banco de dados ListPostagem
		}
		
		@GetMapping("/{id}") //indica que o Método getById( Long id ), responderá a todas as requisições do tipo HTTP GET
		public ResponseEntity<Postagem> getById(@PathVariable Long id) { //O Método getById será do tipo ResponseEntity pq ele responderá Requisições HTTP, com uma Resposta HTTP e PathVariable insere o valor enviado no endereço do endpoint, {id} e no Método getById(Long id); postagens/1
			return postagemRepository.findById(id) //O Método retornará um Objeto da Classe Postagem persistido no Banco de dados
					.map(resposta -> ResponseEntity.ok(resposta)) // se o Objeto da Classe Postagem for econtrado, o Método mapeia o objeto retornado, insere na resposta e retorna ok
					.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); //se o objeto não for encontrado será retronado como erro. build constroi a resposta
		}
		
		@GetMapping("/titulo/{titulo}")
		public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo) { 
			return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
		}
		
		@PostMapping //indica que o Método post(Postagem postagem), responderá a todas as requisições do tipo HTTP POST
		public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) { //retornará a requisição/ @valid valida o objeto postagem conforme as regras em "Postagem" (notBlank)/ @RequestBody recebe o objeto postagem e insere no parâmetro Postagem do Método post.
			if(temaRepository.existsById(postagem.getTema().getId()))
				return ResponseEntity.status(HttpStatus.CREATED)
						.body(postagemRepository.save(postagem));
		
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		@PutMapping //indica que o Método put(Postagem postagem), responderá a todas as requisições do tipo HTTP PUT
		public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) { 
			if(postagemRepository.existsById(postagem.getId())) {
				if(temaRepository.existsById(postagem.getTema().getId()))
					return ResponseEntity.status(HttpStatus.OK)
							.body(postagemRepository.save(postagem));
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		@ResponseStatus(HttpStatus.NO_CONTENT) //indica que o Método delete(Long id), terá uma Response Status específica (HTTP Status NO_CONTENT 🡪 204)
		@DeleteMapping("/{id}") //indica que o Método delete( Long id ), responderá a todas as requisições do tipo HTTP DELETE
		public void delete(@PathVariable Long id) { //id é uma Variável de Caminho (Path Variable), que receberá o id da Postagem que será Deletada.
			Optional<Postagem> postagem = postagemRepository.findById(id); //Objeto Optional da Classe Postagem chamado postagem. Optional evita o erro "Nulo", ao invés de utilizar map.
			if (postagem.isEmpty()) //Se o objeto postagem está vazio retorna como 404 (não encontrado)
				throw new ResponseStatusException(HttpStatus.NOT_FOUND); //as próximas linhas do método serão ignoradas
			
			postagemRepository.deleteById(id); //executa o método e retorna HTTP Status NO_CONTENT 🡪 204
		}
	}


