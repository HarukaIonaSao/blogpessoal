package com.blogpessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogpessoal.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{ /*
Repository é interface pq vamos injetar dependências,como o Jpa. Na Jpa podemos criar os métodos personalizados para interagir com banco tbm...
	tipo o findAllByTitulo...
	ela te da apoio a criar os acordos dos métodos de busca personalizada do banco de dados
*/
    
    public Optional<Usuario> findByUsuario(String usuario); //O optional seleciona um item específico, como o Like%

    //método de busca por específica por nome
    public List <Usuario> findAllByNomeContainingIgnoreCase(String nome);
    
    
}