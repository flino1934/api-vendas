package com.lino.vendas.rest.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.lino.vendas.domain.entity.Cliente;
import com.lino.vendas.repository.ClientesRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/clientes")
@Api("Api Clientes")
public class ClienteController {

	private ClientesRepository clientesRepository;

	public ClienteController(ClientesRepository clientesRepository) {

		this.clientesRepository = clientesRepository;

	}

	
	@GetMapping("{id}")
	@ApiOperation("Obter Detalhes de clientes")
	@ApiResponses({
		@ApiResponse(code = 200,message = "Cliente encontrado"),
		@ApiResponse(code = 404,message = "Cliente não encontrado para o id informado")

	})
	public Cliente getClienteById(@PathVariable("id") @ApiParam("Id do cliente") Integer id) {

		return  clientesRepository.findById(id)
								.orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Cliente não encontrado!"));

	}
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation("Salva um novo cliente")
	@ApiResponses({
		@ApiResponse(code = 201,message = "Cliente Salvo com sucesso"),
		@ApiResponse(code = 404,message = "Erro de validação")

	})
	public Cliente save(@RequestBody @Valid Cliente cliente) {

		return clientesRepository.save(cliente);
		
	}
	
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Integer id) {
		
		clientesRepository.findById(id)
							.map(cliente -> {
								clientesRepository.delete(cliente);
								return cliente;
							})
							.orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Cliente não encontrado!"));
	
		
	}
	
	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable Integer id, 
								 @RequestBody @Valid Cliente cliente) {
		
		clientesRepository
							.findById(id)
								.map(clienteExistente -> {
									cliente.setId(clienteExistente.getId());//vai setar o id ja existente como o id para não gerar dois dados
									clientesRepository.save(cliente);
									return clienteExistente;
								}).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Cliente não encontrado!"));
		
	}
	
	
	@GetMapping
	public List<Cliente> find(Cliente filtro) {//vai pesquisar por alguma referencia o cliente
		
		ExampleMatcher matcher = ExampleMatcher
									.matching()
									.withIgnoreCase()
									.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		Example<Cliente> example = Example.of(filtro, matcher);
		List<Cliente> lista = clientesRepository.findAll(example);
		return clientesRepository.findAll(example);

	}

}
