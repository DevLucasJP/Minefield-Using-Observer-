package meu_campo_minado.backend;

import java.util.Scanner;

public class Play {

	public static void main(String[] args) {

		Field field = new Field();
		Scanner scan = new Scanner(System.in);
		Restrictions r = new Restrictions();

		String userLine;
		String userColumn;
		String userFlag;

		boolean endGame = false;

		do {
			System.out.println(field);

			do {
				System.out.printf("Escolha a linha que deseja jogar: ");
				userLine = scan.nextLine();
			} while (!r.validNumber(userLine));

			do {
				System.out.printf("Escolha a coluna que deseja jogar: ");
				userColumn = scan.nextLine();
			} while (!r.validNumber(userColumn));

			do {
				System.out.println("Deseja marcar/desmarcar como bandeira(S/N)?");
				userFlag = scan.nextLine();
			} while (!r.validFlagOption(userFlag));

			endGame = field.openField(Integer.parseInt(userLine), Integer.parseInt(userColumn), userFlag);
		} while (!endGame);

		System.out.println(field);
		System.out.println("Fim de jogo!");

		scan.close();
	}
}
