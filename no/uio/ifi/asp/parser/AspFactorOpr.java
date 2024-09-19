package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspFactorOpr extends AspSyntax {
    
    String factorOpr;

    AspFactorOpr(int n) {
	    super(n);
    }


    public static AspFactorOpr parse(Scanner s) {
        enterParser("factor opr");

        
        AspFactorOpr afo = new AspFactorOpr(s.curLineNum());
        
        if(s.curToken().kind == (astToken)){
            afo.factorOpr = " * ";
            skip(s, astToken);
        } else if (s.curToken().kind == (slashToken)){
            afo.factorOpr = " / ";
            skip(s, slashToken);
        } else if (s.curToken().kind == (percentToken)){
            afo.factorOpr = " % ";
            skip(s, percentToken);
        } else { 
            afo.factorOpr = " // ";
            skip(s, doubleSlashToken);
        } 

        leaveParser("factor opr");
        return afo;

    }


    @Override
    public void prettyPrint() {

        prettyWrite(factorOpr);
        
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	return null;
    }
}
