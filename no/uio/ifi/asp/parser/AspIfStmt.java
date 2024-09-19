package no.uio.ifi.asp.parser;


import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import java.util.ArrayList;

public class AspIfStmt extends AspCompoundStmt {
    ArrayList<AspExpr> exprs = new ArrayList<>();
    ArrayList<AspSuite> suits = new ArrayList<>();
    AspSuite as;
    AspIfStmt(int n) {
        super(n);
    }
    
    public static AspIfStmt parse(Scanner s){
        enterParser("if stmt");
        AspIfStmt ais = new AspIfStmt(s.curLineNum());
        skip(s, ifToken);
        while(true){
            ais.exprs.add(AspExpr.parse(s));
            skip(s, colonToken);
            ais.suits.add(AspSuite.parse(s));
            if(s.curToken().kind != elifToken) break;
            skip(s, elifToken);
        }
        if (s.curToken().kind == elseToken){
            skip(s, elseToken);
            skip(s, colonToken);
            ais.as = AspSuite.parse(s);
        }
        leaveParser("if stmt");
        return ais;
    }

    @Override
    public void prettyPrint() {
        int nPrinted = 0;
        prettyWrite("if ");
        for(AspExpr expre: exprs){
            for (AspSuite suit: suits){
                if(nPrinted > 0){
                    prettyWrite("elif ");
                }
                expre.prettyPrint(); 
                prettyWrite(": ");
                suit.prettyPrint(); ++nPrinted;
            }
            if (as != null){
                prettyWrite("else");
                prettyWrite(":");
                as.prettyPrint();
            }
            
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        boolean leter = false;
        int teller = 0;
        int i = 0;
        for(AspExpr expr: exprs){
            if(expr.eval(curScope).getBoolValue("if stmt", this)){
                i++;
                trace("if True alt #" + i + ": ..."); 
                suits.get(teller).eval(curScope);
                leter = true;
                return null;
            }
            teller++;
        }
        if(leter == false && as != null){
            trace("else: ..."); 
            as.eval(curScope);
        }

        return null;
    }
    
}
