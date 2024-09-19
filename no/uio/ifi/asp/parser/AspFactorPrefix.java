package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspFactorPrefix extends AspSyntax {
    
    String factorPrefix;

    AspFactorPrefix(int n) {
	    super(n);
    }


    public static AspFactorPrefix parse(Scanner s) {
        enterParser("factor prefix");

        
        AspFactorPrefix afp = new AspFactorPrefix(s.curLineNum());
        
        if(s.curToken().kind == (plusToken)){
            afp.factorPrefix = "+";
            skip(s, plusToken);
        } else {
            afp.factorPrefix = "-";
            skip(s, minusToken);
        }

        leaveParser("factor prefix");
        return afp;

    }


    @Override
    public void prettyPrint() {

        prettyWrite(factorPrefix);
        
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	return null;
    }
}
