package com.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.blogpessoal.model.Usuario;

//web enviroment= ambiente web
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // executa e finaliza, ciclo de vida
public class UsuarioRepositoryTeste {

	@Autowired
	private UsuarioRepository usuarioRepository;

	// inserir info no bd

	@BeforeAll // antes de rodar qualquer teste,insere as informações
	void start() {// toda vez que rodar o teste, insere info no bd virtual
		usuarioRepository.deleteAll();// valida se tiver alguma coisa, ele zera tudo antes de inserir as informações
		usuarioRepository.save(// posição 0 Long-autoincrement 0L
				new Usuario(0L, "João da Silva","https://i.imgur.com/h4t8loa.jpg", "joao@email.com.br", "13465278" ));

		usuarioRepository.save(new Usuario(0L, "Manuela da Silva","https://i.imgur.com/NtyGneo.jpg", "manuela@email.com.br", "13465278"
				));

		usuarioRepository.save(new Usuario(0L, "Adriana da Silva","https://i.imgur.com/5M2p5Wb.jpg", "adriana@email.com.br", "13465278"
				));

		usuarioRepository.save(
				new Usuario(0L, "Paulo Antunes","https://i.imgur.com/FETvs20.jpg", "paulo@email.com.br", "13465278" ));
	}

	@Test // faz o primeiro teste
	@DisplayName("Retornar 1 usuário") // método para mostrar o usuario
	public void deveRetornarUmUsuario() {

		Optional<Usuario> usuario = usuarioRepository.findByUsuario("joao@email.com.br");//se aqui estiver errado, da Error(erro do back com o bd)
		assertTrue(usuario.get().getUsuario().equals("joao@email.com.br"));//se estiver errado, dá Failure(erro no front)
		// pesquisa o usuario		
	}
	
	@Test
	@DisplayName("Retorna 3 usuarios")
	public void deveRetornarTresUsuarios() {
		
		List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Silva");
		assertEquals(3, listaDeUsuarios.size());
		assertTrue(listaDeUsuarios.get(0).getNome().equals("João da Silva"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Manuela da Silva"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("Adriana da Silva"));
	}


}
