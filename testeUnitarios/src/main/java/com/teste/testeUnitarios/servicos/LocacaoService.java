package com.teste.testeUnitarios.servicos;

import com.teste.testeUnitarios.entidades.Filme;
import com.teste.testeUnitarios.entidades.Locacao;
import com.teste.testeUnitarios.entidades.Usuario;
import com.teste.testeUnitarios.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;


import java.util.Date;

@Slf4j
public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, Filme filme) {
		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filme.getPrecoLocacao());

		log.info("passei");
		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

	public static void main(String[] args) {
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario user = new Usuario("Renan");
		Filme film = new Filme("teste", 2, 10.00);
		//ação
		Locacao locacao = service.alugarFilme(user, film);
		//verificação

		log.info(String.valueOf(locacao.getValor() == 10));
		log.info(String.valueOf(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date())));
		log.info(String.valueOf(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1))));
	}
}