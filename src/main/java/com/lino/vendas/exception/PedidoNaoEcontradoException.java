package com.lino.vendas.exception;

public class PedidoNaoEcontradoException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PedidoNaoEcontradoException() {
		super("Pedido Não encontrado");
	}
	
}
