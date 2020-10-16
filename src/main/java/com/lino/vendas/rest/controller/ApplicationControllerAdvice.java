package com.lino.vendas.rest.controller;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lino.vendas.exception.PedidoNaoEcontradoException;
import com.lino.vendas.exception.RegraNegocioException;
import com.lino.vendas.rest.ApiErrors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

	@ExceptionHandler(RegraNegocioException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleRegraNegocioException(RegraNegocioException ex) {
		
		String mensagemErro = ex.getMessage();
		return new ApiErrors(mensagemErro);

	}
	
	@ExceptionHandler(PedidoNaoEcontradoException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiErrors handlePedidoNotFoundException(PedidoNaoEcontradoException ex) {
		
		return new ApiErrors(ex.getMessage());
		
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handlleMethodNotValidExcption(MethodArgumentNotValidException ex) {
		List<String> errors = ex.getBindingResult().getAllErrors().stream()
					.map(erro -> erro.getDefaultMessage() ).collect(Collectors.toList());
		
		return new ApiErrors(errors);
	}
	
}
