package jeank.ia;

import java.util.Arrays;
import java.util.Scanner;

import jeank.ia.rna.Perceptron;

public class RNAMain {

	private static void generateInput() {
		double average = 7.0;
		double rule1 = 0.3;
		double rule2 = 0.7;
		double p1 = 0.0;
		double p2 = 0.0;

		int howMany = (int) (10 / 1.0);

		int[] expected = new int[howMany * howMany];

		System.out.println("{");
		for (int i = 0; i < howMany; i++) {
			p1 = (10 / howMany) * i;
			for (int j = 0; j < howMany; j++) {
				p2 = (10 / howMany) * j;
				System.out.print(" { " + p1 + "," + p2 + " },");
				expected[howMany * i + j] = (p1 * rule1 + p2 * rule2) >= average ? 1 : -1;
			}
			System.out.println();
		}
		System.out.println("}");

		System.out.println(Arrays.toString(expected));

	}

	public static void main(String[] args) {

		// generateInput();

		double[][] input = { { 0.0, 0.0 }, { 0.0, 1.0 }, { 0.0, 2.0 }, { 0.0, 3.0 }, { 0.0, 4.0 }, { 0.0, 5.0 }, { 0.0, 6.0 }, { 0.0, 7.0 }, { 0.0, 8.0 }, { 0.0, 9.0 }, { 1.0, 0.0 }, { 1.0, 1.0 }, { 1.0, 2.0 }, { 1.0, 3.0 }, { 1.0, 4.0 }, { 1.0, 5.0 }, { 1.0, 6.0 }, { 1.0, 7.0 }, { 1.0, 8.0 }, { 1.0, 9.0 }, { 2.0, 0.0 }, { 2.0, 1.0 }, { 2.0, 2.0 }, { 2.0, 3.0 }, { 2.0, 4.0 }, { 2.0, 5.0 }, { 2.0, 6.0 }, { 2.0, 7.0 }, { 2.0, 8.0 }, { 2.0, 9.0 }, { 3.0, 0.0 }, { 3.0, 1.0 }, { 3.0, 2.0 }, { 3.0, 3.0 }, { 3.0, 4.0 }, { 3.0, 5.0 }, { 3.0, 6.0 }, { 3.0, 7.0 }, { 3.0, 8.0 }, { 3.0, 9.0 }, { 4.0, 0.0 }, { 4.0, 1.0 }, { 4.0, 2.0 }, { 4.0, 3.0 }, { 4.0, 4.0 }, { 4.0, 5.0 }, { 4.0, 6.0 }, { 4.0, 7.0 }, { 4.0, 8.0 }, { 4.0, 9.0 }, { 5.0, 0.0 }, { 5.0, 1.0 }, { 5.0, 2.0 }, { 5.0, 3.0 }, { 5.0, 4.0 }, { 5.0, 5.0 }, { 5.0, 6.0 }, { 5.0, 7.0 }, { 5.0, 8.0 }, { 5.0, 9.0 }, { 6.0, 0.0 }, { 6.0, 1.0 }, { 6.0, 2.0 }, { 6.0, 3.0 }, { 6.0, 4.0 }, { 6.0, 5.0 }, { 6.0, 6.0 }, { 6.0, 7.0 }, { 6.0, 8.0 },
				{ 6.0, 9.0 }, { 7.0, 0.0 }, { 7.0, 1.0 }, { 7.0, 2.0 }, { 7.0, 3.0 }, { 7.0, 4.0 }, { 7.0, 5.0 }, { 7.0, 6.0 }, { 7.0, 7.0 }, { 7.0, 8.0 }, { 7.0, 9.0 }, { 8.0, 0.0 }, { 8.0, 1.0 }, { 8.0, 2.0 }, { 8.0, 3.0 }, { 8.0, 4.0 }, { 8.0, 5.0 }, { 8.0, 6.0 }, { 8.0, 7.0 }, { 8.0, 8.0 }, { 8.0, 9.0 }, { 9.0, 0.0 }, { 9.0, 1.0 }, { 9.0, 2.0 }, { 9.0, 3.0 }, { 9.0, 4.0 }, { 9.0, 5.0 }, { 9.0, 6.0 }, { 9.0, 7.0 }, { 9.0, 8.0 }, { 9.0, 9.0 }, };
		int[] expected = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1 };
		// pesos perfeitos
		// double[] pounds = { -7.0, 0.3, 0.7 };

		// pesos bons
		double[] pounds = { -6.9, 0.2, 0.8 };

		// pesos nulos
		// double[] pounds = { 0.0, 0.0, 0.0 };

		Scanner sc = new Scanner(System.in);
		System.out.println("Quão inteligente você gostaría, digite um número de treinos: ");
		int trainings = sc.nextInt();

		Perceptron p = new Perceptron(2, input, expected, pounds);
		for (int i = 0; i < trainings; i++) {
			p.calculateAnEpic();
		}

		System.out.println("Ok, agora nosso neurônio está treinado, faça as suas consultas:");

		while (true) {
			System.out.println("\tDigite a nota das provas 1 e 2, e o resultado esperado:");
			double p1 = sc.nextDouble();
			double p2 = sc.nextDouble();
			int x = sc.nextInt();

			p.setInfoEnabled(false);
			p.addInput(x, p1, p2);
			for (int i = 1; i < trainings; i++) {
				p.calculateAnEpic();
			}
			p.setInfoEnabled(true);
			p.calculateAnEpic();

			System.out.println("Veja na coluna 'y' o resultado, no último registro acima.");
			System.out.println("Continuar? digite sim ou nao");

			if (!sc.next().equalsIgnoreCase("sim")) {
				break;
			}

		}

		System.out.println("Agradecemos pela sua consulta");

	}

	private static void clearScreen() {
		for (int i = 0; i < 200; i++) {
			System.out.println();
		}
	}

}
