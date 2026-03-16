package main.java.conway.domain;

/**
 * Interfaccia che definisce le operazioni per la gestione di una griglia.
 */
public interface IGrid {

    /**
     * Primitiva: l'entità griglia è composta da un numero specifico di righe.
     */
    public int getRowsNum();

    /**
     * Primitiva: l'entità griglia è composta da un numero specifico di colonne.
     */
    public int getColsNum();

    /**
     * Non primitiva: la griglia è composta da entità celle, le quali possiedono
     * un proprio stato (vero o falso) che può essere modificato.
     */
    public void setCellValue(int x, int y, boolean state);

    /**
     * Primitiva: l'entità griglia è composta da entità celle.
     */
    public ICell getCell(int x, int y);

    /**
     * Non primitiva: la griglia è composta da entità celle che possono trovarsi
     * in stato vivo (vero) o morto (falso).
     */
    public boolean getCellValue(int x, int y);

    /**
     * Non primitiva: la griglia può essere riportata al suo stato iniziale.
     */
    public void reset();
}
