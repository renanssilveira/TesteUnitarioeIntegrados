package com.teste.testeUnitarios.Dao;

import com.teste.testeUnitarios.entidades.Locacao;

import java.util.List;

public interface LocacaoDao {

    public void salvar(Locacao locacao);

    List<Locacao> ObterLocacoesPendentes();
}
