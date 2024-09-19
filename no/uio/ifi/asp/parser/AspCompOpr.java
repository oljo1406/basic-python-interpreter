package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;


public class AspCompOpr extends AspSyntax{
    
    String compOpr; // Vil være en av de lovlige tokens i comp opr
    
    AspCompOpr(int n) {
        super(n);
    }


    public static AspCompOpr parse(Scanner s) {
        enterParser("comp opr");

        AspCompOpr aco = new AspCompOpr(s.curLineNum());

        if(s.curToken().kind == (lessToken)){
            aco.compOpr = " < ";
            skip(s, lessToken);
        } else if (s.curToken().kind == (greaterToken)){
            aco.compOpr = " > ";
            skip(s, greaterToken);
        } else if (s.curToken().kind == (doubleEqualToken)){
            aco.compOpr = " == ";
            skip(s, doubleEqualToken);
        } else if (s.curToken().kind == (greaterEqualToken)){
            aco.compOpr = " >= ";
            skip(s, greaterEqualToken);
        } else if (s.curToken().kind == (lessEqualToken)){
            aco.compOpr = " <= ";
            skip(s, lessEqualToken);
        } else {
            aco.compOpr = " != ";
            skip(s, notEqualToken);
        }

        leaveParser("comp opr");
        return aco;
    }


    @Override
    public void prettyPrint() {
        prettyWrite(compOpr);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
    }
}
