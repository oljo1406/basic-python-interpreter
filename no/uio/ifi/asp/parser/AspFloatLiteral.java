package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeFloatValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;


public class AspFloatLiteral extends AspAtom{

    double number;
    String strNumber;

    AspFloatLiteral(int n){
        super(n);
    }

    public static AspFloatLiteral parse(Scanner s){

        enterParser("float literal");

        AspFloatLiteral afl = new AspFloatLiteral(s.curLineNum());

        afl.number = s.curToken().floatLit;
        afl.strNumber = Double.toString(afl.number);

        skip(s, floatToken); 

        leaveParser("float literal");
        
        return afl;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(strNumber); 
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeFloatValue(number);
    }
}
