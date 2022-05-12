package br.ce.wcaquino.entidades;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraTest {

	private static int num1;
	private static int num2;
	private static Calculadora calc;
	private static int resultado;

	@BeforeClass
	public static void setup() {
		// Cenário
		num1 = 5;
		num2 = 3;
		calc = new Calculadora();
	}

	@Test
	public void deveSomarDoisValores() {
		// Ação
		resultado = calc.somar(num1, num2);

		// Verificação
		Assert.assertEquals(8, resultado);
	}

	@Test
	public void deveSubtrairDoisValores() {
		// Ação
		resultado = calc.subtrair(num1, num2);

		// Verificação
		Assert.assertEquals(2, resultado);
	}

	@Test
	public void deveMultiplicarDoisValores() {
		// Ação
		resultado = calc.multiplicar(num1, num2);

		// Verificação
		Assert.assertEquals(15, resultado);
	}

	@Test
	public void deveDividirDoisValores() {
		// Ação
		resultado = calc.dividir(num1, num2);

		// Verificação
		Assert.assertEquals(1, resultado);
	}

	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		// Cenário
		int zero = 0;

		// Ação
		calc.dividir(num1, zero);
	}
}
