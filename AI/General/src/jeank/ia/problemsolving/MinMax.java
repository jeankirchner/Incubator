package jeank.ia.problemsolving;

import java.util.Collections;
import java.util.List;

public class MinMax {

	private static IValuedState MIN = new IValuedState() {

		@Override
		public boolean isLeaf() {
			return true;
		}

		@Override
		public int getRating() {
			return Integer.MIN_VALUE;
		}

		@Override
		public List<IValuedState> getChildren() {
			return Collections.emptyList();
		}

	};

	private static IValuedState MAX = new IValuedState() {

		@Override
		public boolean isLeaf() {
			return true;
		}

		@Override
		public int getRating() {
			return Integer.MAX_VALUE;
		}

		@Override
		public List<IValuedState> getChildren() {
			return Collections.emptyList();
		}

	};

	private static IValuedState minValue(IValuedState s, IValuedState alfa, IValuedState beta) {
		if (s.isLeaf()) {
			return s;
		}
		for (IValuedState m : s.getChildren()) {
			IValuedState x = maxValue(m, alfa, beta);
			beta = beta.getRating() < x.getRating() ? beta : x;
			if (alfa.getRating() >= beta.getRating()) {
				return alfa;
			}
		}
		return beta;
	}

	private static IValuedState maxValue(IValuedState s, IValuedState alfa, IValuedState beta) {
		if (s.isLeaf()) {
			return s;
		}
		for (IValuedState m : s.getChildren()) {
			IValuedState x = minValue(m, alfa, beta);
			alfa = alfa.getRating() > x.getRating() ? alfa : x;
			if (alfa.getRating() >= beta.getRating()) {
				return beta;
			}
		}
		return alfa;
	}

	public static IValuedState minMaxDecision(IValuedState rootState) {
		return maxValue(rootState, MIN, MAX);
	}
}
