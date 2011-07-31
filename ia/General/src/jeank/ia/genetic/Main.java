package jeank.ia.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		op1();
	}

	private static void op1() {
		Scanner sc = new Scanner(System.in);

		List<String> cromossomesStr = new ArrayList<String>();
		List<Integer> cromossomesInt = new ArrayList<Integer>();
		List<Double> capability = new ArrayList<Double>();
		List<Double> probability = new ArrayList<Double>();

		double maxCap = 0.0;
		System.out.println("Add cromossomes in binary");

		String x = sc.next();
		while (!x.equals("exit")) {
			cromossomesStr.add(x);
			int i = Integer.parseInt(x, 2);
			cromossomesInt.add(i);
			double cap = i / (4 + (double) Math.abs(i - 20));
			capability.add(cap);
			maxCap += cap;
			x = sc.next();
		}

		double maxProb = 0.0;
		for (Double a : capability) {
			double b = a.doubleValue();
			b = b / maxCap;
			probability.add(b);
			maxProb += b;
		}

		List<String> strs = new ArrayList<String>();
		strs.add("Indivídou K");
		strs.add("Cromossomo");
		strs.add("Valor x");
		strs.add("Aptidão bruta f(x)");
		strs.add("Pr[K] seleção");
		strs.add("Intervalo N.A.");

		int max = normalizeStrings(strs, 0);

		System.out.println(strs);
		strs.clear();
		double interval = 0.0;
		for (int i = 0; i < cromossomesInt.size(); i++) {
			strs.add("" + i);
			strs.add(cromossomesStr.get(i));
			strs.add("" + cromossomesInt.get(i));
			strs.add("" + capability.get(i));
			strs.add("" + probability.get(i));
			strs.add("[" + interval + "; " + (interval += probability.get(i)) + ')');
			normalizeStrings(strs, max);
			System.out.println(strs);
			strs.clear();
		}

	}

	private static int normalizeStrings(List<String> h, int m) {
		int max = m;
		if (max == 0) {
			// ignora a ultima sendo que não vai ter colunas depois
			for (int i = 0; i < h.size() - 1; i++) {
				String s = h.get(i);
				max = Math.max(max, s.length() + 1);
			}
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < h.size() - 1; i++) {
			String s = h.get(i);
			if (s.length() > max) {
				s = s.substring(0, max);
			}
			sb.append(s);
			for (int j = s.length(); j < max; j++) {
				sb.append(' ');
			}
			h.set(i, sb.toString());
			sb.delete(0, sb.length());
		}
		return max;
	}
}
