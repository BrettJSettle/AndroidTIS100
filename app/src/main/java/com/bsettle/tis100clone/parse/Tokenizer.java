package com.bsettle.tis100clone.parse;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

	private class TokenInfo {
		private final Pattern regex;
		public final int token;

		private TokenInfo(Pattern regex, int token) {
			super();
			this.regex = regex;
			this.token = token;
		}
	}

	public static Tokenizer getTokenizer(){
        Tokenizer TOKENIZER = new Tokenizer();
		TOKENIZER.add("ACC|BAK|NIL", Token.REGISTER); // Register
		TOKENIZER.add("LEFT|UP|DOWN|RIGHT|ANY|LAST", Token.PORT); // Port
		TOKENIZER.add("#.*$", Token.COMMENT); // comment
        TOKENIZER.add("[a-zA-Z0-9_]*:", Token.LABEL); // label
        TOKENIZER.add("-?[0-9]+", Token.NUMBER); // integer number
        TOKENIZER.add("NOP|SWP|SAV|NEG", Token.COMMAND); // command
        TOKENIZER.add("MOV", Token.MOVE); // move
        TOKENIZER.add("ADD|SUB", Token.ADD_SUB); // add/subtract
        TOKENIZER.add("JMP|JEZ|JNZ|JGZ|JLZ", Token.JUMP_LABEL); // jump label
        TOKENIZER.add("JRO", Token.JUMP_RELATIVE); // jump relative
        TOKENIZER.add("[a-zA-Z][a-zA-Z0-9_]*", Token.VARIABLE); // variable
        return TOKENIZER;
    }

	private LinkedList<TokenInfo> tokenInfos;
	private LinkedList<Token> tokens;

	private Tokenizer() {
		tokenInfos = new LinkedList<TokenInfo>();
		tokens = new LinkedList<Token>();
	}

	private void add(String regex, int token) {
	    if (token == Token.LABEL){
            tokenInfos.add(new TokenInfo(Pattern.compile("^(" + regex + ")\\s*"), token));
        }else{
            tokenInfos.add(new TokenInfo(Pattern.compile("^(" + regex + ")(\\s+|$)"), token));
        }

	}

	public void tokenize(String str) {
		String s = str.trim();
		tokens.clear();
		int index = 0;
		while (!s.equals("")) {
			boolean match = false;
			for (TokenInfo info : tokenInfos) {
				Matcher m = info.regex.matcher(s);
				if (m.find()) {
					match = true;
					String tok = m.group().trim(); //TODO : how does trim handle in index calculation
					Token t = new Token(index + m.start(), info.token, tok);
					tokens.add(t);

					index += m.group().length();
					s = m.replaceFirst("").trim();
					break;
				}
			}
			if (!match)
				throw new ParserException(new Token(index, Token.EPSILON, s), "INVALID OPCODE '" + s + "'");
		}

	}

	public LinkedList<Token> getTokens() {
		return tokens;
	}
}