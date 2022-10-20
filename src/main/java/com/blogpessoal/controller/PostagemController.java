package com.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.blogpessoal.model.Postagem;
import com.blogpessoal.repository.PostagemRepository;
import com.blogpessoal.repository.TemaRepository;

@RequestMapping("/postagens")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {// o controller se comunica com o front

	@Autowired
	private PostagemRepository postagemRepository;

	@Autowired
	private TemaRepository temaRepository;

	@GetMapping
	public ResponseEntity<List<Postagem>> getAll() {
		return ResponseEntity.ok(postagemRepository.findAll());

	}

	// cria o método de requisição , que está acessando a tabela de postagem
	@GetMapping("/{id}") // parâmetro para o identificador único
	public ResponseEntity<Postagem> getById(@PathVariable Long id) {// se a requisição der certo(200ok) ele executa a
																	// ação,como se fosse o if
		return postagemRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))// findById no MySql é
																							// equivalente ao getById
				// resposta é a comunicaçao com o bd
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		// o repository traduz a comunicação com o banco de dados. ele pede p o mysql o
		// elemento 1 da lista e dá p o ususário. Se não tiver, ele pergunta p o mysql e
		// fala p o usuário que não tem
	}

	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo) {
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
	} // pathvariable =pega variavel valida que esse é o caminho do método

	@PostMapping // método único não é preciso criar outra tabela
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) {// valida as informações do bd
		if (temaRepository.existsById(postagem.getTema().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}// tabela atributo

	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) {
		if (postagemRepository.existsById(postagem.getId())) {

			if (temaRepository.existsById(postagem.getTema().getId()))
				return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Postagem> postagem = postagemRepository.findById(id);

		if (postagem.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		postagemRepository.deleteById(id);
	}
}