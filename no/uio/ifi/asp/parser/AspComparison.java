package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspComparison extends AspSyntax{
    
    ArrayList<AspTerm> terms = new ArrayList<AspTerm>();
    ArrayList<AspCompOpr> compOprs = new ArrayList<AspCompOpr>();
    
    
    AspComparison(int n) {
        super(n);
    }


    public static AspComparison parse(Scanner s) {
        enterParser("comparison");

        AspComparison ac = new AspComparison(s.curLineNum());

        ac.terms.add(AspTerm.parse(s));

        if(!s.isCompOpr()){
            leaveParser("comparison");
            return ac; //bare en term og ingen comp opr    
        }
        
        while(true){
            ac.compOprs.add(AspCompOpr.parse(s));
            ac.terms.add(AspTerm.parse(s));
            if(!s.isCompOpr()){
                break; //bare en term og ingen comp opr 
            }
        }

        leaveParser("comparison");
        return ac;
    }


    @Override
    public void prettyPrint() {
        int i = 0;
        for (AspTerm term : terms) {
            term.prettyPrint();
            if (i < compOprs.size()) {
                compOprs.get(i).prettyPrint();
                i++;
            }
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = null;
        if(compOprs.isEmpty()){
            v = terms.get(0).eval(curScope);
            return v;
        }
        int i = 0;
        int j = i + 1;
        while(i < compOprs.size()){
            RuntimeValue t1 = terms.get(i).eval(curScope); //i = 0
            RuntimeValue t2 = terms.get(j).eval(curScope); //j = i + 1 -> 1
            String compOpr = compOprs.get(i).compOpr;

            if(compOpr == " < "){
                v = t1.evalLess(t2, this);
            } else if(compOpr == " > "){
                v = t1.evalGreater(t2, this);
            } else if(compOpr == " == "){
                v = t1.evalEqual(t2, this);
            } else if(compOpr == " >= "){
                v = t1.evalGreaterEqual(t2, this);
            } else if(compOpr == " <= "){
                v = t1.evalLessEqual(t2, this);
            } else{
                v = t1.evalNotEqual(t2, this);
            }

            i++;
            j++;

            if(v.getBoolValue("comparison", this) == false){
                return v;
            } 
        }
        return v; //alle er true
    }
}
