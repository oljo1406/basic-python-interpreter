package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.RuntimeDictValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import java.util.Hashtable;
import java.util.Dictionary;

import java.util.ArrayList;


public class AspDictDisplay extends AspAtom{

    ArrayList<AspStringLiteral> stringLiterals = new ArrayList<AspStringLiteral>();
    ArrayList<AspExpr> exprrs = new ArrayList<AspExpr>();

    AspDictDisplay(int n){
        super(n);
    }

    public static AspDictDisplay parse(Scanner s){

        enterParser("dict display");

        AspDictDisplay addy = new AspDictDisplay(s.curLineNum());

        skip(s, leftBraceToken); 
        
        //dersom det ikke er en tom dict
        if(s.curToken().kind != rightBraceToken){
            while(true){
                addy.stringLiterals.add(AspStringLiteral.parse(s));
                skip(s, colonToken);
                addy.exprrs.add(AspExpr.parse(s));
                if (s.curToken().kind != commaToken) break;
                skip(s, commaToken);
            }
        }

        skip(s, rightBraceToken);

        leaveParser("dict display");
        
        return addy;
    }

    @Override
    public void prettyPrint() {
        int nPrinted = 0;

        prettyWrite("{");
        if(!stringLiterals.isEmpty()){ //dersom listen ikke er tom
            for(int i = 0; i < stringLiterals.size(); i++){
                if(nPrinted > 0){
                        prettyWrite(", ");
                }
                stringLiterals.get(i).prettyPrint();
                prettyWrite(":");
                exprrs.get(i).prettyPrint();
                nPrinted++;
            }
        }
        prettyWrite("}");     
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        Dictionary<String, RuntimeValue> dict = new Hashtable<String, RuntimeValue>();
        if (!stringLiterals.isEmpty()){
            int i = 0;
            for (AspStringLiteral strL: stringLiterals){
                AspExpr expr = exprrs.get(i);
                RuntimeValue v =  expr.eval(curScope);
                dict.put(strL.streng,v);
                i ++; 
        }
        }
        return new RuntimeDictValue(dict);
    }
}
