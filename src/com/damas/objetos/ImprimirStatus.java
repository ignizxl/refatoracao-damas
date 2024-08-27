package com.damas.objetos;

public class ImprimirStatus {
  private final Jogador jogadorUm;
  private final Jogador jogadorDois;
  private final int vez;
  private final int jogada;
  private final int jogadasSemComerPecas;
  private final Casa casaBloqueadaOrigem;

  public ImprimirStatus(Jogador jogadorUm, Jogador jogadorDois, int vez, int jogada, int jogadasSemComerPecas, Casa casaBloqueadaOrigem) {
      this.jogadorUm = jogadorUm;
      this.jogadorDois = jogadorDois;
      this.vez = vez;
      this.jogada = jogada;
      this.jogadasSemComerPecas = jogadasSemComerPecas;
      this.casaBloqueadaOrigem = casaBloqueadaOrigem;
  }

  @Override
  public String toString() {
      StringBuilder retorno = new StringBuilder();
      
      retorno.append("Vez: ");
      if (vez == 1) {
          retorno.append(jogadorUm.getNome()).append("\n");
      } else if (vez == 2) {
          retorno.append(jogadorDois.getNome()).append("\n");
      }

      retorno.append("Nº de jogadas: ").append(jogada).append("\n");
      retorno.append("Jogadas sem comer peça: ").append(jogadasSemComerPecas).append("\n\n");

      retorno.append("Informações do(a) jogador(a) ").append(jogadorUm.getNome()).append("\n");
      retorno.append("Pontos: ").append(jogadorUm.getPontos()).append("\n");
      retorno.append("Nº de peças restantes: ").append(12 - jogadorDois.getPontos()).append("\n\n");

      retorno.append("Informações do(a) jogador(a) ").append(jogadorDois.getNome()).append("\n");
      retorno.append("Pontos: ").append(jogadorDois.getPontos()).append("\n");
      retorno.append("Nº de peças restantes: ").append(12 - jogadorUm.getPontos()).append("\n");

      if (casaBloqueadaOrigem != null) {
          retorno.append("\nMova a peça na casa ").append(casaBloqueadaOrigem.getX()).append(":").append(casaBloqueadaOrigem.getY()).append("!");
      }

      return retorno.toString();
  }
}

