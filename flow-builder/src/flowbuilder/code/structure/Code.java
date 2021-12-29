package flowbuilder.code.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Representa um pedaço de código.
 * 
 * @author Anderson Dorow
 */
public abstract class Code extends FBCNode {

	private final List<Note>	notes	= new ArrayList<Note>();

	public void addNote(Note note) {
		notes.add(note);
	}

	public void addNotes(Collection<Note> notes) {
		this.notes.addAll(notes);
	}

	@Override
	public void accept(FBCVisitor visitor) {
		for (Note n : notes) {
			n.accept(visitor);
		}
	}
	
	public List<Note> getNotes() {
		return notes;
	}

}
