// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspExpr extends AspSyntax {
    
    ArrayList<AspAndTest> andTests = new ArrayList<>();

    AspExpr(int n) {
	    super(n);
    }


    public static AspExpr parse(Scanner s) {
        enterParser("expr");

        
        AspExpr ae = new AspExpr(s.curLineNum());

        while(true){
            ae.andTests.add(AspAndTest.parse(s));
            if(s.curToken().kind != orToken) break;
            skip(s, orToken);
        }


        leaveParser("expr");
        return ae;
    }


    @Override
    public void prettyPrint() {
	
        int nPrinted = 0;

        for(AspAndTest andTest: andTests){
            if(nPrinted > 0){
                prettyWrite(" or "); 
            }
            andTest.prettyPrint(); ++nPrinted;
        }
    }


    //Ifølge kompendiumet skal v1 and v2, ..., vn beregnes slik:
    //1. Her evalueres operandene v1, v2 etc inntil man finner en operand som er True.
    //2. Hvis ingen v-i er True, er resultatet siste operand vn (uansett om vn er True eller False).

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = andTests.get(0).eval(curScope);

        for(int i = 1; i < andTests.size(); i++){
            if(v.getBoolValue(" and ", this) == true){
                return v;
            }
            v = andTests.get(i).eval(curScope);
        }
        //Alle andTests er true, og returnerer siste uansett
        return v;
    }
}
