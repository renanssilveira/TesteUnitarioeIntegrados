package com.teste.testeUnitarios.servicos;

import com.teste.testeUnitarios.LocacaoDao;
import com.teste.testeUnitarios.entidades.Filme;
import com.teste.testeUnitarios.entidades.Locacao;
import com.teste.testeUnitarios.entidades.Usuario;
import com.teste.testeUnitarios.expections.FilmeSemEstoqueException;
import com.teste.testeUnitarios.expections.LocadoraException;
import com.teste.testeUnitarios.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;

@Slf4j
public class LocacaoService {

    @Autowired
    private LocacaoDao locacaoDao;

    public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception {
        Locacao locacao = new Locacao();
        locacao.setValor(0.0);
        if (isNull(usuario)) {
            throw new LocadoraException("Usuario Vazio");
        }

        if (isNull(filmes) || filmes.isEmpty()) {
            throw new LocadoraException("Filme Vazio");
        }

        if (filmes.stream().anyMatch(filme -> filme.getEstoque().equals(0))) {
            throw new FilmeSemEstoqueException("Filme Esgotado");
        }

        implementaDesconto(filmes);

        filmes.forEach(filme ->
                {
                    locacao.setFilme(filme);
                    locacao.setUsuario(usuario);
                    locacao.setDataLocacao(new Date());
                    locacao.setValor(locacao.getValor() + filme.getPrecoLocacao());
                }
        );


        //Entrega no dia seguinte
        Date dataEntrega = new Date();
        dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
        if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)){
            dataEntrega = DataUtils.adicionarDias(dataEntrega, 1);
        }
        locacao.setDataRetorno(dataEntrega);
        //Salvando a locacao...

        locacaoDao.salvar(locacao);

        return locacao;
    }


    private void implementaDesconto(List<Filme> filmes) {
        AtomicInteger i = new AtomicInteger();
        filmes.forEach(filme -> {
            if (i.get() == 2) {
                filme.setPrecoLocacao(filme.getPrecoLocacao() * 0.75);
            } else if (i.get() == 3) {
                filme.setPrecoLocacao(filme.getPrecoLocacao() * 0.50);
            } else if (i.get() == 4) {
                filme.setPrecoLocacao(filme.getPrecoLocacao() * 0.25);
            } else if (i.get() == 5) {
                filme.setPrecoLocacao(0.0);
            }
            i.getAndIncrement();
        });
    }

}