package com.generation.blogpessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;

@Service //faz validações através de laços condicionais
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private String criptografarSenha(String senha) { //criptografa a senha do usuário
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); //método que criptografa a senha digitada 
		return encoder.encode(senha);
	}
	
	private boolean compararSenhas(String senhaDigitada, String senhaBanco) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(senhaDigitada, senhaBanco);
	}
	
	private String gerarBasicToken(String usuario, String senha) {
		String token = usuario + ":" + senha;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		return "Basic" + new String(tokenBase64);
	}
	
	public Optional<Usuario> cadastrarUsuario(Usuario usuario) { //verifica se o objeto já tem cadastro, criptografa a senha e manda o objeto para o db
		if(usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) //verifica se o usuário está cadastrado no db
			return Optional.empty();
		
		usuario.setSenha(criptografarSenha(usuario.getSenha())); //criptografa a senha digitada antes de mandar o objeto para o db
		return Optional.of(usuarioRepository.save(usuario)); //manda o objeto para o banco de dados com a senha criptografada
	}
	
	public Optional<Usuario> atualizarUsuario(Usuario usuario) { //atualiza um usuario ja cadastrado
		if (usuarioRepository.findById(usuario.getId()).isPresent()) { //verifica se o id passado no objeto de usuário já existe para poder fazer a atualização, sem o id ele não atualiza
			Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario()); //verifica pelo email digitado do usuario se ele já está cadastrado no banco de dados
		if ((buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != usuario.getId())) //valida se o usuario está presente E se o id passado não é diferente do cadastrado no banco de dados
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
		
		usuario.setSenha(criptografarSenha(usuario.getSenha())); //criptografa novamente a senha do usuario antes de mandar para o db
		return Optional.ofNullable(usuarioRepository.save(usuario)); //insere o objeto de usuario atualizado no banco de dados 
		}
		
		return Optional.empty();
	}
	
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) { //cria usuario login
		Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());
		
		if (usuario.isPresent()) { //verifica se o usuário existe 
			if (compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) { //pega as informações do banco de dados e insere no objeto usuarioLogin

                usuarioLogin.get().setId(usuario.get().getId());
                usuarioLogin.get().setNome(usuario.get().getNome());
                usuarioLogin.get().setFoto(usuario.get().getFoto());
                usuarioLogin.get().setToken(gerarBasicToken(usuarioLogin.get().getUsuario(),usuarioLogin.get().getSenha())); //token é gerado antes da senha por conta da criptografia que precisa ser devolvida 
                usuarioLogin.get().setSenha(usuario.get().getSenha());

                return usuarioLogin;
            }
		}
		return Optional.empty(); //se não conseguir logar, retorna como vazio
	}
}
