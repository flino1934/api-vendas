package com.lino.vendas.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lino.vendas.domain.entity.Cliente;
import com.lino.vendas.domain.entity.ItemPedido;
import com.lino.vendas.domain.entity.Pedido;
import com.lino.vendas.domain.entity.Produto;
import com.lino.vendas.domain.enums.StatusPedido;
import com.lino.vendas.exception.PedidoNaoEcontradoException;
import com.lino.vendas.exception.RegraNegocioException;
import com.lino.vendas.repository.ClientesRepository;
import com.lino.vendas.repository.ItemsPedidoRepository;
import com.lino.vendas.repository.PedidosRepository;
import com.lino.vendas.repository.ProdutosRepository;
import com.lino.vendas.rest.dto.ItemPedidoDTO;
import com.lino.vendas.rest.dto.PedidoDTO;
import com.lino.vendas.service.PedidoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {// aqui vai toda a lógica de salvar o pedido

	private final PedidosRepository pedidosRepository;
	private final ClientesRepository clientesRepository;
	private final ProdutosRepository produtosRepository;
	private final ItemsPedidoRepository itemsPedidoRepository; 

	@Override
	@Transactional
	public Pedido salvar(PedidoDTO dto) {

		Integer idCliente = dto.getCliente();//vai pegar o id do cliente
		Cliente cliente = clientesRepository.findById(idCliente)
				.orElseThrow(() -> new RegraNegocioException("Codigo de cliente Invalido"));//vai verificar se existe o id que foi pego
		
		Pedido pedido = new Pedido();// vai arrmazenar os dados inicialmente no obj e em seguida salvar na base
		pedido.setTotal(dto.getTotal());
		pedido.setDataPedido(LocalDate.now());
		pedido.setCliente(cliente);
		pedido.setStatus(StatusPedido.REALIZADO);

		List<ItemPedido> itemsItemPedido = converterItems(pedido, dto.getItems());
		pedidosRepository.save(pedido);
		itemsPedidoRepository.saveAll(itemsItemPedido);
		pedido.setItens(itemsItemPedido);
		return pedido;
	}

	private List<ItemPedido> converterItems(Pedido pedido ,List<ItemPedidoDTO> items) {
		
		if (items.isEmpty()) {
			throw new RegraNegocioException("Não é possivel realizar um pedido sem items");// vai verificar se a items no pedido 
		}
		return items
				.stream()
				.map(dto -> {
					Integer idProduto = dto.getProduto();// vai verificar o id do peroduto
					Produto produto = produtosRepository.findById(idProduto)//vai pesquisar o id do produto
							.orElseThrow(() -> new RegraNegocioException("Codigo de produto Inválido: "+idProduto));
					
					ItemPedido itemPedido = new ItemPedido();
					itemPedido.setQuantidade(dto.getQuantidade());
					itemPedido.setPedido(pedido);
					itemPedido.setProduto(produto);
					return itemPedido;
				}).collect(Collectors.toList());
	}

	@Override
	public Optional<Pedido> obterPedidoCompleto(Integer id) {
		
		return pedidosRepository.findByIdFetchItens(id);
	}

	@Override
	@Transactional
	public void atualizaStatus(Integer id, StatusPedido statusPedido) {
		
		pedidosRepository
		.findById(id)
		.map(pedido ->{
			pedido.setStatus(statusPedido);
			return pedidosRepository.save(pedido);
		}).orElseThrow(()->new PedidoNaoEcontradoException());
		
		
	}

}
