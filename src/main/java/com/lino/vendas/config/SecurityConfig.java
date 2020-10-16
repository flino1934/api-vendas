package com.lino.vendas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lino.vendas.security.jwt.JwtAuthFilter;
import com.lino.vendas.service.impl.UsuarioServiceImpl;
import com.lino.vendas.validation.JwtService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UsuarioServiceImpl usuarioService;
	@Autowired
	private JwtService jwtService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();//ele tem um metodo de criptografia propia toda vez que for passado a senha
		//vai ser passado o hash proprio de criptografia o que aumenta a segurança do sistema nunca se repete o hash
	
	}
	
	
	
	@Bean
	public OncePerRequestFilter jwtFilter() {
		return new JwtAuthFilter(jwtService, usuarioService);
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
//		auth.inMemoryAuthentication()//esta passando o usuario em memoria por enquanto.
//			.passwordEncoder(passwordEncoder())//vai passar como uma instancia 
//			.withUser("fulano")//esta passando o nome do usuario
//			.password(passwordEncoder().encode("123"))//a senha e precisa passar ele como encode pq é cripitografada
//			.roles("USER","ADMIN");//papel exercido dentro do sistema
		
		auth.
			userDetailsService(usuarioService)//vai carregar os usuarios autenticados
			.passwordEncoder(passwordEncoder());//vai ser utilizado para comparar a senha
		
	}//ele vai trazer os objetos que vão fazer autenticação (login,senha,nome de usuario...)
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//aqui esta toda a configuração de api quais paginas eles vão ter acesso e qua papel tem acesso 
		http
			.csrf().disable()//desabilitou pois não é uma aplicação web estamos só trabalhando com back e rest
			.authorizeRequests()
				.antMatchers("/api/clientes/**")//esta tratando a permissão deste endPoint
					.hasAnyRole("USER","ADMIN")//os papeis que tem acesso
				.antMatchers("/api/produtos/**")
					.hasAnyRole("USER","ADMIN")
				.antMatchers("/api/pedidos/**")
					.hasRole("USER")
				.antMatchers(HttpMethod.POST,"/api/usuarios/**")
					.permitAll()
				.anyRequest().authenticated()
			.and()//volta para a raiz do endpoint
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
				
				
	}//parte de autorização ele vai pegar o usuario autenticado e verificar se ele tem autorização para oq 
	

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
    
}
