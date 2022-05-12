package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {

	private Filme filme;

	private FilmeBuilder( ) {}

	public static FilmeBuilder filme() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		builder.filme.setNome("Sempre tem mais um");
		builder.filme.setEstoque(1);
		builder.filme.setPrecoLocacao(4.0);
		return builder;
	}
	
	public FilmeBuilder semEstoque() {
		filme.setEstoque(0);
		return this;
	}

	public Filme filmeAtual() {
		return filme;
	}

}
