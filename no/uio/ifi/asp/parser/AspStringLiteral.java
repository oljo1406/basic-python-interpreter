package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeStringValue;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspStringLiteral extends AspAtom{

    String streng;

    AspStringLiteral(int n){
        super(n);
    }

    public static AspStringLiteral parse(Scanner s){

        enterParser("string literal");

        AspStringLiteral asl = new AspStringLiteral(s.curLineNum());
         
        asl.streng = s.curToken().stringLit;
        
        skip(s, stringToken);

        leaveParser("string literal");
        
        return asl;
    }

    @Override
    public void prettyPrint() {
        
        prettyWrite("\"" + streng + "\""); //Ønsker å printe f.eks. "januar" og ikke januar
        
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeStringValue(streng);  
    }
}
