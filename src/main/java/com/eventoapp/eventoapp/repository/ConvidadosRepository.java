package com.eventoapp.eventoapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.eventoapp.eventoapp.models.Convidados;
import com.eventoapp.eventoapp.models.Evento;

public interface ConvidadosRepository extends CrudRepository<Convidados, String>{

	Iterable<Convidados>  findByEvento(Evento evento);
	Convidados findByDocumento(String documento);
}
