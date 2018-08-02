package com.bsettle.tis100clone.parse;

public class Token {
	public static final int EPSILON = 0;
    public static final int REGISTER = 1;
    public static final int PORT = 2;
    public static final int COMMENT = 3;
	public static final int LABEL = 4;
	public static final int NUMBER = 5;
	public static final int COMMAND = 6;
	public static final int MOVE = 7;
	public static final int ADD_SUB = 8;
	public static final int JUMP_LABEL = 9;
	public static final int JUMP_RELATIVE = 10;
	public static final int VARIABLE = 11;

	public final int location;
	public final int token;
	public final String sequence;

	public Token(int location, int token, String sequence) {
		super();
		this.location = location;
		this.token = token;
		this.sequence = sequence;
	}

    @Override
    public boolean equals(Object obj) {
	    if (!(obj instanceof Token)){
	        return false;
        }
        Token t = (Token) obj;
        return t.location == location && t.token == token && t.sequence.equals(sequence);
    }
}
