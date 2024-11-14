package com.teste.testeUnitarios.entidades;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Locacao {

	private Usuario usuario;
	private Filme filme;
	private Date dataLocacao;
	private Date dataRetorno;
	private Double valor;

}