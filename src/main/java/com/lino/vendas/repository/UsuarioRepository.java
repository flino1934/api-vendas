package com.lino.vendas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lino.vendas.domain.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	
	
	Optional<Usuario> findByLogin(String login);

}
