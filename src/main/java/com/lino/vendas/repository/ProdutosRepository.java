package com.lino.vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lino.vendas.domain.entity.Produto;

public interface ProdutosRepository extends JpaRepository<Produto, Integer>{

}
