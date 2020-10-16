package com.lino.vendas.service;

import java.util.Optional;

import com.lino.vendas.domain.entity.Pedido;
import com.lino.vendas.domain.enums.StatusPedido;
import com.lino.vendas.rest.dto.PedidoDTO;

public interface PedidoService {

	Pedido salvar(PedidoDTO dto);
	
	Optional<Pedido> obterPedidoCompleto(Integer id);
	
	void atualizaStatus(Integer id,StatusPedido statusPedido);
}
