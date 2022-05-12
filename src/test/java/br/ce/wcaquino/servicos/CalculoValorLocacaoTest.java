package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.filme;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

	private Usuario usuario;

	@InjectMocks
	private LocacaoService service;

	@Parameter
	public List<Filme> filmes;
	@Parameter(value = 1)
	public Double valorLocacao;
	@Parameter(value = 2)
	public String cenario;

	@Mock
	LocacaoDAO dao;
	@Mock
	SPCService spc;

	@Before
	public void setup() {
		// Cenário
		MockitoAnnotations.openMocks(this);
		usuario = new Usuario("Marlyson Clingio");
	}

	private static Filme filme1 = filme().filmeAtual();
	private static Filme filme2 = filme().filmeAtual();
	private static Filme filme3 = filme().filmeAtual();
	private static Filme filme4 = filme().filmeAtual();
	private static Filme filme5 = filme().filmeAtual();
	private static Filme filme6 = filme().filmeAtual();
	private static Filme filme7 = filme().filmeAtual();

	@Parameters(name = "{2}")
	public static Collection<Object[]> getParametros() {
		return Arrays.asList(new Object[][] { { Arrays.asList(filme1, filme2), 8.0, "2 Filmes: Sem desconto" },
				{ Arrays.asList(filme1, filme2, filme3), 11.0, "3 Filmes: 25%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 Filmes: 50%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 Filmes: 75%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0, "6 Filmes: 100%" },
				{ Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0,
						"7 Filmes: Sem desconto" } });
	}

	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
		// Ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Verificação
		assertEquals(valorLocacao, locacao.getValor(), 0.01);
	}

}
