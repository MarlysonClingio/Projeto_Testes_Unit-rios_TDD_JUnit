package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.filme;
import static br.ce.wcaquino.builders.LocacaoBuilder.locacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.usuario;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;


public class LocacaoServiceTest {

	private Usuario usuario;
	private Usuario usuario2;
	private List<Filme> filmes;

	@InjectMocks
	private LocacaoService service;

	@Mock
	private LocacaoDAO dao;
	@Mock
	private SPCService spc;
	@Mock
	private EmailService email;

	@Rule
	public ErrorCollector error = new ErrorCollector();
	@SuppressWarnings("deprecation")
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		// Cenário
		MockitoAnnotations.openMocks(this);
		usuario = usuario().usuarioAtual();
		filmes = Arrays.asList(filme().filmeAtual());
	}

	@Test
	public void deveAlugarFilmeComSucesso() throws FilmeSemEstoqueException, LocadoraException {

		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// Ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		locacao.setUsuario(usuario);
		locacao.setFilmes(filmes);

		// Verificação
		assertEquals("Marlyson Clingio", locacao.getUsuario().getNome());
		for (Filme filme : locacao.getFilmes()) {
			assertEquals("Sempre tem mais um", filme.getNome());
		}
		assertEquals(4.0, locacao.getValor(), 0.01);
		assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
		assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));

	}

	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws FilmeSemEstoqueException, LocadoraException {
		// Cenário
		filmes = Arrays.asList(filme().semEstoque().filmeAtual());

		// Ação
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		// Ação
		try {
			service.alugarFilme(null, filmes);
			fail();
		} catch (LocadoraException e) {
			assertEquals("Usuario Vazio", e.getMessage());
		}
	}

	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException {
		// Ação
		try {
			service.alugarFilme(usuario, null);
			fail();
		} catch (LocadoraException e) {
			assertEquals("Filme Vazio", e.getMessage());
		}
	}

	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		// Cenário
		usuario2 = usuario().usuarioAtual();
		when(spc.possuiNegativacao(usuario)).thenReturn(true);

		// Ação
		try {
			service.alugarFilme(usuario, filmes);
			// Verificação
			fail();
		} catch (LocadoraException e) {
			assertEquals("Usuario Negativado", e.getMessage());
		}

		verify(spc).possuiNegativacao(usuario);
	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() throws FilmeSemEstoqueException, LocadoraException {
		// Cenário
		usuario2 = new Usuario("Marlyson Almeida");
		List<Locacao> locacoes = Arrays.asList(locacao().usuario(usuario).atrasada().locacaoAtual(),
				locacao().usuario(usuario2).locacaoAtual());
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

		// Ação
		service.notificarAtrasos();

		// Verificação
		verify(email, atLeastOnce()).notificarAtraso(usuario);
		verify(email, never()).notificarAtraso(usuario2);
		Mockito.verifyNoMoreInteractions(email);
	}

	@Test
	public void deveTratarErroNoSPC() throws Exception {

		// Cenário
		when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha na consulta do SPC"));

		// Verificação
		exception.expect(LocadoraException.class);
		exception.expectMessage("SPC fora do ar, tente novamente");

		// Ação
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void deveProrrogarUmaLocacao() throws Exception {
		// Cenário
		Locacao locacao = locacao().locacaoAtual();

		// Ação
		service.prorrogarLocacao(locacao, 3);

		// Verificação
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetornada = argCapt.getValue();

		assertEquals(locacaoRetornada.getValor(), (Double) 12.0);
		assertTrue(isMesmaData(locacaoRetornada.getDataLocacao(), new Date()));
		assertTrue(isMesmaData(locacaoRetornada.getDataRetorno(), obterDataComDiferencaDias(3)));
	}
}
