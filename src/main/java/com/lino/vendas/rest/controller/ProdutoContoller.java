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

import com.lino.vendas.domain.entity.Produto;
import com.lino.vendas.repository.ProdutosRepository;

@RestController
@RequestMapping("api/produtos")
public class ProdutoContoller {

	private ProdutosRepository produtosRepository;

	public ProdutoContoller(ProdutosRepository produtosRepository) {
		this.produtosRepository = produtosRepository;
	}

	@GetMapping("{id}")
	public void getById(@PathVariable("id") Integer id) {
		
		produtosRepository.findById(id)
							.orElseThrow(() ->
							new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não encontrado"));
							
						}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Produto save(@RequestBody @Valid Produto produto) {
		
		return produtosRepository.save(produto);
		
	}
	
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Integer id) {
		
		produtosRepository.findById(id)
							.map(produto ->{
								produtosRepository.delete(produto);
								return Void.TYPE;
							})
							.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não encontrado"));

						}
	
	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable("id") Integer id,@RequestBody 
						@Valid Produto produto) {
		
		produtosRepository.findById(id)
							.map(p -> {
								produto.setId(p.getId());
								produtosRepository.save(produto);
								return produto;
							}).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não encontrado!"));

						}
	
	@GetMapping
	public List<Produto> find(Produto filtro){
		
		ExampleMatcher matcher = ExampleMatcher
								.matching()
								.withIgnoreCase()
								.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
		Example<Produto> example = Example.of(filtro,matcher);
		List<Produto> lista = produtosRepository.findAll(example);
		return produtosRepository.findAll(example);

	}

}
