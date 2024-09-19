package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeIntValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspIntegerLiteral extends AspAtom{
    String nr;
    long number;

    AspIntegerLiteral(int n){
        super(n);
    }

    public static AspIntegerLiteral parse(Scanner s){

        enterParser("integer literal");

        AspIntegerLiteral ail = new AspIntegerLiteral(s.curLineNum());

        ail.number = s.curToken().integerLit;
        ail.nr = Long.toString(ail.number);

        skip(s, integerToken); //Integer token
        leaveParser("integer literal");
        
        return ail;
    }

    @Override
    public void prettyPrint() {

        prettyWrite(nr);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeIntValue(number);
    }
}
