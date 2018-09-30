// This class is a scanner for the program
// and programming language being interpreted.

import java.util.*;

public class Scanner {

    private String program;	// source program being interpreted
    private int pos;		// index of next char in program
    private Token token;	// last/current scanned token

    // sets of various characters and lexemes
    private Set<String> whitespace = new HashSet<String>();
    private Set<String> digits = new HashSet<String>();
    private Set<String> letters = new HashSet<String>();
    private Set<String> legits = new HashSet<String>();
    private Set<String> keywords = new HashSet<String>();
    private Set<String> operators = new HashSet<String>();

    // initializers for previous sets

	//creates a set of chars in place based on the given range (lo - hi)
	//example: fill(s, 'a', 'd') => s = {'a', 'b', 'c', 'd'}
    private void fill(Set<String> s, char lo, char hi) {
		for (char c=lo; c<=hi; c++)
			s.add(c+"");
    }    

	//initializes the set of characters considered 'whitespace'
	//in this simplistic grammar we consider newline and tabs to be whitespace
    private void initWhitespace(Set<String> s) {
		s.add(" ");
		s.add("\n");
		s.add("\t");
    }

	//initializes the set of valid 'digit' characters
    private void initDigits(Set<String> s) {
		fill(s,'0','9');
    }

	//initializes the set of valid 'letter' characters
    private void initLetters(Set<String> s) {
		fill(s,'A','Z');
		fill(s,'a','z');
    }

	//initializes the set of valid 'legit' characters
    private void initLegits(Set<String> s) {
		s.addAll(letters);
		s.addAll(digits);
    }

	//initializes the set of valid 'operator' characters
    private void initOperators(Set<String> s) {
		s.add("=");
		s.add("+");
		s.add("-");
		s.add("*");
		s.add("/");
		s.add("(");
		s.add(")");
		s.add(";");
    }

    private void initKeywords(Set<String> s) {
    }

    // constructor:
    //   - squirrel-away source program
    //   - initialize sets
    public Scanner(String program) {
		this.program=program;
		pos=0;
		token=null;
		initWhitespace(whitespace);
		initDigits(digits);
		initLetters(letters);
		initLegits(legits);
		initKeywords(keywords);
		initOperators(operators);
    }

    // handy string-processing methods

	//returns whether the scanner is at the end of the program
    public boolean done() {
		return pos>=program.length();
    }

	//increments the scanners position until a token is found which is NOT in Set s
    private void many(Set<String> s) {
		while (!done() && s.contains(program.charAt(pos)+""))
			pos++;
    }
	
	//increment the scanners position one character
	//past wherever char c is found in the given program
    private void past(char c) {
		while (!done() && c!=program.charAt(pos))
			pos++;
		if (!done() && c==program.charAt(pos))
			pos++;
	}

    // scan various kinds of lexeme

	//creates a token out of the next number found in the scanner
    private void nextNumber() {
		int old=pos;
		many(digits);
		token=new Token("num",program.substring(old,pos));
    }

	//creates a keyword (id) token 
    private void nextKwId() {
		int old=pos;
		many(letters);
		many(legits);
		String lexeme=program.substring(old,pos);
		token=new Token((keywords.contains(lexeme) ? lexeme : "id"),lexeme);
    }

    private void nextOp() {
		int old=pos;
		pos=old+2;
		if (!done()) {
			String lexeme=program.substring(old,pos);
			if (operators.contains(lexeme)) {
			token=new Token(lexeme); // two-char operator
			return;
			}
		}
		pos=old+1;
		String lexeme=program.substring(old,pos);
		token=new Token(lexeme); // one-char operator
		}
		
    // This method determines the kind of the next token (e.g., "id"),
    // and calls a method to scan that token's lexeme (e.g., "foo").
    public boolean next() {
		if (done())
			return false;
		many(whitespace);
		String c=program.charAt(pos)+"";
		if (digits.contains(c))
			nextNumber();
		else if (letters.contains(c))
			nextKwId();
		else if (operators.contains(c))
			nextOp();
		else if (c.equals("#")){
			//this is a comment, move past the first hashtag
			past('#');
			//move past the second hastag, ignoring everything in between
			past('#');
			return next();
		} else {
			System.err.println("illegal character at position "+pos);
			pos++;
			return next();
		}
		return true;
    }

    // This method scans the next lexeme,
    // if the current token is the expected token.
    public void match(Token t) throws SyntaxException {
		if (!t.equals(curr()))
			throw new SyntaxException(pos,t,curr());
		next();
    }

	//returns the current token in the scanner
    public Token curr() throws SyntaxException {
		if (token==null)
			throw new SyntaxException(pos,new Token("ANY"),new Token("EMPTY"));
		return token;
    }

    public int pos() { return pos; }

    // for unit testing
    public static void main(String[] args) {
		try {
			Scanner scanner=new Scanner(args[0]);
			while (scanner.next())
				System.out.println(scanner.curr());
		} catch (SyntaxException e) {
			System.err.println(e);
		}
    }

}
