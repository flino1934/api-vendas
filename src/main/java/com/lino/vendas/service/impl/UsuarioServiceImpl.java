package com.lino.vendas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lino.vendas.domain.entity.Usuario;
import com.lino.vendas.exception.SenhaInvalidaException;
import com.lino.vendas.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UserDetailsService{
	//Essa classe tem o dever de carregar o usuario da base de dados

	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private UsuarioRepository repository;
	
	@Transactional//pq estamos em uma camada de serviço é necessario anotar com essa @
	public Usuario salvar(Usuario usuario) {
		return repository.save(usuario);
	}
	
	public UserDetails autenticar(Usuario usuario) {
		UserDetails user = loadUserByUsername(usuario.getLogin());
		boolean senhasBatem = encoder.matches(usuario.getSenha(), user.getPassword());
		if (senhasBatem) {
			return user;
		}
		
		throw new SenhaInvalidaException();
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuario usuario = repository.findByLogin(username)
				.orElseThrow(()-> new UsernameNotFoundException("Usuario não encontrado na base de dados"));
		
		String [] roles = usuario.isAdmin() ? new String[]{"ADMIN","USER"} : 
				new String [] {"USER"};
		//esta verificando com uma operação ternaria se é admin 
		
		return User.builder()
				.username(usuario.getLogin())
				.password(usuario.getSenha())
				.roles(roles)
				.build();
		
	}

}
