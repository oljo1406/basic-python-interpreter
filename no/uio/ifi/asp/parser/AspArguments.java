package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeListValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;



public class AspArguments extends AspPrimarySuffix{
    ArrayList<AspExpr>expressions = new ArrayList<AspExpr>();
    AspArguments(int n) {
        super(n);
    }
    static AspArguments parse(Scanner s){
        enterParser("arguments");
        
        AspArguments aa = new AspArguments(s.curLineNum());

        skip(s, leftParToken);
        if (s.curToken().kind != rightParToken) {
            while(true){
                aa.expressions.add(AspExpr.parse(s));
                if (s.curToken().kind != commaToken) break;
                skip(s, commaToken);
            }
        }
        
        skip (s, rightParToken);
        leaveParser("arguments");
        return aa; 
    }
    @Override
    public void prettyPrint() {
        int nPrinted = 0;
        prettyWrite("(");
        for(AspExpr ae: expressions){
            if(nPrinted > 0){
                prettyWrite(", ");
            }
            ae.prettyPrint(); ++nPrinted;
        }
        prettyWrite(")");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> evalExprs = new ArrayList<>();
        if (!expressions.isEmpty()){
            for (AspExpr expr: expressions){
                RuntimeValue v = expr.eval(curScope);
                evalExprs.add(v);
            }
        }
        RuntimeValue v = new RuntimeListValue(evalExprs);
        return v;
    }

}
