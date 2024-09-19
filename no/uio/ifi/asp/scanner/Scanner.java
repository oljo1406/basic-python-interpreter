// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
    private LineNumberReader sourceFile = null;
    private String curFileName;
    private ArrayList<Token> curLineTokens = new ArrayList<>();
    private Stack<Integer> indents = new Stack<>();
    private final int TABDIST = 4;


    public Scanner(String fileName) {
	curFileName = fileName;
	indents.push(0);

	try {
	    sourceFile = new LineNumberReader(
			    new InputStreamReader(
				new FileInputStream(fileName),
				"UTF-8"));
	} catch (IOException e) {
	    scannerError("Cannot read " + fileName + "!");
	}
    }


    private void scannerError(String message) {
	String m = "Asp scanner error";
	if (curLineNum() > 0)
	    m += " on nyLine " + curLineNum();
	m += ": " + message;

	Main.error(m);
    }


    public Token curToken() {
	while (curLineTokens.isEmpty()) {
	    readNextLine();
	}
	return curLineTokens.get(0);
    }


    public void readNextToken() {
	if (! curLineTokens.isEmpty())
	    curLineTokens.remove(0);
    }


    private void readNextLine() {
	curLineTokens.clear();

	// Read the next nyLine:
	String line = null;
	try {
	    line = sourceFile.readLine();
	    if (line == null) {
		sourceFile.close();
		sourceFile = null;
	    } else {
		Main.log.noteSourceLine(curLineNum(), line);
	    }
	} catch (IOException e) {
	    sourceFile = null;
	    scannerError("Unspecified I/O error!");
	}


	String nyLine = "";

	//Hvis line er null -> EOF og dedent

	if(line == null){
		while(0<indents.peek()){
			indents.pop();
			Token token = new Token(dedentToken, curLineNum());
			curLineTokens.add(token);
			Main.log.noteToken(token);
		}
		Token eoftoken = new Token(eofToken, curLineNum());
		curLineTokens.add(eoftoken);
		Main.log.noteToken(eoftoken);
		return;
	}

	nyLine = expandLeadingTabs(line); 
	
	int indent = findIndent(nyLine);
	
	if(nyLine == "" || nyLine.charAt(indent) == '#' || indent == nyLine.length()){
		return; //Hvis nyLine er tom eller # -> går ut av readNextLine
	}

	if(indent > indents.peek()){ //Sjekker at indent er større en høyeste -> legger til en indent-token på listen
		indents.push(indent);
		Token inToken = new Token(indentToken, curLineNum());
		curLineTokens.add(inToken);
	} while (indent < indents.peek()){ //Sjekker at indent er mindre en høyeste -> legger til en deindent-token på listen
		indents.pop();
		Token deToken = new Token(dedentToken, curLineNum());
		curLineTokens.add(deToken);
	} if (indent != indents.peek()){
		//ERROR - indenteringsfeil
		System.out.println("Indenteringsfeil");
	}

	
	for(int i = 0; i < nyLine.length();){ //i++
		
		if(isLetterAZ(nyLine.charAt(i))){ 
			String name = "";
			while(i < nyLine.length() && (isLetterAZ(nyLine.charAt(i)) || isDigit(nyLine.charAt(i)))){
				name += nyLine.charAt(i);
				if (i < nyLine.length() - 1){
					i++;
				} else {
					break;
				}
			}
			//Sjekk hva navn er f.eks. if, else, etc
			if (name.equals("and")){
				Token token = new Token(andToken, curLineNum());
				curLineTokens.add(token);
			}
			else if (name.equals("def")){
				Token token = new Token(defToken, curLineNum());
				curLineTokens.add(token);
			}
			else if (name.equals("elif")){
				Token token = new Token(elifToken, curLineNum());
				curLineTokens.add(token);
			}
			else if (name.equals("else")){
				Token token = new Token(elseToken, curLineNum());
				curLineTokens.add(token);
			}
			else if (name.equals("False")){
				Token token = new Token(falseToken, curLineNum());
				curLineTokens.add(token);
			}
			else if (name.equals("for")){
				Token token = new Token(forToken, curLineNum());
				curLineTokens.add(token);
			}
			else if (name.equals("global")){
				Token token = new Token(globalToken, curLineNum());
				curLineTokens.add(token);
			}
			else if (name.equals("if")){
				Token token = new Token(ifToken, curLineNum());
				curLineTokens.add(token);
			}
			else if (name.equals("in")){
				Token token = new Token(inToken, curLineNum());
				curLineTokens.add(token);
			}
			else if (name.equals("None")){
				Token token = new Token(noneToken, curLineNum());
				curLineTokens.add(token);
			}
			else if (name.equals("not")){
				Token token = new Token(notToken, curLineNum());
				curLineTokens.add(token);
			}
			else if (name.equals("or")){
				Token token = new Token(orToken, curLineNum());
				curLineTokens.add(token);
			}
			else if (name.equals("pass")){
				Token token = new Token(passToken, curLineNum());
				curLineTokens.add(token);
			}
			else if (name.equals("return")){
				Token token = new Token(returnToken, curLineNum());
				curLineTokens.add(token);
			}
			else if (name.equals("True")){
				Token token = new Token(trueToken, curLineNum());
				curLineTokens.add(token);
				
			}
			else if (name.equals("while")){
				Token token = new Token(whileToken, curLineNum());
				curLineTokens.add(token);
			}

			//Opprette token
			else {
				Token token = new Token(nameToken, curLineNum());
				token.name = name;
				curLineTokens.add(token);
			}
		}
		else if(isDigit(nyLine.charAt(i))){ 
			//int 
			String stringTall = "";
			int tall = 0;
			double floatTall = 0.0;

			while(i < nyLine.length() && isDigit(nyLine.charAt(i))){
				stringTall += nyLine.charAt(i);
				if (i < nyLine.length() - 1){
					i++;
				} else {
					break;
				}	
			}

			if(nyLine.charAt(i) == '.'){
				i++; //flyter i fra . til tall
				String secondPartOfFloat = "";
				//float 
				while(i < nyLine.length() && isDigit(nyLine.charAt(i))){
				secondPartOfFloat += nyLine.charAt(i);
				i++;
				}

				floatTall = Double.parseDouble(stringTall + "." + secondPartOfFloat);
				i--; //flytter tilbake til det siste tallet sånn at charAt(i) ikke er ute av bonds
				//Opprette token
				Token token = new Token(floatToken, curLineNum());
				token.floatLit = floatTall;
				curLineTokens.add(token);
				
			}
			else{
			tall = Integer.valueOf(stringTall);
			//Opprette token
			Token token = new Token(integerToken, curLineNum());
			token.integerLit = tall;
			curLineTokens.add(token);
			}
		}
		//lage string 

		else if(nyLine.charAt(i) == '\"' || nyLine.charAt(i) == '\'' ){
			
			String setning = "";
			
			i++;
			

			while (i < nyLine.length() && nyLine.charAt(i) != '\"' && nyLine.charAt(i) != '\''){
				setning += nyLine.charAt(i); 
				if (i < nyLine.length() - 1){
					i++;
				} else {
					break;
				}
				
			}
			
			Token token = new Token(stringToken, curLineNum());
			token.stringLit = setning; 
			curLineTokens.add(token);
			
		}

		//I er nå siste tallet aka 0, så i + 1 = null
	
		if(nyLine.charAt(i) == '=' && nyLine.charAt(i + 1) == '='){ 
			Token token = new Token(doubleEqualToken, curLineNum());
			curLineTokens.add(token);
			i++;
		}
		else if(nyLine.charAt(i) == '='){   
			Token token = new Token(equalToken, curLineNum());
			curLineTokens.add(token);
		}
		if(nyLine.charAt(i) == '*'){   
			Token token = new Token(astToken, curLineNum());
			curLineTokens.add(token);
		}
		if(nyLine.charAt(i) == '/' && nyLine.charAt(i + 1) == '/'){ 
			Token token = new Token(doubleSlashToken, curLineNum());
			curLineTokens.add(token);
			i++;
		}
		else if(nyLine.charAt(i) == '/'){   
			Token token = new Token(slashToken, curLineNum());
			curLineTokens.add(token);
		}
		if(nyLine.charAt(i) == '>' && nyLine.charAt(i + 1) == '='){   
			Token token = new Token(greaterEqualToken, curLineNum());
			curLineTokens.add(token);
			i++;
		}
		else if(nyLine.charAt(i) == '>'){   
			Token token = new Token(greaterToken, curLineNum());
			curLineTokens.add(token);
		}
		if(nyLine.charAt(i) == '<' && nyLine.charAt(i + 1) == '='){   
			Token token = new Token(lessEqualToken, curLineNum());
			curLineTokens.add(token);
			i++;
		}
		else if(nyLine.charAt(i) == '<'){   
			Token token = new Token(lessToken, curLineNum());
			curLineTokens.add(token);
		}
		if(nyLine.charAt(i) == '-'){   
			Token token = new Token(minusToken, curLineNum());
			curLineTokens.add(token);
		}
		if(nyLine.charAt(i) == '!' && nyLine.charAt(i + 1) == '='){   
			Token token = new Token(notEqualToken, curLineNum());
			curLineTokens.add(token);
			i++;
		}
		if(nyLine.charAt(i) == '%'){   
			Token token = new Token(percentToken, curLineNum());
			curLineTokens.add(token);
		}
		if(nyLine.charAt(i) == '+'){   
			Token token = new Token(plusToken, curLineNum());
			curLineTokens.add(token);
		}
		if(nyLine.charAt(i) == ':'){   
			Token token = new Token(colonToken, curLineNum());
			curLineTokens.add(token);
		}
		if(nyLine.charAt(i) == ','){   
			Token token = new Token(commaToken, curLineNum());
			curLineTokens.add(token);
		}
		if(nyLine.charAt(i) == '{'){   
			Token token = new Token(leftBraceToken, curLineNum());
			curLineTokens.add(token);
		}
		if(nyLine.charAt(i) == '['){   
			Token token = new Token(leftBracketToken, curLineNum());
			curLineTokens.add(token);
		}
		if(nyLine.charAt(i) == '('){   
			Token token = new Token(leftParToken, curLineNum());
			curLineTokens.add(token);
		}
		if(nyLine.charAt(i) == '}'){   
			Token token = new Token(rightBraceToken, curLineNum());
			curLineTokens.add(token);
		}
		if(nyLine.charAt(i) == ']'){   
			Token token = new Token(rightBracketToken, curLineNum());
			curLineTokens.add(token);
		}
		if(nyLine.charAt(i) == ')'){   
			Token token = new Token(rightParToken, curLineNum());
			curLineTokens.add(token);
		}
		if(nyLine.charAt(i) == ';'){   
			Token token = new Token(semicolonToken, curLineNum());
			curLineTokens.add(token);
		}

		i++; //Hvis det er en blank
	}
	
	// Terminate nyLine:
	
	curLineTokens.add(new Token(newLineToken,curLineNum()));

	for (Token t: curLineTokens) 
	    Main.log.noteToken(t);
    }

    public int curLineNum() {
	return sourceFile!=null ? sourceFile.getLineNumber() : 0;
    }

    private int findIndent(String s) {
	int indent = 0;

	while (indent<s.length() && s.charAt(indent)==' ') indent++;
	return indent;
    }

    private String expandLeadingTabs(String s) {
	
	//bytter ut tabs med space 
	int n = 0;
	int i = 0; 
	
	if (s != null){
		for(int j = 0; j < s.length(); j++){
			if (s.charAt(j) == ' '){
				n++; }
			else if(s.charAt(j) == '\t'){
				n += TABDIST - (n % TABDIST);
			}
			else {
				i = j;
				break;
			}
		}
	}
	

	//Oppretter en ny string og legger til riktig antall blanke

	String streng = "";
	for (int j = 0; j < n; j++){
		streng += " ";
	}

	if (s != null){
		for (int j = i; j < s.length(); j++){
			streng += s.charAt(j);
		}
	}
	
	return streng;
    }


    private boolean isLetterAZ(char c) {
	return ('A'<=c && c<='Z') || ('a'<=c && c<='z') || (c=='_');
    }


    private boolean isDigit(char c) {
	return '0'<=c && c<='9';
    }


    public boolean isCompOpr() {
	TokenKind k = curToken().kind;
	if(
		k == (lessToken) || 
		k == (greaterToken) || 
		k == (doubleEqualToken) || 
		k == (greaterEqualToken) || 
		k == (lessEqualToken) || 
		k == (notEqualToken)){
			return true;
	}
	return false;
    }


    public boolean isFactorPrefix() {
	TokenKind k = curToken().kind;
	if(k == plusToken || k == minusToken){
		return true;
	}
	return false;
    }


    public boolean isFactorOpr() {
	TokenKind k = curToken().kind;
	if(k == astToken || k == slashToken || k == percentToken || k == doubleSlashToken){
		return true;
	}
	return false;
    }
	

    public boolean isTermOpr() {
	TokenKind k = curToken().kind;
	if(k == plusToken || k == minusToken){
		return true;
	}
	return false;
    }


    public boolean anyEqualToken() {
	for (Token t: curLineTokens) {
	    if (t.kind == equalToken) return true;
	    if (t.kind == semicolonToken) return false;
	}
	return false;
    }
}
