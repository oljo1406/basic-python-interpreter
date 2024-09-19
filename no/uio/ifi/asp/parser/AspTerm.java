package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspTerm extends AspSyntax {
    
    ArrayList<AspFactor> factors = new ArrayList<AspFactor>();
    ArrayList<AspTermOpr> termOprs = new ArrayList<AspTermOpr>();

    AspTerm(int n) {
	    super(n);
    }


    public static AspTerm parse(Scanner s) {
        enterParser("term");

        
        AspTerm at = new AspTerm(s.curLineNum());
        at.factors.add(AspFactor.parse(s));

        if(s.curToken().kind != plusToken && s.curToken().kind != minusToken){
            leaveParser("term");
            return at;
        }
        while(true){
            at.termOprs.add(AspTermOpr.parse(s));
            at.factors.add(AspFactor.parse(s));
            if(s.curToken().kind != plusToken && s.curToken().kind != minusToken){
                break;
            }
        }

        leaveParser("term");
        return at;

    }

    @Override
    public void prettyPrint() {
        
        int i = 0;
        for (AspFactor factor : factors) {
            factor.prettyPrint();
            if (i < termOprs.size()) {
                termOprs.get(i).prettyPrint();
                i++;
            }
        }
    }
        

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue v = factors.get(0).eval(curScope);
    for (int i = 1; i < factors.size(); ++i){
        String k = termOprs.get(i-1).termOpr;   
        if (k == " - "){
            v = v.evalSubtract(factors.get(i).eval(curScope), this); 
        }
        else if ( k == " + "){
            v = v.evalAdd(factors.get(i).eval(curScope), this); 
        }
    }
    return v; 
    }
}
