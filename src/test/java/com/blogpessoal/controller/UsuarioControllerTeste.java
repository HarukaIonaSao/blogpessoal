package com.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.blogpessoal.model.Usuario;
import com.blogpessoal.repository.UsuarioRepository;
import com.blogpessoal.service.UsuarioService;

//@SpringBootTest indica que a Classe UsuarioControllerTest é uma
//Classe Spring Boot Testing. A Opção environment indica que caso a porta principal (8080 para
//uso local) esteja ocupada, o Spring irá atribuir uma outra porta automaticamente.

/*@TestMethodOrder diz a ordem de execuçaõ dos testes.A opção MethodOrdered.OrderAnnotation.class
 * mostra que os testes serão executados na  ordem indicada pela @Order
 * */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // O ciclo de vida da Classe teste é por classe, ele roda e finaliza
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTeste {

	// simula o cliente se comunicando com a Api(tipo Insominia), e usa os
	// protocolos Http(GET,PUT,POST,DELETE)
	@Autowired /*
				 * um objeto da Classe TestRestTemplate para enviar as requisições para a nossa
				 * aplicação.
				 */
	private TestRestTemplate testRestTemplate;

	@Autowired /*
				 * , um objeto da Classe UsuarioService para persistir os objetos no Banco de
				 * dados de testes com a senha criptografada.
				 */
	private UsuarioService usuarioService;

	@Autowired // um objeto da Interface UsuarioRepository para impar o Banco de dados de
				// testes
	private UsuarioRepository usuarioRepository;

	@BeforeAll // apaga todos os dados da tabela antes de inserir novos
	void start() {

		usuarioRepository.deleteAll();
	}

	@Test
	@Order(1) // será o primeiro a ser executado
	@DisplayName("Cadastrar Um Usuário") // mensagem a ser exibida no lugar do nome do método
	public void deveCriarUmUsuario() {

		/*
		 * foi criado um objeto da classe HttpEntity(requisicao), que recebeu um objeto
		 * da classe Usuario. Faz o mesmo que o Insomina no POST: transforma os
		 * atributos num objeto da Classe Usuario, que é enviado ao Request Body
		 */
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, "Paulo Antunes",
				"https://i.imgur.com/h4t8loa.jpg", "paulo_antunes@email.com.br", "12345678"));

		/*
		 * envia a requisicao Http passando 4 parâmetros: A URL-Endereço do endpoint
		 * ("/usuarios/cadastrar") O metodo Http- POST(Nesse caso) O objeto HttpEntity-
		 * requisicao(nesse caso), que contém o objeto da Classe Usuario Response Body-
		 * Tipo Usuario(nesse caso) (Usuario.class)
		 */
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao,
				Usuario.class);

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getFoto(), resposta.getBody().getFoto());
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());
	}

	@Test
	@Order(2) // será o primeiro a ser executado
	@DisplayName("Não deve permitir duplicação de usuário")
	public void naoDevePermitirDuplicacaoDeUsuario() {

		usuarioService.cadastrarUsuario(new Usuario(0L, "Maria da Silva", "https://i.imgur.com/h4t8loa.jpg",
				"maria_silva@email.com.br", "12345678"));

		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, "Maria da Silva",
				"https://i.imgur.com/h4t8loa.jpg", "maria_silva@email.com.br", "12345678"));

		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao,
				Usuario.class);

		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}

	@Test
	@Order(3) // será o primeiro a ser executado
	@DisplayName("Alterar um usuário")
	public void deveAtualizarUmUsuario() {

		Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(0L,
				"Juliana Andrews","https://i.imgur.com/h4t8loa.jpg", "juliana_andrews@email.com.br", "juliana123"));

		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(), 
				"Juliana Andrews Ramos","https://i.imgur.com/h4t8loa.jpg", "juliana_ramos@email.com.br", "juliana123");

		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);

		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
		assertEquals(usuarioUpdate.getFoto(), resposta.getBody().getFoto());
		assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
	}
	
	@Test
	@Order(4) // será o primeiro a ser executado
	@DisplayName("Listar todos os usuários")
	public void deveMostrarTodosUsuarios() {
		
		usuarioService.cadastrarUsuario(new Usuario(
				0L, "Sabrina Sanches",
						"https://i.imgur.com/h4t8loa.jpg", "sabrina_sanches@email.com.br", "sabrina123"));
		
		usuarioService.cadastrarUsuario(new Usuario(
				0L, "Ricardo Marques",
						"https://i.imgur.com/h4t8loa.jpg", "ricardo_marques@email.com.br", "ricardo123"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/all", HttpMethod.GET,null, String.class);
		
		assertEquals(HttpStatus.OK,resposta.getStatusCode());
		
		
		
	}

}
