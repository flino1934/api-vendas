package com.lino.vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lino.vendas.domain.entity.ItemPedido;

public interface ItemsPedidoRepository extends JpaRepository<ItemPedido, Integer> {

}
