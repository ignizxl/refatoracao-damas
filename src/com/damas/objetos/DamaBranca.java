package com.damas.objetos;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import com.damas.objetos.patternStrategy.MovimentoDamaStrategy;

public class DamaBranca implements Peca {
  public static final Icon ICONE = new ImageIcon(DamaBranca.class.getResource("/resources/dama_branca.png"));
  Casa casa;

  public DamaBranca(Casa casa) {
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
