package com.bsettle.tis100clone.parse;

public class ParserException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7314899553275702440L;
	private Token token;

	public ParserException(Token token, String message) {
		super(message);
		this.token = token;
	}

	public Token getToken() {
		return token;
	}
}
