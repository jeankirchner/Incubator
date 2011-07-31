package jeank.ia.games;

import java.util.List;

public interface IValuedState {

	boolean isLeaf();

	int getRating();

	List<IValuedState> getChildren();

}
