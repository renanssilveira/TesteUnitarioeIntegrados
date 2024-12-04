package com.teste.testeUnitarios.service;

import builders.FilmeBuilder;
import com.teste.testeUnitarios.Dao.LocacaoDao;
import com.teste.testeUnitarios.entidades.Filme;
import com.teste.testeUnitarios.entidades.Locacao;
import com.teste.testeUnitarios.entidades.Usuario;
import com.teste.testeUnitarios.expections.FilmeSemEstoqueException;
import com.teste.testeUnitarios.expections.LocadoraException;
import com.teste.testeUnitarios.servicos.EmailService;
import com.teste.testeUnitarios.servicos.LocacaoService;
import com.teste.testeUnitarios.servicos.SpcService;
import com.teste.testeUnitarios.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.*;

import static builders.LocacaoBuilder.umLocacao;
import static builders.UsuarioBuilder.umUsuario;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@Slf4j
public class LocacaoServiceTest {


    @InjectMocks
    private LocacaoService service;

    @Mock
    private List<Filme> filmes;

    @Mock
    private Usuario usuario;

    @Mock
    private LocacaoDao locacaoDao;

    @Mock
    private SpcService spcService;

    @Mock
    private EmailService emailService;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();


    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        filmes = new ArrayList<>();
        usuario = umUsuario().agora();

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

        filmes.add(new Filme("teste", 2, 10.00));
        //ação
        Locacao locacao = service.alugarFilme(usuario, filmes);
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

