package com.bsettle.tis100clone.parse;

import java.util.LinkedList;

import com.bsettle.tis100clone.command.*;

public class Parser {
	private LinkedList<Token> tokens;
	private Token lookahead;

	@SuppressWarnings("unchecked")
	public Expression parse(LinkedList<Token> tokens) {
		this.tokens = (LinkedList<Token>) tokens.clone();
		lookahead = this.tokens.getFirst();

		Expression e = expression();

		if (lookahead.token != Token.EPSILON)
			throw new ParserException(lookahead, String.format("Unexpected symbol %s found", lookahead.sequence));
		return e;
	}

	private void nextToken() {
		tokens.pop();
		// at the end of input we return an epsilon token
		if (tokens.isEmpty())
			lookahead = new Token(-1, Token.EPSILON, "");
		else
			lookahead = tokens.getFirst();
		
	}
	
	private CommentExpression comment(){
		CommentExpression ce = new CommentExpression(lookahead.sequence);
		nextToken();
		return ce;
	}

	private Expression expression() {
		if (lookahead.token == Token.COMMENT) {
			return comment();
		} else if (lookahead.token == Token.LABEL) {
			String label = labelDeclaration();
			return labeledExpression(label);
		} else if (lookahead.token == Token.EPSILON){
			return null;
		}else {
			return instruction();
		}
	}
	
	private LabeledExpression labeledExpression(String label) {
		if (lookahead.token == Token.COMMENT) {
			return new LabeledExpression(label, comment());
		}else if (lookahead.token == Token.EPSILON){
			return new LabeledExpression(label, null);
		}else {
			return new LabeledExpression(label, instruction());
		}
	}

	private String labelDeclaration() {
		String label = lookahead.sequence;
		label = label.substring(0, label.length()-1);
		if (label.isEmpty()) {
			throw new ParserException(lookahead, "INVALID LABEL");
		}
		nextToken();
		return label;
	}

	private AdditionInstruction addSub(Token token){
        if (lookahead.token == Token.EPSILON){
            throw new ParserException(lookahead, String.format("USAGE: %s <SRC>", token.sequence));
        }
        SourceExpression ex = source();
        return new AdditionInstruction(token, ex);
    }

    private MoveInstruction move(Token token){
        if (lookahead.token == Token.EPSILON){
            throw new ParserException(lookahead, "USAGE: MOV <SRC> <DST>");
        }
        SourceExpression src = source();
        if (lookahead.token == Token.EPSILON){
            throw new ParserException(lookahead, "USAGE: MOV <SRC> <DST>");
        }
        DestinationExpression dst = destination();

        return new MoveInstruction(token, src, dst);
    }

    private JumpInstruction jump(Token token){
        if (lookahead.token == Token.EPSILON){
            throw new ParserException(lookahead, String.format("USAGE: %s <LBL>", token.sequence));
        }
        String src = label();
	    return new JumpInstruction(token, src);
    }

    private JumpRelativeInstruction jumpRelative(Token token){
        if (lookahead.token == Token.EPSILON){
            throw new ParserException(lookahead, "USAGE: JRO <SRC>");
        }
        SourceExpression src = source();
        return new JumpRelativeInstruction(token, src);
    }

	private InstructionExpression instruction() {
		Token token = lookahead;
		nextToken();
		if (token.token == Token.ADD_SUB) {
			return addSub(token);
			// ADD/SUB SRC
		} else if (token.token == Token.COMMAND) {
			return new CommandInstruction(token);
			// NOP|NEG|SWP|SAV
		} else if (token.token == Token.MOVE) {
			return move(token);
			// MOV SRC DEST
		} else if (token.token == Token.JUMP_LABEL) {
			return jump(token);
			// JMP LABEL
		} else if (token.token == Token.JUMP_RELATIVE) {
			return jumpRelative(token);
			// JMP SRC
		}
		throw new ParserException(token, "INVALID OPCODE '" + token.sequence + "'");
	}

	private String label() {
        if (lookahead.token != Token.VARIABLE){
            throw new ParserException(lookahead, "INVALID LABEL: " + lookahead.sequence);
        }
		String var = lookahead.sequence;
		nextToken();
		return var;
	}
	
	private RegisterExpression register(){
		RegisterExpression re = new RegisterExpression(lookahead.sequence);
		nextToken();
		return re;
	}

	private PortExpression port(){
		PortExpression pe = new PortExpression(lookahead.sequence);
		nextToken();
		return pe;
	}

	private SourceExpression source() {
		String seq = lookahead.sequence;
		if (lookahead.token == Token.REGISTER) {
            if (seq.equals("BAK")){
                throw new ParserException(lookahead, "'BAK' IS NOT A VALID SOURCE");
            }
			return register();
		} else if (lookahead.token == Token.PORT) {
			return port();
		} else if (lookahead.token == Token.NUMBER) {
			nextToken();
			return new NumberExpression(seq);
		}
		throw new ParserException(lookahead, "INVALID SOURCE '" + seq + "'");
	}

	private DestinationExpression destination(){
        String seq = lookahead.sequence;
        if (lookahead.token == Token.REGISTER) {
        	if (seq.equals("BAK")){
        	    throw new ParserException(lookahead, "BAK IS NOT A VALID DESTINATION");
            }
            return register();
        } else if (lookahead.token == Token.PORT) {
            return port();
        }
        throw new ParserException(lookahead, "INVALID DESTINATION '" + seq + "'");
    }

}
