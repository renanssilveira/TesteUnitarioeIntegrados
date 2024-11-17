package com.teste.testeUnitarios.service;

import com.teste.testeUnitarios.entidades.Filme;
import com.teste.testeUnitarios.entidades.Locacao;
import com.teste.testeUnitarios.entidades.Usuario;
import com.teste.testeUnitarios.servicos.LocacaoService;
import com.teste.testeUnitarios.utils.DataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.util.Date;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();


    @Test
    public void teste() {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario user = new Usuario("Renan");
        Filme film = new Filme("teste", 2, 10.00);
        //ação
        Locacao locacao = service.alugarFilme(user, film);
        //verificação

        Assert.assertEquals(10, locacao.getValor(), 0.0);
        Assert.assertThat(locacao.getValor(), CoreMatchers.is(10.0));
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));



        error.checkThat(locacao.getValor(), CoreMatchers.is(13.0));
        error.checkThat(locacao.getValor(), CoreMatchers.is(11.0));

    }
}
