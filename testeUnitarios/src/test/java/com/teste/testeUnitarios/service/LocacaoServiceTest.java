package com.teste.testeUnitarios.service;

import com.teste.testeUnitarios.entidades.Filme;
import com.teste.testeUnitarios.entidades.Locacao;
import com.teste.testeUnitarios.entidades.Usuario;
import com.teste.testeUnitarios.expections.FilmeSemEstoqueException;
import com.teste.testeUnitarios.expections.LocadoraException;
import com.teste.testeUnitarios.servicos.LocacaoService;
import com.teste.testeUnitarios.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.fail;

@Slf4j
public class LocacaoServiceTest {

    private LocacaoService service;

    private static int contador = 0;

    private List<Filme> filmes;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        service = new LocacaoService();
        contador++;
        log.info(String.valueOf(contador));
        filmes = new ArrayList<>();
    }

    @After
    public void tearDown() {
        log.info("depois");
    }

    @BeforeClass
    public static void setupClass() {
        log.info("Antes no incio");
    }

    @AfterClass
    public static void tearDownClass() {
        log.info("depois no fim");
    }


    @Test
    public void teste() throws Exception {
        //cenario

        Usuario user = new Usuario("Renan");
        filmes.add(new Filme("teste", 2, 10.00));
        //ação
        Locacao locacao = service.alugarFilme(user, filmes);
        //verificação

        Assert.assertEquals(10, locacao.getValor(), 0.0);
        Assert.assertThat(locacao.getValor(), CoreMatchers.is(10.0));
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));


        error.checkThat(locacao.getValor(), CoreMatchers.is(10.0));
        error.checkThat(locacao.getValor(), CoreMatchers.is(10.0));
    }


    @Test(expected = FilmeSemEstoqueException.class)
    public void test_FilmeSemEstoqueElegante() throws Exception {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario user = new Usuario("Renan");
        filmes.add(new Filme("teste", 0, 10.00));
        //ação
        service.alugarFilme(user, filmes);
        //verificação
    }

    @Test
    public void test_FilmeSemEstoqueRobusta() {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario user = new Usuario("Renan");
        filmes.add(new Filme("teste", 0, 10.00));
        //ação
        try {
            service.alugarFilme(user, filmes);
            fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Filme Esgotado");
        }
        //verificação
    }

    @Test
    public void test_FilmeSemEstoque3() throws Exception {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario user = new Usuario("Renan");
        filmes.add(new Filme("teste", 0, 10.00));

        exception.expect(FilmeSemEstoqueException.class);
        exception.expectMessage("Filme Esgotado");
        //ação
        service.alugarFilme(user, filmes);
        //verificação
    }

    @Test
    public void test_UsuarioVazio() {
        //cenario
        LocacaoService service = new LocacaoService();
        //Usuario user = null;
        filmes.add(new Filme("teste", 2, 10.00));
        try {
            service.alugarFilme(null, filmes);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Usuario Vazio");
        }
    }


    @Test
    public void test_FilmeVazio() throws Exception {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario user = new Usuario("Renan");
        //Filme film = new Filme("teste", 0, 10.00);

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme Vazio");
        //ação
        service.alugarFilme(user, null);
        //verificação
    }

}
