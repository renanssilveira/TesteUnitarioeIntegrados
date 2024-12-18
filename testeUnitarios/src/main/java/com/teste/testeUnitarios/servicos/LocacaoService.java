package com.teste.testeUnitarios.servicos;

import com.teste.testeUnitarios.Dao.LocacaoDao;
import com.teste.testeUnitarios.entidades.Filme;
import com.teste.testeUnitarios.entidades.Locacao;
import com.teste.testeUnitarios.entidades.Usuario;
import com.teste.testeUnitarios.expections.FilmeSemEstoqueException;
import com.teste.testeUnitarios.expections.LocadoraException;
import com.teste.testeUnitarios.utils.DataUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;

@Slf4j
public class LocacaoService {

    private LocacaoDao locacaoDao;

    private SpcService spcService;

    private EmailService emailService;

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

        if (spcService.possuiNegativacvao(usuario)) {
            throw new LocadoraException("Usuario Negativado");
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
        if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
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

    public void notificarAtrasos() {
        List<Locacao> locacaos = locacaoDao.ObterLocacoesPendentes();
        locacaos.forEach(locacao -> {
            if(locacao.getDataRetorno().before(new Date())) {
                emailService.notificarAtraso(locacao.getUsuario());
            }
        });
    }

    public void prorogarLocacao(Locacao locacao, int dias){
        Locacao renovacaoLocacao = new Locacao();
        renovacaoLocacao.setDataLocacao(new Date());
        renovacaoLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
        renovacaoLocacao.setUsuario(locacao.getUsuario());
        renovacaoLocacao.setFilmes(locacao.getFilmes());
        renovacaoLocacao.setValor(locacao.getValor() * dias);
        locacaoDao.salvar(renovacaoLocacao);
    }
}