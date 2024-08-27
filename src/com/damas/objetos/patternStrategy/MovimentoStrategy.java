package com.damas.objetos.patternStrategy;

import com.damas.objetos.Casa;

public interface MovimentoStrategy {
  boolean isMovimentoValido(Casa origem, Casa destino);
}
