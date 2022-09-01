package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity //será utilizada para gerar uma tabela no Banco de dados
@Table(name="tb_postagens") //nome da Tabela no Banco de dados
public class Postagem {

	@Id //inidica que o Atributo anotado será a Chave Primária
	@GeneratedValue(strategy = GenerationType.IDENTITY) //indica que a Chave Primária será gerada pelo Spring Data JPA
	private Long id;
	
	@NotBlank(message = "O atributo título é obrigatório!") // não permite que o Atributo seja Nulo ou contenha apenas Espaços em branco.
	@Size(min = 5, max = 100, message = "O atributo título deve conter no mínimo 05 e no máximo 100 carecteres") //define o valor Mínimo (min) e Máximo (max) de Caracteres do Atributo
	private String titulo;
	
	@NotBlank(message = "O atributo texto é obrigatório!")
	@Size(min = 10, max = 1000, message = "O atributo texto deve conter no mínimo 10 e no máximo 1000 caracteres")
	private String texto;
	
	@UpdateTimestamp // configura o Atributo data como Timestamp
	private LocalDateTime data;

	@ManyToOne //indica que Postagem será o lado N:1 e terá um Objeto da Classe Tema, que será a Chave Estrangeira na Tabela tb_postagens (tema_id).
	@JsonIgnoreProperties("postagem") //indica que uma parte do JSON será ignorado, o objeto tema será exibido como um sub objeto de postagem
	private Tema tema; //Objeto da Classe Tema, que receberá os dados de Postagem, que representa a chave estrangeira tema_id em tb_postagens 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}
	
}
