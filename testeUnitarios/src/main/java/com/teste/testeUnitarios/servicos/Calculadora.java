package com.teste.testeUnitarios.servicos;


import com.teste.testeUnitarios.expections.NaoPodeDividirPorZeroException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Calculadora {

	public int somar(int a, int b) {
		return a + b;
	}

	public int subtrair(int a, int b) {
		return a - b;
	}

	public int divide(int a, int b) throws NaoPodeDividirPorZeroException {
		if(b == 0) {
			throw new NaoPodeDividirPorZeroException();
		}
		return a / b;
	}

	public void imprimi(){
		log.info("PAssei");
	}


}
