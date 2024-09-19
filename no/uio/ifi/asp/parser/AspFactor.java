package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspFactor extends AspSyntax {
    
    ArrayList<AspFactorPrefix> factorPrefixs = new ArrayList<AspFactorPrefix>();
    ArrayList<AspPrimary> primaries = new ArrayList<AspPrimary>();
    ArrayList<AspFactorOpr> factorOprs = new ArrayList<AspFactorOpr>();

    AspFactor(int n) {
	    super(n);
    }


    public static AspFactor parse(Scanner s) {
        enterParser("factor");

        
        AspFactor af = new AspFactor(s.curLineNum());

        while(true){
            if(s.isFactorPrefix()){
                af.factorPrefixs.add(AspFactorPrefix.parse(s));
            }else {
                af.factorPrefixs.add(null); 
            }

            af.primaries.add(AspPrimary.parse(s));

            if(!s.isFactorOpr()) break;

            af.factorOprs.add(AspFactorOpr.parse(s));
            
        }

        leaveParser("factor");
        return af;
    }

    @Override
    public void prettyPrint() {
        for (int i = 0; i < primaries.size(); i++) {
            //if (i < factorPrefixs.size()) {
            if (factorPrefixs.get(i) != null) {
                factorPrefixs.get(i).prettyPrint();
            }
            primaries.get(i).prettyPrint();
            if (i < factorOprs.size()) {
                factorOprs.get(i).prettyPrint();
            }
        } 
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = null;

        for (int i = 0; i < primaries.size(); i++) {
            RuntimeValue v2 = primaries.get(i).eval(curScope);
            if (factorPrefixs.get(i) != null) {
                String factorPrefix = factorPrefixs.get(i).factorPrefix;
                if (factorPrefix == "+") {
                    v2 = v2.evalPositive(this);
                }
                else if (factorPrefix == "-") {
                    v2 = v2.evalNegate(this);
                }
                else {
                    Main.panic("Illegal prefix operator!");
                }
            }
            if (i == 0) {
                v = v2;
            }
            else {
                String factorOpr = factorOprs.get(i -1).factorOpr;
                switch (factorOpr) {
                    case " * ":
                        v = v.evalMultiply(v2, this);
                        break;
    
                    case " / ":
                        v = v.evalDivide(v2, this);
                        break;
    
                    case " // ": 
                        v = v.evalIntDivide(v2, this);
                        break;
    
                    case " % ":
                        v = v.evalModulo(v2, this);
                        break;
                        
                    default:
                    Main.panic("Illegal factor opr : " + factorOpr);   
                }
            }
        }
        return v;
    }
}
