package com.teste.testeUnitarios.service;

import com.teste.testeUnitarios.servicos.Calculadora;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;


@Slf4j
public class CalculadoraMockTest {

	@Mock
	private Calculadora calcMock;

	@Spy
	private Calculadora calcSpy;

	@Before
	public void setup(){
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void devoMostrarDiferencaEntreMockSpy(){
		Mockito.when(calcMock.somar(1,3)).thenReturn(3);
		Mockito.when(calcSpy.somar(1,3)).thenReturn(3);
		log.info(String.valueOf(calcMock.somar(1, 2)));
		log.info(String.valueOf(calcSpy.somar(1, 2)));
	}


	@Test
	public void teste(){
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
		Mockito.when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5);
		
		Assert.assertEquals(5, calc.somar(134345, -234));
		System.out.println(argCapt.getAllValues());
	}
}
