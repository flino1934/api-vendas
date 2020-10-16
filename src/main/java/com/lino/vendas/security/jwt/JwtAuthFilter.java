package com.lino.vendas.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lino.vendas.service.impl.UsuarioServiceImpl;
import com.lino.vendas.validation.JwtService;

public class JwtAuthFilter extends OncePerRequestFilter{

	private JwtService jwtSertvice;
	private UsuarioServiceImpl usuarioService;
	
	
	
	public JwtAuthFilter(JwtService jwtSertvice, UsuarioServiceImpl usuarioService) {
		
		this.jwtSertvice = jwtSertvice;
		this.usuarioService = usuarioService;
	}



	@Override
	protected void doFilterInternal(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorization = httpServletRequest.getHeader("Authorization");
		
		if (authorization != null && authorization.startsWith("Bearer")) {// esta verificando se esta no padrão passando o authorization e o bearer
			
			String token = authorization.split(" ")[1];// vai retirar o espaço juntando o bearer e o token
			boolean isValid = jwtSertvice.tokenValido(token);

			if (isValid) {

				String loginUsuario = jwtSertvice.obterLoginUsuario(token);
				// se o usuario for valido ele vai obeter o login de usuario
				UserDetails usuario = usuarioService.loadUserByUsername(loginUsuario);
				UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(usuario, null,
						usuario.getAuthorities());
				user.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
				SecurityContextHolder.getContext().setAuthentication(user);
			}
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
		
	}


}
