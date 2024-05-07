package meu_campo_minado.backend;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Restrictions {

	Field field = new Field();

	public boolean validNumber(String user) {

		Pattern p = Pattern.compile("^[0-9]+$");
		Matcher verify = p.matcher(user);

		if (verify.matches()) {
			int number = Integer.parseInt(user);

			if (number >= 0 && number <= 8) {
				return true;
			} else {
				System.out.println("Escolha um número válido!");
				return false;
			}
		} else {
			System.out.println("Escolha um número válido!");
			return false;
		}
	}

	public boolean validFlagOption(String user) {

		if (user.equalsIgnoreCase("s") || user.equalsIgnoreCase("n")) {
			return true;
		} else {
			System.out.println("Digite apenas 'S' ou 'N'!");
			return false;
		}
	}
}
