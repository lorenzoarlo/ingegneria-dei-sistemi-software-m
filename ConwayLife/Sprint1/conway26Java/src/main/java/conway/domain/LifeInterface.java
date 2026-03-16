package main.java.conway.domain;

/**
 * Interfaccia che definisce il motore logico e l'evoluzione del gioco.
 */
public interface LifeInterface {

    /**
     * Primitiva: calcola l'evoluzione dello stato complessivo alla generazione successiva.
     */
    void nextGeneration();

    /**
     * Non primitiva: restituisce lo stato booleano (vivo o morto) di una cella specifica.
     */
    boolean isAlive(int row, int col);

    /**
     * Non primitiva: imposta lo stato di una cella agendo sulla struttura sottostante.
     */
    void setCell(int row, int col, boolean alive);

    /**
     * Non primitiva: restituisce l'oggetto Cella corrispondente alle coordinate specificate.
     */
    ICell getCell(int x, int y);

    /**
     * Primitiva: restituisce l'entità griglia associata alla logica di gioco.
     */
    IGrid getGrid();

    /**
     * Non primitiva: riporta le griglie allo stato iniziale (o fornisce una rappresentazione 
     * di reset del sistema).
     */
    void resetGrids();
}