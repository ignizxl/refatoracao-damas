package com.damas.objetos.patternStrategy;

import com.damas.objetos.Casa;

public class MovimentoDamaStrategy implements MovimentoStrategy {

  @Override
  public boolean isMovimentoValido(Casa origem, Casa destino) {
    int distanciaX = Math.abs((destino.getX() - origem.getX()));
    int distanciaY = Math.abs((destino.getY() - origem.getY()));

    if (distanciaX == distanciaY)
      return true;

    return false;
  }
}
