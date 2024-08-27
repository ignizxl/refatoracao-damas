package com.damas.objetos.patternStrategy;

import com.damas.objetos.Casa;

public class MovimentoPedraStrategy implements MovimentoStrategy {

  @Override
  public boolean isMovimentoValido(Casa origem, Casa destino) {

    // SENTIDO UNITÁRIO E DISTANCIA X E Y DA CASA ATUAL ATÉ A CASA DE DESTINO
    int distanciaX = Math.abs(destino.getX() - origem.getX());
    int distanciaY = Math.abs(destino.getY() - origem.getY());

    if ((distanciaX == 0) || (distanciaY == 0))
      return false;

    // REGRA DE MOVIMENTO NO CASO DA DISTÂNCIA SER DE 2 CASAS (MOVIMENTO DE COMER
    // PEÇA)
    if ((distanciaX <= 2 || distanciaY <= 2) && (distanciaX == distanciaY)) {
      return true;
    }

    return false;
  }
}
