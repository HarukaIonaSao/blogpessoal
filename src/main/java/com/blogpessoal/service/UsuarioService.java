package com.blogpessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.blogpessoal.model.Usuario;
import com.blogpessoal.model.UsuarioLogin;
import com.blogpessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;// acesso a interface repository

	// public pois podemos acessar de outro lugar -Optional pode ou não ter um valor
	public Optional<Usuario>/* model usuario */ cadastrarUsuario/* metodo */(Usuario usuario)/* objeto criado */ {

		// acessa o Mysql e compara se já tem um usuario no db e cadastra se não tiver
		if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())/* acessa a model */ // verifica se tem
																									// usuario
																									// cadastrado
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " Usuário já cadastrado!", null);// responde que
																										// usuario já
																										// existe se der
																										// true

		usuario.setSenha(criptografarSenha(usuario.getSenha()));// se não tiver usuario, cadastra a seha e acessa o
																// criptografar senha

		return Optional.of(usuarioRepository.save(usuario));// salva usuario e retorna o novo usuario para o
															// Optional(pega o que tem na Model e passa para a
															// repository)
	}

	public Optional<Usuario> atualizarUsuario(Usuario usuario) {

		// Se o usuario existe, ele segue o procedimento, senão, vai para o trhow
		if (usuarioRepository.findById(usuario.getId()).isPresent()) {
			Optional<Usuario> buscaUsuario = usuarioRepository. // cria um optional vazio e coloca na variável
					findByUsuario(usuario.getUsuario());
			if (buscaUsuario.isPresent()) {
				if (buscaUsuario.get().getId() != usuario.getId())// verifica se o email é de outra pessoa
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
			}
			usuario.setSenha(criptografarSenha(usuario.getSenha()));
			return Optional.of(usuarioRepository.save(usuario));
		}

		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!", null);

	}

	public Optional<UsuarioLogin> logarUsuario(Optional<UsuarioLogin> usuarioLogin) {
		Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());
		if (usuario.isPresent()) {
			if (compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get()
						.setToken(gerarBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));
				usuarioLogin.get().setSenha(usuario.get().getSenha());
				return usuarioLogin;
			}
		}
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos!", null);
	}

	/* * usada para referenciar senha no controller BCrypt pega o criptografar
	 * password e cria um objeto BCryptEncoder
	 */
	private String criptografarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();// novo método que retorna a senha
		String senhaEncoder = encoder.encode(senha);// senha que estou digitando + a senha criptografada
		return senhaEncoder;// criptografar a senha= "bagunçar tudo"
	}

	private boolean compararSenhas(String senhaDigitada, String senhaBanco) {// ele arruma a senha "bagunçada" para manipular
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(senhaDigitada, senhaBanco);// pega a senha digitada com a senha do bd
	}

	private String gerarBasicToken(String email, String password) {// método responsável por gerar o token
		String estrutura = email + ":" + password;// cria uma variável que cria uma estrutura que "bagunça "tudo e gera
													// o token
		byte[] estruturaBase64 = Base64.encodeBase64(estrutura.getBytes(Charset.forName("US-ASCII")));// e junta com o
																										// email e a
																										// senha
		return "Basic " + new String(estruturaBase64);// tem que deixar o espaço após o Bacic
	}

}
