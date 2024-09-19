package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspPrimary extends AspSyntax {
    
    AspAtom atom;
    ArrayList<AspPrimarySuffix> primarySuffixs = new ArrayList<AspPrimarySuffix>();

    AspPrimary(int n) {
	    super(n);
    }


    public static AspPrimary parse(Scanner s) {
        enterParser("primary");

        
        AspPrimary ap = new AspPrimary(s.curLineNum());
        
        ap.atom = AspAtom.parse(s);

        if(s.curToken().kind != leftParToken && s.curToken().kind != leftBracketToken){
            leaveParser("primary");
            return ap;
        }

        while(true){
            ap.primarySuffixs.add(AspPrimarySuffix.parse(s));
            if(s.curToken().kind != leftParToken || s.curToken().kind != leftBracketToken) break;
        }

        leaveParser("primary");
        return ap;

    }


    @Override
    public void prettyPrint() {

        atom.prettyPrint();

        if(!primarySuffixs.isEmpty()){
           for(AspPrimarySuffix primarySuffix: primarySuffixs){
                primarySuffix.prettyPrint();
            } 
        }       
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = atom.eval(curScope); //vil alltid være 1 atom
        if(!primarySuffixs.isEmpty()){
            for(AspPrimarySuffix primarySuffix: primarySuffixs){
                if (atom instanceof AspStringLiteral || atom instanceof AspListDisplay || atom instanceof AspDictDisplay || atom instanceof AspName){
                    if (primarySuffix instanceof AspSubscription){
                        v = v.evalSubscription(primarySuffix.eval(curScope), this);
                    } 
                } 

                if(v instanceof RuntimeFunc){
                    ArrayList<RuntimeValue> list = new ArrayList<RuntimeValue>();
                    ArrayList<String> traceList = new ArrayList<String>();
                    for(AspPrimarySuffix arg : primarySuffixs){
                        //Vil allid være en RuntimeListValue
                        RuntimeListValue runtimeListValue = (RuntimeListValue) arg.eval(curScope);
                        //Arraylisten vil holde på de ulike argumentene 
                        ArrayList<RuntimeValue> runtimeList = runtimeListValue.getList();
                        for(RuntimeValue value : runtimeList){
                            list.add(value);
                            traceList.add(value.showInfo());

                        }
                    }

                    trace("Call function " + v.toString() + " with params " + traceList);
                    v = v.evalFuncCall(list, this);
                } 
                }
            }
        return v;
    }
}
