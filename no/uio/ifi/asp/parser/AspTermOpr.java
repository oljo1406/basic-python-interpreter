package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspTermOpr extends AspSyntax {
    
    String termOpr;

    AspTermOpr(int n) {
	    super(n);
    }

    public static AspTermOpr parse(Scanner s) {
	enterParser("term opr");

	
	AspTermOpr ato = new AspTermOpr(s.curLineNum());
    
    if(s.curToken().kind == (plusToken)){
        ato.termOpr = " + ";
        skip(s, plusToken);
    } else {
        ato.termOpr = " - ";
        skip(s, minusToken);
    } 

	leaveParser("term opr");
	return ato;

    }

    @Override
    public void prettyPrint() {
        
        prettyWrite(termOpr);

    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	    return null;
    }
}
