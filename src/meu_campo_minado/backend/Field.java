package meu_campo_minado.backend;

import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public class Field implements SquareObserver {

	private Square[][] field = new Square[9][9];
	private Square[][] hiddenField = new Square[9][9];

	private Predicate<Square> isMined = im -> im.getMine();
	private Predicate<Square> haveNumber = hn -> !hn.getMine() && !hn.getText().equals(" ");
	private Predicate<Square> blankSpace = bs -> bs.getText().equals(" ");

	public Field() {

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {

				field[i][j] = new Square();
				field[i][j].registerObserver(this);
				hiddenField[i][j] = new Square();
			}
		}

		generateMines();
	}

	private void generateMines() {

		Random random = new Random();

		for (int i = 0; i < 10; i++) {

			int line = random.nextInt(9);
			int column = random.nextInt(9);

			while (isMined.test(hiddenField[line][column])) {
				line = random.nextInt(9);
				column = random.nextInt(9);
			}

			hiddenField[line][column].setText("*");
			hiddenField[line][column].setMine();
			generateWarnings(line, column);
		}
	}

	private boolean findNext(int line, int column, int i, int j) {

		BinaryOperator<Integer> absolute = (a, b) -> Math.abs(a - b);
		int absLine = absolute.apply(line, i);
		int absColumn = absolute.apply(column, j);

		boolean diagonalNext = absLine == 1 && absColumn == 1;
		boolean crossNext = (absLine + absColumn) == 1;
		boolean next = diagonalNext || crossNext;

		return next;
	}

	private void generateWarnings(int line, int column) {

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {

				if (findNext(line, column, i, j) && !isMined.test(hiddenField[i][j])) {
					if (hiddenField[i][j].getText().equals("1")) {
						hiddenField[i][j].setText("2");
					} else if (hiddenField[i][j].getText().equals("2")) {
						hiddenField[i][j].setText("3");
					} else if (hiddenField[i][j].getText().equals("3")) {
						hiddenField[i][j].setText("4");
					} else {
						hiddenField[i][j].setText("1");
					}
				}

				if (hiddenField[i][j].getText().equals("?")) {
					hiddenField[i][j].setText(" ");
				}
			}
		}
	}

	public boolean openField(int line, int column, String flag) {

		if (field[line][column].getOpen()) {
			System.out.println("O campo escolhido já está aberto!");

			return false;
		} else if (field[line][column].getFlag()) {
			field[line][column].setFlag();
			field[line][column].setText("?");
			field[line][column].notifyObsevers(FieldEvents.UNMARK);

			return false;
		} else {
			if (flag.equalsIgnoreCase("s")) {
				field[line][column].setFlag();
				field[line][column].setText("F");
				field[line][column].notifyObsevers(FieldEvents.MARK);
			} else {
				field[line][column].setOpen();

				if (!isMined.test(hiddenField[line][column])) {
					if (haveNumber.test(hiddenField[line][column])) {
						field[line][column].setText(hiddenField[line][column].getText());
						field[line][column].setOpen();
						field[line][column].notifyObsevers(FieldEvents.OPEN);
					} else if (blankSpace.test(hiddenField[line][column])) {
						field[line][column].setText(hiddenField[line][column].getText());
						field[line][column].setOpen();
						field[line][column].notifyObsevers(FieldEvents.OPEN);

						for (int i = 0; i < 9; i++) {
							for (int j = 0; j < 9; j++) {

								if (findNext(line, column, i, j) && blankSpace.test(hiddenField[i][j])
										&& !field[i][j].getOpen()) {
									field[i][j].setText(hiddenField[i][j].getText());
									field[i][j].setOpen();

									triggerChain(i, j);
								} else if (findNext(line, column, i, j) && haveNumber.test(hiddenField[i][j])
										&& !field[i][j].getOpen()) {
									field[i][j].setText(hiddenField[i][j].getText());
									field[i][j].setOpen();
								}
							}
						}
					}

					return false;
				} else {
					for (int i = 0; i < 9; i++) {
						for (int j = 0; j < 9; j++) {

							if (isMined.test(hiddenField[i][j])) {
								field[i][j].setText(hiddenField[i][j].getText());
								field[i][j].setOpen();
								field[line][column].notifyObsevers(FieldEvents.EXPLOSION);
							}
						}
					}

					return true;
				}
			}

			if (winCondition()) {
				return true;
			} else {
				return false;
			}
		}
	}

	private void triggerChain(int line, int column) {

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {

				if (findNext(line, column, i, j) && blankSpace.test(hiddenField[i][j]) && !field[i][j].getOpen()) {
					field[i][j].setText(hiddenField[i][j].getText());
					field[i][j].setOpen();

					triggerChain(i, j);
				} else if (findNext(line, column, i, j) && haveNumber.test(hiddenField[i][j])
						&& !field[i][j].getOpen()) {
					field[i][j].setText(hiddenField[i][j].getText());
					field[i][j].setOpen();
				}
			}
		}
	}

	public boolean winCondition() {

		int winCon = 0;

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {

				if (field[i][j].getOpen()) {
					winCon++;
				}
			}
		}

		if (winCon == 71) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {

		StringBuilder fieldView = new StringBuilder();

		for (int i = 0; i < 9; i++) {

			fieldView.append(i + " ");

			for (int j = 0; j < 9; j++) {
				fieldView.append(field[i][j]);

				if (j < 8) {
					fieldView.append(" ");
				}
			}

			if (i < 8) {
				fieldView.append("\n");
			} else {
				fieldView.append("\n  0 1 2 3 4 5 6 7 8");
			}
		}

		return fieldView.toString();
	}

	public void update(Square square, FieldEvents event) {

		if (event == FieldEvents.OPEN) {
			System.out.println("Campo aberto!");
		} else if (event == FieldEvents.MARK) {
			System.out.println("Campo marcado como bandeira!");
		} else if (event == FieldEvents.UNMARK) {
			System.out.println("Campo desmarcado!");
		} else if (event == FieldEvents.EXPLOSION) {
			System.out.println("Campo explodiu!");
		}
	}
}
