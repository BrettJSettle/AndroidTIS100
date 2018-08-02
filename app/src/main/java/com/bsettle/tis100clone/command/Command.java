package com.bsettle.tis100clone.command;

import com.bsettle.tis100clone.parse.Parser;
import com.bsettle.tis100clone.parse.ParserException;
import com.bsettle.tis100clone.parse.Tokenizer;

public class Command {

	private static Tokenizer TOKENIZER = Tokenizer.getTokenizer();
	private static Parser PARSER = new Parser();

	private String text;
	private Expression expression;
	private ParserException error = null;

	public Command(String text) {
		setCommand(text);
	}

	public void setCommand(String text) {
		text = text.toUpperCase();
		error = null;
		this.text = text;
		try {
			expression = parse(text);
		} catch (ParserException e) {
			error = e;
		}

	}
	
	public static Expression parse(String s) throws ParserException {
		TOKENIZER.tokenize(s);
		
		if (!TOKENIZER.getTokens().isEmpty()) {
			return PARSER.parse(TOKENIZER.getTokens());
		}
		return null;
	}

	public String getText() {
		return text;
	}

	public Expression getExpression() {
		return expression;
	}

	public ParserException getError() {
		return error;
	}

	public boolean isEmpty() {
		return expression == null || expression instanceof CommentExpression || (expression instanceof LabeledExpression
				&& ((LabeledExpression) expression).getExpression() == null);
	}
}
