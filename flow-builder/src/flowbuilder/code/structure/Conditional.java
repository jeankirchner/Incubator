package flowbuilder.code.structure;

public class Conditional extends Code {

	private final If	_if;
	private final Else	_else;

	public Conditional(If _if, Else _else) {
		if (_if == null) {
			throw new NullPointerException("If can't be null");
		}
		this._if = _if;
		this._else = _else;
	}

	@Override
	public void accept(FBCVisitor visitor) {
		super.accept(visitor);
		_if.accept(visitor);
		if (_else != null) {
			_else.accept(visitor);
		}
	}

	public If get_if() {
		return _if;
	}
	
	public Else get_else() {
		return _else;
	}
	
}
