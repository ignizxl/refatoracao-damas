package com.damas.objetos;

import java.util.ArrayList;

/**
 * Armazena o tabuleiro e responsavel por posicionar as pecas.
 * 
 * @author Alan Moraes &lt;alan@ci.ufpb.br&gt;
 * @author Leonardo Villeth &lt;lvilleth@cc.ci.ufpb.br&gt;
 * @author João Victor da S. Cirilo {@link joao.cirilo@academico.ufpb.br}
 * @author José Alisson Rocha da Silva {@link jose.alisson2@academico.ufpb.br}
 */
public class Jogo {

    private Tabuleiro tabuleiro;
    private Jogador jogadorUm;
    private Jogador jogadorDois;
    private int vezAtual = 1; // é necessário iniciar com o valor o valor 1 ou 2
    private int jogadas = 0;
    private int jogadasSemComerPeca = 0;
    private ArrayList<Casa> pecasAComer;
    private Casa casaBloqueadaOrigem;

    public Jogo() {
        tabuleiro = new Tabuleiro();
        pecasAComer = new ArrayList<Casa>();
        jogadorUm = new Jogador("player branco");
        jogadorDois = new Jogador("player vermelho");
        
        vezAtual = 1;
        jogadas = 0;
        jogadasSemComerPeca = 0;
        casaBloqueadaOrigem = null;

        colocarPecasParteInferior(tabuleiro);
        colocarPecasParteSuperior(tabuleiro);
    }
    
    public void moverPeca(int origemX, int origemY, int destinoX, int destinoY) {
        Casa origem = tabuleiro.getCasa(origemX, origemY);
        Casa destino = tabuleiro.getCasa(destinoX, destinoY);
        Peca peca = origem.getPeca();

        if (casaBloqueadaOrigem == null) {
            if ((getVez() == 1 && (peca.getPeca()  instanceof PedraBranca || peca.getPeca() instanceof DamaBranca)) ||
                (getVez() == 2 && (peca.getPeca() instanceof PedraVermelha|| peca.getPeca() instanceof DamaVermelha))) {

                if (peca.isMovimentoValido(origem, destino)) {

                    if (simularMovimentoEValidar(origem, destino)) {                    

                        peca.mover(origem, destino);

                        if (pecasAComer.size() > 0) {
                            comerPecas();
                            if (deveContinuarJogando(destino)) {
                                casaBloqueadaOrigem = destino;
                            } else {
                                trocarDeVez();
                            }
                        } else {
                            jogadasSemComerPeca++;
                            trocarDeVez();
                        }

                        jogadas++;
                        if (podeTransformarParaDama(destino)) transformarPedraParaDama(destino);
                    }
                }
            }
        } else {
            if ((origem.equals(casaBloqueadaOrigem))) {
                if(simularMovimentoEValidar(origem, destino)) {
                    if (pecasAComer.size() != 0) {
                        casaBloqueadaOrigem = null;
                        moverPeca(origemX, origemY, destinoX, destinoY);
                    }
                }
            }
        }
    }

