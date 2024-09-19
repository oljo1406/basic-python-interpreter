package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeNoneValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import javax.lang.model.type.NullType;


public class AspNoneLiteral extends AspAtom{

    NullType none;

    AspNoneLiteral(int n){
        super(n);
    }

    public static AspNoneLiteral parse(Scanner s){

        enterParser("none literal");

        AspNoneLiteral anl = new AspNoneLiteral(s.curLineNum());
         
        anl.none = null;
        
        skip(s, noneToken);

        leaveParser("none literal");
        
        return anl;
    }

    @Override
    public void prettyPrint() {
        
        prettyWrite("None");
        
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeNoneValue();
    }
}
