package com.lino.vendas.rest.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformacoesItemPedidosDTO {

	private String descricaoItemPedido;
	private BigDecimal precoUnitario;
	private Integer quantidade;

}
