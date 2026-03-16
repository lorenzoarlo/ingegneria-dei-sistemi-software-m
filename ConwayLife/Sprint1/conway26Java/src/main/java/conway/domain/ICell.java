package main.java.conway.domain;

/**
 * Interfaccia che definisce le proprietà e il comportamento di una singola
 * cella.
 */
public interface ICell {

    /**
     * Primitiva: la cella è un'entità dotata della capacità di modificare il
     * proprio stato interno in base a un valore booleano.
     */
    public void setStatus(boolean status);

    /**
     * Primitiva: la cella può trovarsi in uno stato specifico, ovvero essere
     * viva o morta.
     */
    public boolean isAlive();

    /**
     * Non primitiva: una cella può invertire il proprio stato attuale, passando
     * da viva a morta e viceversa.
     */
    public void switchCellState();
}
