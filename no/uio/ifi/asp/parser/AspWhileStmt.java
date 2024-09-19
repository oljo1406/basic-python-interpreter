package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspWhileStmt extends AspCompoundStmt {
    AspExpr ae;
    AspSuite as;

    AspWhileStmt(int n) {
        super(n);
    }
    public static AspWhileStmt parse(Scanner s){
        enterParser("while stmt");
        AspWhileStmt aws = new AspWhileStmt(s.curLineNum());
        skip(s, whileToken);
        aws.ae = AspExpr.parse(s);
        skip(s, colonToken);
        aws.as = AspSuite.parse(s);
        leaveParser("while stmt");
        return aws; 
    }

    @Override
    public void prettyPrint() {
        prettyWrite("while ");
        ae.prettyPrint();
        prettyWrite(": ");
        as.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        while (true) {
            RuntimeValue t = ae.eval(curScope);
            if (! t.getBoolValue("while loop test",this)) break;
            trace("while True: ...");
            as.eval(curScope);
        }
        trace("while False:");
        return null;
    }   
}
