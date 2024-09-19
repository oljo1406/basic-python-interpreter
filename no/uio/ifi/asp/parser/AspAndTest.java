package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspAndTest extends AspSyntax{
    
    ArrayList<AspNotTest> notTests = new ArrayList<AspNotTest>();
    
    AspAndTest(int n) {
        super(n);
    }


    public static AspAndTest parse(Scanner s) {
        enterParser("and test");

        AspAndTest aat = new AspAndTest(s.curLineNum());

        while(true){
            aat.notTests.add(AspNotTest.parse(s));
            if (s.curToken().kind != andToken) break;
            skip(s, andToken);
        }

        leaveParser("and test");
        return aat;
    }


    @Override
    public void prettyPrint() {
        int nPrinted = 0;

        for(AspNotTest ant: notTests){
            if(nPrinted > 0){
                prettyWrite(" and ");
            }
            ant.prettyPrint(); ++nPrinted;
        }

    }
    
    //Ifølge kompendiumet skal v1 and v2, ..., vn beregnes slik:
        //1. Her evalueres operandene v1, v2 etc inntil man finner en operand som er False.
        //2. Hvis ingen v-i er False, er resultatet siste operand vn (uansett om vn er True eller False).

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = notTests.get(0).eval(curScope);

        for(int i = 1; i < notTests.size(); i++){
            if(v.getBoolValue(" and ", this) == false){
                return v;
            }
            v = notTests.get(i).eval(curScope);
        }
        //Alle notTests er true, og returnerer siste uansett
        return v;
    }
}
