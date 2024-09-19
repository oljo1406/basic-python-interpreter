package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspName extends AspAtom{

    public String name;

    AspName(int n){
        super(n);
    }

    public static AspName parse(Scanner s){

        enterParser("name");

        AspName aspn = new AspName(s.curLineNum());

        aspn.name = s.curToken().name;
        
        skip(s, nameToken); 

        leaveParser("name");
        
        return aspn;
    }

    @Override
    public void prettyPrint() {
        prettyWrite(name);
        
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return curScope.find(name, this);
    }
}
