package com.teste.testeUnitarios.expections;

public class FilmeSemEstoqueException extends Exception{

    public FilmeSemEstoqueException(String filmeEsgotado) {
        super(filmeEsgotado);
    }
}
