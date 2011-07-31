package flowbuilder.code;

public enum KeyWord {

    PROCESS("process"),
    
    BEGIN("begin"),
    
    IF("if"),
    
    ELSE("else"),
    
    LOOP("loop"),

    FORK("fork"),

    TASK("task"),
    
    CALL("call"),
    
    END("end"),

    NOTE("@note");
    
    private String description;
    
    private KeyWord(String description) {
        this.description = description;
    }
    
    public String description() {
        return description;
    }
    
}