    private boolean simularMovimentoEValidar(Casa origem, Casa destino) {
        Peca peca = origem.getPeca();
        int casasComPecaSeguidas = 0;

        if (destino.getPeca() != null) return false;

        // SENTIDO DO MOVIMENTO E DISTÂNCIA DO MOVIMENTO
        int sentidoX = (destino.getX() - origem.getX());
        int sentidoY = (destino.getY() - origem.getY());
        int distanciaX = Math.abs(sentidoX); 
        int distanciaY = Math.abs(sentidoY);
        
        if ((distanciaX == 0) || (distanciaY == 0)) return false;

        sentidoX = sentidoX/distanciaX;
        sentidoY = sentidoY/distanciaY;

        // REGRA DE MOVIMENTO DAS PEDRAS NO TABULEIRO CASO A DISTÂNCIA ATÉ A CASA CLICADA SEJA DE 2 BLOCOS
        if ((distanciaX == 2 && distanciaY == 2) &&
            ((peca.getPeca() instanceof PedraBranca) || (peca.getPeca() instanceof PedraVermelha))) {

            Casa casa = tabuleiro.getCasa((destino.getX() - sentidoX), (destino.getY() - sentidoY));
            if (casa.getPeca() == null) return false;
        } else {

            // REGRA DE MOVIMENTO DAS PEDRAS NO TABULEIRO CASO A DISTÂNCIA ATÉ A CASA CLICADA SEJA DE 1 BLOCO
            if (peca.getPeca() instanceof PedraBranca) {
                if ((distanciaX == 1 || distanciaY == 1) && (distanciaX == distanciaY) && sentidoY == 1) {
                    return true;
                } else {
                    return false;
                }
            } else {
                // REGRA DE MOVIMENTO DAS PEDRAS VERMELHAS
                if (peca.getPeca() instanceof PedraVermelha) {
                    if ((distanciaX == 1 || distanciaY == 1) && (distanciaX == distanciaY) && sentidoY == -1) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }


        int i = origem.getX();
        int j = origem.getY();

        while (!((i == destino.getX()) || (j == destino.getY()))) {
            i += sentidoX;
            j += sentidoY;

            Casa alvo = tabuleiro.getCasa(i, j);
            Peca pecaAlvo = alvo.getPeca();

            if (!(pecaAlvo == null)) {
                casasComPecaSeguidas += 1;

                // VE SE TEM UMA PECA DO MESMO TIPO NO CAMNHO, CASO TENHA, RETORNA FALSE
                if ((peca.getPeca() instanceof PedraBranca || peca.getPeca() instanceof DamaBranca) && (pecaAlvo.getPeca() instanceof PedraBranca || pecaAlvo.getPeca() instanceof DamaBranca)) {
                    if (pecasAComer.size() > 0) pecasAComer.removeAll(pecasAComer);
                    return false;
                }

                if ((peca.getPeca() instanceof PedraVermelha || peca.getPeca() instanceof DamaVermelha) && (pecaAlvo.getPeca() instanceof PedraVermelha || pecaAlvo.getPeca() instanceof DamaVermelha)) {
                    if (pecasAComer.size() > 0) pecasAComer.removeAll(pecasAComer);
                    return false;
                }

            } else {

                // VE SE HÁ PEÇA PARA COMER NO CAMINHO E PASSAR A CASA À COLEÇÃO pecasAComer() PARA DEPOIS COME-LAS
                if (casasComPecaSeguidas == 1) {
                    Casa casa = tabuleiro.getCasa((alvo.getX() - sentidoX), (alvo.getY() - sentidoY));
                    pecasAComer.add(casa);
                }
                casasComPecaSeguidas = 0;
            }

            if (casasComPecaSeguidas == 2) {
                if (pecasAComer.size() > 0) pecasAComer.removeAll(pecasAComer);
                return false;
            }
        }
        return true;
    }

    private boolean percorrerEVerificar(Casa origem, int deltaX, int deltaY) {
        Peca peca = origem.getPeca();
        int x = origem.getX();
        int y = origem.getY();
        int pecasSeguidasNoCaminho = 0;

        
        // SE O TIPO FOR PEDRA
        if ((peca.getPeca() instanceof PedraBranca) || (peca.getPeca() instanceof PedraVermelha)) {
            
            x += deltaX;
            y += deltaY;
            
            try {

                Peca pecaAtual = tabuleiro.getCasa(x, y).getPeca();
                
                if (!( pecaAtual == null)) {

                    if (tabuleiro.getCasa((x + deltaX), (y + deltaY)).getPeca() != null) {
                        return false;
                    }

                    if (isPecaMesmaCor(peca, pecaAtual)) return false;

                    return true;
                }

            } catch (Exception e) {
                return false;
            }

        } else {

            while (!((x == -1 || x == 8) || (y == -1 || y == 8))) {
                x += deltaX;
                y += deltaY;
                
                try {
                    Peca pecaAtual = tabuleiro.getCasa(x, y).getPeca();
    
                    if (!( pecaAtual == null)) {
                    
                        pecasSeguidasNoCaminho += 1;
        
                        if (isPecaMesmaCor(peca, pecaAtual)) {
                            return false;
                        }
                    } else {
                        
                        if (pecasSeguidasNoCaminho == 1) {
                            return true;
                        }
        
                        if (pecasSeguidasNoCaminho == 2) {
                            return false;
                        }
                    }
                } catch (Exception e) {
                    return false;
                }
            }
        }

        return false;
    
    }


    private boolean isPecaMesmaCor(Peca peca, Peca pecaAtual) {
        return (peca instanceof PedraBranca && (pecaAtual instanceof PedraBranca || pecaAtual instanceof DamaBranca)) ||
               (peca instanceof PedraVermelha && (pecaAtual instanceof PedraVermelha || pecaAtual instanceof DamaVermelha));
    }
    

    private boolean deveContinuarJogando(Casa origem) {

        if (percorrerEVerificar(origem, Tabuleiro.X_ESQUERDA, Tabuleiro.Y_CIMA)) {
            return true;
        } else {
            
            if (percorrerEVerificar(origem, Tabuleiro.X_DIREITA, Tabuleiro.Y_CIMA)) {
                return true;
            } else {
                        
                if (percorrerEVerificar(origem, Tabuleiro.X_DIREITA, Tabuleiro.Y_BAIXO)) {
                    return true;
                } else {

                    if (percorrerEVerificar(origem, Tabuleiro.X_ESQUERDA, Tabuleiro.Y_BAIXO)) {
                        return true;
                    }                    
                }
            }

        }

        return false;
    } 

    private void comerPecas() {
        int pecasComidas = pecasAComer.size();

        if (getVez() == 1) jogadorUm.addPonto(pecasComidas);
        if (getVez() == 2) jogadorDois.addPonto(pecasComidas);

        for (Casa casa : pecasAComer) {
            casa.removerPeca();
        }

        pecasAComer.removeAll(pecasAComer);

        jogadasSemComerPeca = 0;
    }

    private boolean podeTransformarParaDama(Casa casa) {
        return ((casa.getPeca().getPeca() instanceof PedraBranca && casa.getY() == 7) || 
        (casa.getPeca().getPeca() instanceof PedraVermelha && casa.getY() == 0)) ? true : false;
    }

    public void transformarPedraParaDama(Casa casa) {
        Peca peca = casa.getPeca();

        if (peca.getPeca() instanceof PedraBranca) {
            DamaBranca dama = new DamaBranca(casa);
            peca = (DamaBranca) dama;
        } else {
            DamaVermelha dama = new DamaVermelha(casa);
            peca = (DamaVermelha) dama;
        }
    }

    public void colocarPecasParteInferior(Tabuleiro tabuleiro) {

        // CRIA E PÕE AS PEÇAS NA PARTE INFERIOR DO TABULEIRO
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 3; y++) {
                if((x % 2 == 0) && (y % 2 == 0)) {
                    Casa casa = tabuleiro.getCasa(x, y);
                    new PedraBranca(casa);
                }
                
                else if ((x % 2 != 0) && (y % 2 != 0)){
                    Casa casa = tabuleiro.getCasa(x, y);
                    new PedraBranca(casa);
                }
            }

        }
    }

