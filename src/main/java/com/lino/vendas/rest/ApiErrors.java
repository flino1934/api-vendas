package com.lino.vendas.rest;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public class ApiErrors {

	@Getter
	private List<String> errors;// vai ser um array de erros.

	public ApiErrors(String menssageErro) {

		this.errors = Arrays.asList(menssageErro);

	}

	public ApiErrors(List<String> errors) {

		this.errors = errors;
	}

}
