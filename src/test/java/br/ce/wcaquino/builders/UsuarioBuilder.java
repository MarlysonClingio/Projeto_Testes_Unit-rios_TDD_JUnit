package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Usuario;

public class UsuarioBuilder {
	
	private Usuario usuario;
	
	private UsuarioBuilder( ) {}
	
	
	public static UsuarioBuilder usuario() {
		UsuarioBuilder builder = new UsuarioBuilder();
		builder.usuario = new Usuario();
		builder.usuario.setNome("Marlyson Clingio");
		return builder;
	}
	
	public Usuario usuarioAtual() {
		return usuario;
	}
}
