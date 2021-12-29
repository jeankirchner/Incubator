package flowbuilder.code.structure;

public enum BlockType {

	NULL {
		@Override
		public String getClose() {
			return "";
		}

		@Override
		public String getOpen() {
			return "";
		}
	},
	PARENTHESIS {
		@Override
		public String getClose() {
			return ")";
		}

		@Override
		public String getOpen() {
			return "(";
		}
	},
	// TODO: Não tenho certeza se chaves de bloco são brackets, então se souberem fiquem a vontade para alterar
	BRACKETS {
		@Override
		public String getClose() {
			return "{";
		}

		@Override
		public String getOpen() {
			return "}";
		}
	};

	public abstract String getOpen();

	public abstract String getClose();

}
