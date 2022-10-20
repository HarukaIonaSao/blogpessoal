package com.blogpessoal.repository;

import java.util.List; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.blogpessoal.model.Postagem;

@Repository// extends - dentro de repository ele acessa os métodos do Jpa e tudo o que ele já tem, como o findAll, FindById
/*Nós extendemos a JPA porque dentro dela tem todos os métodos criados, aqueles que usamos no banco de dados, e podemos também criar novos métodos específico, além dos padrões
 * */
public interface PostagemRepository  extends JpaRepository<Postagem , Long> {//jpa manipula os dados do bd e manda para o usuario
     //Jpa já fornece os métodos padrão, get,put,delete,post,put
	//Querymethod 
	public List <Postagem> findAllByTituloContainingIgnoreCase(@Param("titulo") String titulo);
	//ignore case ignora maiúscula a minúscula
	// pode ser tmb 
	//public Optional<Postagem> findByPostagem(String postagem)
	
}
