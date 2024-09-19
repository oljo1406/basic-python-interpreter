package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeDictValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspAssignment extends AspSmallStmt {
    ArrayList<AspSubscription> subsc = new ArrayList<>();
    AspName nvn;
    AspExpr expr; 
    AspAssignment(int n) {
        super(n); 
    }
    public static AspAssignment parse(Scanner s){
        enterParser("assignment");
        AspAssignment aa = new AspAssignment(s.curLineNum());
        aa.nvn = AspName.parse(s);
        if (s.curToken().kind != equalToken) {
            while(true){
                aa.subsc.add(AspSubscription.parse(s));
                if(s.curToken().kind == equalToken) break;
            }
        }
        skip(s, equalToken); 
        aa.expr = AspExpr.parse(s); 
        leaveParser("assignment");

        return aa; 
    }
    

    @Override
    public void prettyPrint() {
        nvn.prettyPrint();
        for(AspSubscription as: subsc){
            as.prettyPrint(); 
        }
        prettyWrite(" = ");
        expr.prettyPrint();
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        if(subsc.size() == 0){
            RuntimeValue v = expr.eval(curScope);
            curScope.assign(nvn.name, v);
            trace(nvn.name + " = " + v.showInfo());
        }
        //Hvis den har et element f.eks. a[1] = 5
        else if(subsc.size() == 1){
            //Henter RuntimeValue til navn -> list, dict, float, int, etc.
            RuntimeValue runtimeName = nvn.eval(curScope);
            RuntimeValue runtimeSubsc = subsc.get(0).eval(curScope);
            RuntimeValue runtimeExpr = expr.eval(curScope);

            runtimeName.evalAssignElem(runtimeSubsc, runtimeExpr, this);
            trace(nvn.name + "[" + runtimeSubsc.showInfo() + "]" + " = " + runtimeExpr.showInfo());
        } 
        //Hvis den har 2 eller flere f.eks. a[1][2] = 10
        else{ 
            assert subsc.size() >= 2;
            //Henter navn sin eval 
            RuntimeValue runtimeName = nvn.eval(curScope);
            //Må vi så finne verdien til subsc unntatt den siste
            for(int i = 0; i < (subsc.size() - 1); i++){
                //RuntimeName er nå verdien til alle subsc unntatt den siste
                runtimeName = runtimeName.evalSubscription(subsc.get(i).eval(curScope), this);
            }
            RuntimeValue runtimeSubsc = subsc.get(subsc.size() - 1).eval(curScope);
            RuntimeValue runtimeExpr = expr.eval(curScope);
            runtimeName.evalAssignElem(runtimeSubsc, runtimeExpr, this);
            trace(runtimeName.showInfo() + runtimeSubsc.showInfo() + "==>" + runtimeExpr.showInfo());
        }
        return null;
    }
}
