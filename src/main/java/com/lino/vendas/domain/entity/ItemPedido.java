package com.lino.vendas.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne//muitos para um, muitos item para um pedido
	@JoinColumn(name = "pedido_id")
	private Pedido pedido;
	@ManyToOne//muitos para um, muitos item para um produto
	@JoinColumn(name = "produto_id")
	private Produto produto;
	@Column(name = "quantidade")
	private Integer quantidade;


}
