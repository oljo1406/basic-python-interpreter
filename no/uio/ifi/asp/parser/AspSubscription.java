package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;



public class AspSubscription extends AspPrimarySuffix{
    AspExpr body; 
    AspSubscription(int n) {
        super(n);
    }
    static AspSubscription parse(Scanner s){
        enterParser("subscription");
        
        AspSubscription as = new AspSubscription(s.curLineNum());

        skip(s, leftBracketToken);
        as.body = AspExpr.parse(s);
        skip (s, rightBracketToken);

        leaveParser("subscription");

        return as; 
    }
    @Override
    public void prettyPrint() {
        prettyWrite("[");
        body.prettyPrint();
        prettyWrite("]");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = body.eval(curScope);
        return v;
    }

}

