package com.lino.vendas.exception;

public class RegraNegocioException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public RegraNegocioException(String message) {//vai retornar uma mensagem de erro
		super(message);
	}

}
