package com.blogpessoal.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity //transforma a classe em entidade tabela
@Table(name  = "tb_postagens") //cria tabela
public class Postagem {

	@Id  //primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY)	//auto increment o.remove exclui tudo de uma categoria e o que se relaciona com ela
	private Long id ;
	
	@NotBlank //preenchimento obrigatorio
	@Size(min = 5, max = 50)  //tamanho do texto
	private String titulo;
	
	@NotBlank
	@Size(min = 5, max = 1000)
	private String texto;
	
	@UpdateTimestamp
	private LocalDateTime data;

	@ManyToOne
	@JsonIgnoreProperties("postagem")//ele para o loop quando chega em postagem
	private Tema tema;
	
	@ManyToOne
	@JsonIgnoreProperties("postagem")
	private Usuario usuario;
	
	//get and set
			
	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
				
	
}
