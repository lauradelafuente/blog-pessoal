package com.generation.blogpessoal.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.generation.blogpessoal.model.Usuario;

public class UserDetailsImpl implements UserDetails{ //A classe fornece as informações básicas do usuário para o Spring Security, UserDetails (Usuário, Senha, Direitos de acesso e as Restrições da conta).

private static final long serialVersionUID =1L; //declara a variável constante (não muda), 1L = padronização de id como Long

	private String userName;
	private String password;
	
	private List<GrantedAuthority> authorities; //da as permissões para o usuário que está se cadastrando
	
	public UserDetailsImpl (Usuario user) { //passa dados de cadastro para a segurança(usuario e senha)
		this.userName = user.getUsuario(); 
		this.password = user.getSenha();
	}
	
	public UserDetailsImpl () { } //delimita que para realizar o cadastro os atributos usuario e senha devem estar preenchidos (construtor vazio não barra informações a mais como nome,foto...)
	
	@Override //sobrescrita dos métodos
	public Collection<? extends GrantedAuthority> getAuthorities() { //Retorna as autoridades concedidas ao usuário. Não pode retornar null.
		
		return authorities;
	}
	
	@Override
	public String getPassword() { //Retorna a senha usada para autenticar o usuário(passa a senha definida)
		
		return password;
	}
	
	@Override
	public String getUsername() { //Retorna o nome de usuário usado para autenticar o usuário(considera o usuario passado). Não pode retornar null.
		
		return userName;
	}
	
	@Override
	public boolean isAccountNonExpired() { //Indica se a conta do usuário expirou. Uma conta expirada não pode ser autenticada (return false).
		
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() { //Indica se o usuário está bloqueado ou desbloqueado. Um usuário bloqueado não pode ser autenticado (return false).
		
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() { //Indica se as credenciais do usuário (senha) expiraram. Senha expirada impede a autenticação (return false).
		
		return true;
	}
	
	@Override
	public boolean isEnabled() { //Indica se o usuário está habilitado ou desabilitado. Um usuário desabilitado não pode ser autenticado (return false).
		
		return true;
	}
}
