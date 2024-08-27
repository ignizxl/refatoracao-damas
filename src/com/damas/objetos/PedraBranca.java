package com.damas.objetos;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import com.damas.objetos.patternStrategy.MovimentoPedraStrategy;

public class PedraBranca implements Peca {
  public static final Icon ICONE = new ImageIcon(PedraBranca.class.getResource("/resources/pedra_branca.png"));

  Casa casa;

  public PedraBranca(Casa casa) {
    this.casa = casa;
    casa.colocarPeca(this); // Associa a peça à casa
  }

  @Override
  public void mover(Casa origem, Casa destino) {
    origem.removerPeca();
    destino.colocarPeca(this);
    origem = destino;
  }

  @Override
  public Peca getPeca() {
    return this;
  }

  @Override
  public Icon getIcone() {
    return ICONE;
  }

  @Override
  public boolean isMovimentoValido(Casa origem, Casa destino) {
    MovimentoPedraStrategy movimentoStrategy = new MovimentoPedraStrategy();
    return movimentoStrategy.isMovimentoValido(origem, destino);
  }

}