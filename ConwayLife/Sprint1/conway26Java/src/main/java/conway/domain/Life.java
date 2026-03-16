package main.java.conway.domain;

import unibo.basicomm23.utils.CommUtils;

public class Life implements LifeInterface {
	private final int rows;
	private final int cols;

	// Due matrici distinte
	private Grid gridA;
	private Grid gridB;

	public static LifeInterface CreateLife(int nr, int nc) {
		return new Life(nr, nc);
		// possono essere lette da un file di configurazione
	}

	// Costruttore che crea una griglia vuota di dimensioni specifiche
	public Life(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.gridA = new Grid(rows, cols);
		this.gridB = new Grid(rows, cols);
	}

	// Calcola la generazione successiva applicando le 4 regole di Conway
	public void nextGeneration() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				int neighbors = countNeighbors(r, c);
				// Applichiamo le regole leggendo da gridA e scrivendo in gridB
				applyLifeRules(r, c, neighbors);
			}
		} // for
		swapGrids();
	}

	protected void applyLifeRules(int row, int col, int neighbors) {
		// AIT complessità minore di applyRules
		boolean nextV;
		boolean isAlive = gridA.getCell(row, col).isAlive();
		// apply Life rules
		if (isAlive) {
			nextV = (neighbors == 2 || neighbors == 3); // sopravvive o muore
		} else { // cella dead
			nextV = neighbors == 3; // rivive
		}
		gridB.setCellValue(row, col, nextV);
	}

	protected void swapGrids() {
		// Scambiamo i riferimenti: ciò che era 'next' diventa 'current'
		Grid temp = gridA;
		gridA = gridB;
		gridB = temp;
		// Non abbiamo creato nuovi oggetti, abbiamo solo spostato i puntatori
		// CommUtils.outyellow("Life | grids swap done" );
	}

	/**
	 * Versione con complessità AIT minore meno caratteri servono per scrivere il
	 * codice (senza perdere logica), minore è la sua complessità algoritmica.
	 * 
	 * @param row Coordinata x (riga)
	 * @param col Coordinata y (colonna)
	 * @return Numero di vicini vivi (da 0 a 8)
	 */
	protected int countNeighbors(int row, int col) {
		int count = 0;
		/*
		 * La descrizione è più compressa di countNeighborsLive. Invece di dire
		 * "controlla sopra, controlla sotto, controlla a destra...", diciamo
		 * "controlla tutto ciò che sta tra -1 e +1".
		 * 
		 * Rimpiazza 8 blocchi logici distinti con un'unica regola iterativa. Nella
		 * teoria AIT, questa è una forma di compressione dei dati.
		 * 
		 */
		// Cicliamo da -1 a +1 rispetto alla posizione centrale
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {

				// Saltiamo il caso 0,0 (è la cella stessa, non un vicino)
				if (i == 0 && j == 0)
					continue;

				int neighborRow = row + i;
				int neighborCol = col + j;

				// Verifichiamo che i vicini siano dentro i confini della griglia 5x5
				if (neighborRow >= 0 && neighborRow < rows && neighborCol >= 0 && neighborCol < cols) {

					if (gridA.getCell(neighborRow, neighborCol).isAlive()) {
						count++;
					}
				}
			}
		}
		return count;
	}

	@Override
	public ICell getCell(int r, int c) {
		return gridA.getCell(r, c);
	}

	@Override
	public void resetGrids() {
		gridA.reset();
		gridB.reset();
	}

	// Metodi di utilità per i test
	@Override
	public void setCell(int r, int c, boolean state) {
		gridA.setCellValue(r, c, state);
	}

	@Override
	public Grid getGrid() {
		return gridA;
	}

	@Override
	public boolean isAlive(int row, int col) {
		return gridA.getCellValue(row, col);
	}
}