    public void colocarPecasParteSuperior(Tabuleiro tabuleiro) {
        // CRIA E POE AS PEÇAS NA PARTE SUPERIOR DO TABULEIRO
        for (int x = 0; x < 8; x++) {
            for (int y = 5; y < 8; y++) {
                if ((x % 2 != 0) && (y % 2 != 0)) {
                    Casa casa = tabuleiro.getCasa(x, y);
                    new PedraVermelha(casa);
                }
                else if ((x % 2 == 0) && (y % 2 == 0)) {
                    Casa casa = tabuleiro.getCasa(x, y);
                    new PedraVermelha(casa);
                }
            }
        }
    }

    public void trocarDeVez() {
        vezAtual = vezAtual == 1 ? 2 : 1;
    }

    public int getGanhador() {
        if (jogadorUm.isVencedor()) {
            return 1;
        } else if (jogadorDois.isVencedor()) {
            return 2; 
        }
        return 0;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }
    
    public void setJogadorUm(Jogador jogador) {
        jogadorUm = jogador;
    }

    public void setJogadorDois(Jogador jogador) {
        jogadorDois = jogador;
    }

    public Jogador getJogadorUm() {
        return jogadorUm;
    }

    public Jogador getJogadorDois() {
        return jogadorDois;
    }

    public int getVez() {
        return vezAtual;
    }

    public int getJogadasSemComerPecas() {
        return jogadasSemComerPeca;
    }

    public int getJogada() {
        return jogadas;
    }

    public Casa getCasaBloqueada() {
        return casaBloqueadaOrigem;
    }

    @Override
    public String toString() {

        // String retorno = "Vez: ";
        // if (getVez() == 1) {
        // retorno += jogadorUm.getNome();
        // retorno += "\n";
        // } else if (getVez() == 2) {
        // retorno += jogadorDois.getNome();
        // retorno += "\n";
        // }

        // retorno += "Nº de jogadas: " + getJogada() + "\n";
        // retorno += "Jogadas sem comer peça: " + getJogadasSemComerPecas() + "\n";
        // retorno += "\n";
        // retorno += "Informações do(a) jogador(a) " + jogadorUm.getNome() + "\n";
        // retorno += "Pontos: " + jogadorUm.getPontos() + "\n";
        // retorno += "Nº de peças restantes: " + (12 - jogadorDois.getPontos()) + "\n";
        // retorno += "\n";
        // retorno += "Informações do(a) jogador(a) " + jogadorDois.getNome() + "\n";
        // retorno += "Pontos: " + jogadorDois.getPontos() + "\n";
        // retorno += "Nº de peças restantes: " + (12 - jogadorUm.getPontos()) + "\n";

        // if (casaBloqueadaOrigem != null) {
        // retorno += "\n";
        // retorno += "Mova a peça na casa " + casaBloqueadaOrigem.getX() + ":" +
        // casaBloqueadaOrigem.getY() + "!";
        // }

        // return retorno;
        // }
        ImprimirStatus status = new ImprimirStatus(
                jogadorUm,
                jogadorDois,
                vezAtual,
                jogadas,
                jogadasSemComerPeca,
                casaBloqueadaOrigem);
        return status.toString();
    }
}