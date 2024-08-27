package com.damas.objetos;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.damas.objetos.patternStrategy.MovimentoDamaStrategy;

public class DamaVermelha implements Peca {
  public static final Icon ICONE = new ImageIcon(DamaVermelha.class.getResource("/resources/dama_vermelha.png"));
  Casa casa;

  public DamaVermelha(Casa casa) {
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
    MovimentoDamaStrategy movimentoValido = new MovimentoDamaStrategy();
    return movimentoValido.isMovimentoValido(origem, destino);
  }
}
