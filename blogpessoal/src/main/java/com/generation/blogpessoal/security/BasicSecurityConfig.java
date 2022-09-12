package com.generation.blogpessoal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //indica que a Classe é do tipo configuração, ou seja, define uma Classe como fonte de definições de Beans
@EnableWebSecurity //habilita a segurança de forma Global (toda a aplicação) e sobrescreve os Métodos que irão redefinir as regras de Segurança da sua aplicação.
public class BasicSecurityConfig {

	@Bean //Objeto que é instanciado, montado e gerenciado pelo Spring(injeção de dependencia).Classe irá escrever as regras de funcionamento da sua aplicação, que poderão ser utilizadas em qualquer Classe, diferente do Autowired
	public PasswordEncoder passwordEncoder() { //Indica que a aplicação está baseada em um modelo de criptografia.
		return new BCryptPasswordEncoder(); //Modelo de criptografia utilizado, se trata de um algoritmo de criptografia do tipo hash
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception { // Informa ao spring que a configuração de autenticação será redefinida em sua implementação, define que será considerado usuario e senha(regra de negócio) 
        return authenticationConfiguration.getAuthenticationManager(); //Procura uma implementação da Interface UserDetailsService e a utiliza para identificar se o usuário é válido ou não
    }
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { //Informa ao Spring que a configuração padrão será substituida por uma nova
		http
		.sessionManagement() //Define que o sistema não guardará sessões para o cliente. Terá começo, meio e fim, impede bots, só permite uma requisição por vez 
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().csrf().disable() //padrão de segurança, permite que o usuário faça put e delete
		.cors(); //evita bloqueio de acesso (permite que o frontend mande requisições)
		http //obriga usuario a se cadastrar para conseguir fazer alterações
		.authorizeHttpRequests((auth) -> auth
			.antMatchers("/usuarios/logar").permitAll()
			.antMatchers("/usuarios/cadastrar").permitAll()
			.antMatchers(HttpMethod.OPTIONS).permitAll() //da acesso as aplicações (CRUD) após o usuário estar logado.
			.anyRequest().authenticated())
		.httpBasic();
		
		return http.build(); //substitui a segurança padrão
	}
}
