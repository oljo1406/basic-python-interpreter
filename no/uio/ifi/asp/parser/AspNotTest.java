package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspNotTest extends AspSyntax {
    
    AspComparison aspComparison;
    String not = "";

    AspNotTest(int n) {
	    super(n);
    }


    public static AspNotTest parse(Scanner s) {
        enterParser("not test");

        
        AspNotTest ant = new AspNotTest(s.curLineNum());
        
        if(s.curToken().kind == notToken){
            ant.not = "not ";
            skip(s, notToken);
        }

        ant.aspComparison = AspComparison.parse(s);

        leaveParser("not test");
        return ant;

    }


    @Override
    public void prettyPrint() {

        if(!not.isEmpty()){
            prettyWrite(not);
        }
        
        aspComparison.prettyPrint();
        
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	RuntimeValue value = aspComparison.eval(curScope);
    if (not == "not "){
        value = value.evalNot(this);
    }
    return value;
    }
}
