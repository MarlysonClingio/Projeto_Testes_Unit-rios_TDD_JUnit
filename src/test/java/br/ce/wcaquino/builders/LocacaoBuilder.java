package br.ce.wcaquino.builders;

import static br.ce.wcaquino.builders.FilmeBuilder.filme;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;

import java.util.Arrays;
import java.util.Date;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoBuilder {
	private Locacao elemento;

	private LocacaoBuilder() {
	}

	public static LocacaoBuilder locacao() {
		LocacaoBuilder builder = new LocacaoBuilder();
		inicializarDadosPadroes(builder);
		return builder;
	}

	public static void inicializarDadosPadroes(LocacaoBuilder builder) {
		builder.elemento = new Locacao();
		Locacao elemento = builder.elemento;

		elemento.setUsuario(UsuarioBuilder.usuario().usuarioAtual());
		elemento.setFilmes(Arrays.asList(filme().filmeAtual()));
		elemento.setDataLocacao(new Date());
		elemento.setDataRetorno(DataUtils.obterDataComDiferencaDias(1));
		elemento.setValor(4.0);
	}

	public LocacaoBuilder usuario(Usuario param) {
		elemento.setUsuario(param);
		return this;
	}

	public LocacaoBuilder listaFilmes(Filme... params) {
		elemento.setFilmes(Arrays.asList(params));
		return this;
	}

	public LocacaoBuilder dataLocacao(Date param) {
		elemento.setDataLocacao(param);
		return this;
	}

	public LocacaoBuilder dataRetorno(Date param) {
		elemento.setDataRetorno(param);
		return this;
	}

	public LocacaoBuilder valor(Double param) {
		elemento.setValor(param);
		return this;
	}

	public Locacao locacaoAtual() {
		return elemento;
	}

	public LocacaoBuilder atrasada() {
		elemento.setDataLocacao(obterDataComDiferencaDias(-4));
		elemento.setDataRetorno(obterDataComDiferencaDias(-2));
		return this;
	}
}
