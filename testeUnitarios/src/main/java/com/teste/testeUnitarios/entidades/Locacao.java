package com.teste.testeUnitarios.entidades;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Locacao {

	private Usuario usuario;
	private Filme filme;
	private List<Filme> filmes;
	private Date dataLocacao;
	private Date dataRetorno;
	private Double valor;

}