package com.teste.testeUnitarios.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//Controla a ordem dos teste
public class OrdemTest {

    private static int contador = 0;

    @Test
    public void inicia(){
        contador++;
      log.info(String.valueOf(contador));
    }

    @Test
    public void verifica(){
        Assert.assertEquals(contador, 1);
    }
}
