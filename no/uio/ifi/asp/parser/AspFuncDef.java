package no.uio.ifi.asp.parser;
import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.runtime.RuntimeFunc;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFuncDef extends AspCompoundStmt{
    AspName an;
    ArrayList<AspName> names = new ArrayList<>();
    AspSuite as; 

    AspFuncDef(int n) {
        super(n);
    }

    public AspName getName(){
        return an; 
    }
    
    public ArrayList<AspName> getNameList(){
        return names;  
    }

    public AspSuite getSuite(){
        return as; 
    }
    

    public static AspFuncDef parse(Scanner s){
        enterParser("func def");
        AspFuncDef afd = new AspFuncDef(s.curLineNum()); 
        skip(s, defToken);
        afd.an = AspName.parse(s);
        skip(s, leftParToken);
        while(true){
            if(s.curToken().kind == rightParToken) break;
            afd.names.add(AspName.parse(s));
            if(s.curToken().kind == rightParToken) break;
            skip(s, commaToken);
        }
        skip(s, rightParToken);
        skip(s, colonToken);
        afd.as = AspSuite.parse(s);
        leaveParser("func def");
        return afd;
    }

    @Override
    public void prettyPrint() {
        int nPrinted = 0;
        prettyWrite("def ");
        an.prettyPrint();
        prettyWrite(" (");
        if (!names.isEmpty()){
            for(AspName name: names){
                name.prettyPrint(); ++nPrinted;
                if(nPrinted < names.size()){
                    prettyWrite(", ");
                }
            }
        }
        prettyWrite(")");
        prettyWrite(":");
        as.prettyPrint(); 

        prettyWriteLn(); //Gir et mellomrom mellom hver funksjon
        
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue function = new RuntimeFunc(this, curScope, an.name);
        curScope.assign(an.name, function);
        trace("def " + an.name);
        return function;
    }
}