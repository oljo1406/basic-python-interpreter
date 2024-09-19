package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeListValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;


public class AspListDisplay extends AspAtom{

    ArrayList<AspExpr> aexprs = new ArrayList<AspExpr>();

    AspListDisplay(int n){
        super(n);
    }

    public static AspListDisplay parse(Scanner s){

        enterParser("list display");

        AspListDisplay ald = new AspListDisplay(s.curLineNum());

        skip(s, leftBracketToken); 

        if(s.curToken().kind != rightBracketToken){
            while(true){
            ald.aexprs.add(AspExpr.parse(s));
            if (s.curToken().kind != commaToken) break;
            skip(s, commaToken);
            }
        }

        skip(s, rightBracketToken); 

        leaveParser("list display");
        
        return ald;
    }

    @Override
    public void prettyPrint() {
        
        int nPrinted = 0;

        prettyWrite("[");

        if(!aexprs.isEmpty()){
            for(AspExpr aspexpr : aexprs){
                if(nPrinted > 0){
                    prettyWrite(", ");
                }
                aspexpr.prettyPrint(); ++nPrinted;
            }
        }
        prettyWrite("]");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> evalExprs = new ArrayList<>();
        if (!aexprs.isEmpty()){
            for (AspExpr expr: aexprs){
                RuntimeValue v = expr.eval(curScope);
                evalExprs.add(v);
            }
        }
        return new RuntimeListValue(evalExprs);
    }
}
