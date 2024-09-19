package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeBoolValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspBooleanLiteral extends AspAtom{

    boolean bool;

    AspBooleanLiteral(int n){
        super(n);
    }

    public static AspBooleanLiteral parse(Scanner s){

        enterParser("boolean literal");

        AspBooleanLiteral abl = new AspBooleanLiteral(s.curLineNum());

        if(s.curToken().kind == trueToken){
            abl.bool = true;
            skip(s, trueToken);
        } else {  
            abl.bool = false;
            skip(s, falseToken);
        }
        leaveParser("boolean literal");
        return abl;
    }

    @Override
    public void prettyPrint() {
        if(bool){
            prettyWrite("True");
        } else {
            prettyWrite("False");
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeBoolValue(bool);
    }
}
