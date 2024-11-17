package com.teste.testeUnitarios.servicos;

import com.teste.testeUnitarios.entidades.Filme;
import com.teste.testeUnitarios.entidades.Locacao;
import com.teste.testeUnitarios.entidades.Usuario;
import com.teste.testeUnitarios.expections.FilmeSemEstoqueException;
import com.teste.testeUnitarios.expections.LocadoraException;
import com.teste.testeUnitarios.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Objects;

import static java.util.Objects.isNull;

@Slf4j
public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, Filme filme) throws Exception {

		if(isNull(usuario)){
			throw new LocadoraException("Usuario Vazio");
		}

		if(isNull(filme)){
			throw new LocadoraException("Filme Vazio");
		}

		if(filme.getEstoque() == 0){
			throw new FilmeSemEstoqueException("Filme Esgotado");
		}


		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filme.getPrecoLocacao());


		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}


}