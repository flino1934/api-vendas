package com.lino.vendas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lino.vendas.domain.entity.Cliente;
import com.lino.vendas.domain.entity.Pedido;

public interface PedidosRepository extends JpaRepository<Pedido, Integer>{
	
	List<Pedido> findByCliente(Cliente cliente);
	
	@Query(" select p from Pedido p left join fetch p.itens where p.id =:id ")
	Optional<Pedido>findByIdFetchItens(@Param("id") Integer id);
	

}
