package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspInnerExpr extends AspAtom{

    AspExpr expr;

    AspInnerExpr(int n){
        super(n);
    }

    public static AspInnerExpr parse(Scanner s){

        enterParser("inner expr");

        AspInnerExpr aie = new AspInnerExpr(s.curLineNum());

        skip(s, leftParToken);

        aie.expr = AspExpr.parse(s);

        skip(s, rightParToken);

        leaveParser("inner expr");
        
        return aie;
    }

    @Override
    public void prettyPrint() {
       
        prettyWrite("(");

        expr.prettyPrint();

        prettyWrite(")");

    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = expr.eval(curScope);
        return v;
    }
}
