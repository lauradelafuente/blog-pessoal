package com.generation.blogpessoal.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService { //A classe é uma implementação da Interface UserDetailsService

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override //sobrescreve o que a segurança padrão define na entrada
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException { //Verifica cadastro e login, valida se o usuário existe no banco de dados, não permite usuários com o mesmo nome
		
		Optional<Usuario> usuario = usuarioRepository.findByUsuario(userName); //Busca por usuario(findBy); SELECT usuario FROM tb_usuarios = email@email.com
		
		if (usuario.isPresent()) //Verifica se já existe
			return new UserDetailsImpl(usuario.get());
		else 
			throw new ResponseStatusException(HttpStatus.FORBIDDEN); 
	}

}
