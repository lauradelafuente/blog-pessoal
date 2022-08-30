package com.generation.blogpessoal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.generation.blogpessoal.model.Postagem;

@Repository //indica que a Interface é do tipo repositório, responsável pela interação com o Banco de dados através dos Métodos padrão.
public interface PostagemRepository extends JpaRepository <Postagem, Long > { //2 parametros: Classe Postagem (gerou a tb_postagens) e O Long representa a Chave Primária (id) 

}
