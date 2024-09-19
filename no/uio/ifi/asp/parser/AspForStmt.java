package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeDictValue;
import no.uio.ifi.asp.runtime.RuntimeListValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspForStmt extends AspCompoundStmt{
    AspName an;
    AspExpr ae;
    AspSuite as;
    AspForStmt(int n) {
        super(n);
    }

    public static AspForStmt parse(Scanner s){
        enterParser("for stmt");
        AspForStmt afs = new AspForStmt(s.curLineNum());
        skip(s, forToken);
        afs.an = AspName.parse(s);
        skip(s, inToken);
        afs.ae = AspExpr.parse(s);
        skip(s, colonToken);
        afs.as = AspSuite.parse(s);
        leaveParser("for stmt");
        return afs; 
    }

    @Override
    public void prettyPrint() {
        prettyWrite("for ");
        an.prettyPrint(); 
        prettyWrite(" in ");
        ae.prettyPrint();
        prettyWrite(":");
        as.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        int teller = 1;
        RuntimeValue v = ae.eval(curScope);
        if(v instanceof RuntimeListValue){
            //Konverterer for å hente listen. Dette kan vi gjøre fordi vi vet at det er en RuntimeListValue.
            RuntimeListValue runtimeListValue = (RuntimeListValue) v;
            //Har en metode for å hente listen
            for(RuntimeValue value : runtimeListValue.getList()){
                curScope.assign(an.name, value);
                trace("for #" + teller + ": " + an.name + " = " + value.showInfo()); 
                teller ++;
                as.eval(curScope);
            }
        } else {
            //bruker trenger en error
            RuntimeValue.runtimeError("expr is not a list", this);
        }
        return null;
    }
}
