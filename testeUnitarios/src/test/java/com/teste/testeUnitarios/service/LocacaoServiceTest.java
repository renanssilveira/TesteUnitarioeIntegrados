package com.teste.testeUnitarios.service;

import com.teste.testeUnitarios.entidades.Filme;
import com.teste.testeUnitarios.entidades.Locacao;
import com.teste.testeUnitarios.entidades.Usuario;
import com.teste.testeUnitarios.utils.DataUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class LocacaoService {

    @Test
    public void teste() {
        //cenario
        com.teste.testeUnitarios.servicos.LocacaoService service = new com.teste.testeUnitarios.servicos.LocacaoService();
        Usuario user = new Usuario("Renan");
        Filme film = new Filme("teste", 2, 10.00);
        //ação
        Locacao locacao = service.alugarFilme(user, film);
        //verificação

        Assert.assertEquals(10, (double) locacao.getValor(), 0.0);
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
    }
}