        filmes.add(new Filme("teste", 0, 10.00));
        //ação
        service.alugarFilme(usuario, filmes);
        //verificação
    }

    @Test
    public void test_FilmeSemEstoqueRobusta() {
        //cenario

        filmes.add(new Filme("teste", 0, 10.00));
        //ação
        try {
            service.alugarFilme(usuario, filmes);
            fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Filme Esgotado");
        }
        //verificação
    }

    @Test
    public void test_FilmeSemEstoque3() throws Exception {
        //cenario

        filmes.add(new Filme("teste", 0, 10.00));

        exception.expect(FilmeSemEstoqueException.class);
        exception.expectMessage("Filme Esgotado");
        //ação
        service.alugarFilme(usuario, filmes);
        //verificação
    }

    @Test
    public void test_UsuarioVazio() {
        //cenario

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

        //Filme film = new Filme("teste", 0, 10.00);

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme Vazio");
        //ação
        service.alugarFilme(usuario, null);
        //verificação
    }

    @Test
    public void devePagar75no3Filme() throws Exception {
        //cenario
        filmes.add(new Filme("teste1", 2, 10.00));
        filmes.add(new Filme("teste2", 4, 10.00));
        filmes.add(new Filme("teste3", 5, 10.00));

        //ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

        log.info(String.valueOf(locacao));
        //verificação
        assertEquals(locacao.getValor(), 27.5, 0.2);
    }

    @Test
    public void devePagar50no4Filme() throws Exception {
        //cenario
        filmes.add(new Filme("teste1", 2, 10.00));
        filmes.add(new Filme("teste2", 4, 10.00));
        filmes.add(new Filme("teste3", 5, 10.00));
        filmes.add(new Filme("teste4", 5, 10.00));
        //ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificação
        assertEquals(locacao.getValor(), 32.5, 0.2);
    }

    @Test
    public void devePagar25no5Filme() throws Exception {
        //cenario
        filmes.add(new Filme("teste1", 2, 10.00));
        filmes.add(new Filme("teste2", 4, 10.00));
        filmes.add(new Filme("teste3", 5, 10.00));
        filmes.add(new Filme("teste4", 5, 10.00));
        filmes.add(new Filme("teste5", 5, 10.00));
        //ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificação
        assertEquals(locacao.getValor(), 35, 0.2);
    }

    @Test
    public void devePagar0no6Filme() throws Exception {
        //cenario
        filmes.add(new Filme("teste1", 2, 10.00));
        filmes.add(new Filme("teste2", 4, 10.00));
        filmes.add(new Filme("teste3", 5, 10.00));
        filmes.add(new Filme("teste4", 5, 10.00));
        filmes.add(new Filme("teste5", 5, 10.00));
        filmes.add(new Filme("teste6", 5, 10.00));
        //ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificação
        assertEquals(locacao.getValor(), 35, 0.2);
    }

    @Test
    @Ignore
    public void naoDeveDevolverFilmeDomingo() throws Exception {

        //cenario
        filmes.add(new Filme("teste1", 2, 10.00));
        filmes.add(new Filme("teste2", 4, 10.00));
        filmes.add(new Filme("teste3", 5, 10.00));
        filmes.add(new Filme("teste4", 5, 10.00));
        filmes.add(new Filme("teste5", 5, 10.00));
        filmes.add(new Filme("teste6", 5, 10.00));

        //ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificação
        Assert.assertTrue(DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY));
    }

    @Test
    public void naoAlugaFilmeNegativado() throws Exception {
        filmes.add(new Filme("teste1", 2, 10.00));

        Mockito.when(spcService.possuiNegativacvao(usuario)).thenReturn(true);
        exception.expect(LocadoraException.class);
        exception.expectMessage("Usuario Negativado");
        //ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //Verificação
        Mockito.verify(spcService).possuiNegativacvao(usuario);
    }

    @Test
    public void deveEnviarEmailLocacaoAtrasada() {
        //cenario
        filmes.add(new Filme("teste1", 2, 10.00));
        Usuario usuario1 = new Usuario("Jose");
        List<Locacao> locacaos = Arrays.asList(umLocacao()
                        .atrasado().comUsuario(usuario)
                        .agora(),
                umLocacao()
                        .atrasado().comUsuario(usuario)
                        .agora(),
                umLocacao().comUsuario(usuario1).agora(),
                umLocacao().comUsuario(usuario1).agora());
        Mockito.when(spcService.possuiNegativacvao(usuario)).thenReturn(false);
        Mockito.when(locacaoDao.ObterLocacoesPendentes()).thenReturn(locacaos);
        //ação
        service.notificarAtrasos();

        //Verificacao
        verify(emailService, Mockito.atLeastOnce()).notificarAtraso(Mockito.any(Usuario.class));
        verify(emailService, Mockito.atLeastOnce()).notificarAtraso(usuario);
        verify(emailService, Mockito.never()).notificarAtraso(usuario1);
        verifyNoMoreInteractions(emailService);
        Mockito.verifyNoInteractions(spcService);

    }

    @Test
    public void deveTratarErronoSPC() throws Exception {
        //cenario
        Usuario usuario1 = umUsuario().agora();
        List<Filme> filmes1 = Arrays.asList(FilmeBuilder.umFilme().agora());

        when(spcService.possuiNegativacvao(Mockito.any(Usuario.class))).thenThrow(new RuntimeException("Falha Maluca"));

        //Verificacao
        exception.expect(Exception.class);
        exception.expectMessage("Falha Maluca");


        //ação
        service.alugarFilme(usuario1, filmes1);

        //verificação

    }

    @Test
    public void deveValidarProrogacao(){
        //cenario
        Locacao locaca1 = umLocacao().agora();

        //ação
        service.prorogarLocacao(locaca1, 3);

        //Verificação
        ArgumentCaptor<Locacao> argumentCaptor = ArgumentCaptor.forClass(Locacao.class);
        Mockito.verify(locacaoDao).salvar(argumentCaptor.capture());
        Locacao locacaoRetorno = argumentCaptor.getValue();
        Double valor = locaca1.getValor() * 3;
        Date dataRetorno = DataUtils.obterDataComDiferencaDias(3);
        Assert.assertEquals(locacaoRetorno.getValor(),valor);

    }
}
