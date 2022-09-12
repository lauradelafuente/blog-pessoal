package com.generation.blogpessoal.model;

public class UsuarioLogin { //apoio para diferenciar usuario logado do não logado

	private Long id;
	private String nome;
	private String usuario;
	private String senha;
	private String foto;
	private String token; //armazena as informações de usuario e senha, se estiver logado utiliza o token. chave para fazer alterações na aplicação 
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
