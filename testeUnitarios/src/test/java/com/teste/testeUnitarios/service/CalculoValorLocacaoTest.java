package com.teste.testeUnitarios.service;

import com.teste.testeUnitarios.Dao.LocacaoDao;
import com.teste.testeUnitarios.entidades.Filme;
import com.teste.testeUnitarios.entidades.Locacao;
import com.teste.testeUnitarios.entidades.Usuario;
import com.teste.testeUnitarios.expections.FilmeSemEstoqueException;
import com.teste.testeUnitarios.servicos.LocacaoService;
import com.teste.testeUnitarios.servicos.SpcService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    @InjectMocks
    private LocacaoService service;
    @Mock
    private LocacaoDao locacaoDao;

    @Mock
    private SpcService spcService;

    @Parameter
    public List<Filme> filmes;

    @Parameter(value = 1)
    public Double valorLocacao;

    @Parameter(value = 2)
    public String cenario;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private static Filme filme1 = new Filme("Filme 1", 2, 10.00);
    private static Filme filme2 = new Filme("Filme 2", 2, 10.00);
    private static Filme filme3 = new Filme("Filme 3", 2, 10.00);
    private static Filme filme4 = new Filme("Filme 4", 2, 10.00);
    private static Filme filme5 = new Filme("Filme 5", 2, 10.00);
    private static Filme filme6 = new Filme("Filme 6", 2, 10.00);
    private static Filme filme7 = new Filme("Filme 7", 2, 10.00);

    @Parameters(name = "{2}")
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList(filme1, filme2), 20.0, "2 Filmes: Sem Desconto"},
                {Arrays.asList(filme1, filme2, filme3), 27.5, "3 Filmes: 25%"},
                {Arrays.asList(filme1, filme2, filme3, filme4), 30.625, "4 Filmes: 50%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5), 29.21875, "5 Filmes: 75%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 25.0390625, "6 Filmes: 100%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 33.154296875, "7 Filmes: Sem Desconto"}
        });
    }

    @Test
    public void deveCalcularValorLocacaoConsiderandoDescontos() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(valorLocacao));
    }
}
