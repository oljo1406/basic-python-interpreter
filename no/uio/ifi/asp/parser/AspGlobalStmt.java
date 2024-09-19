package no.uio.ifi.asp.parser;
import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspGlobalStmt extends AspSmallStmt{
    ArrayList<AspName> nvn = new ArrayList<>();

    AspGlobalStmt(int n){
        super(n); 
    }

    public static AspGlobalStmt parse(Scanner s){
        enterParser("global stmt");
        AspGlobalStmt ags = new AspGlobalStmt(s.curLineNum());
        skip(s, globalToken);
        while(true){
            ags.nvn.add(AspName.parse(s));
            if (s.curToken().kind != commaToken) break;
            skip(s, commaToken);
            }
        leaveParser("global stmt");
        return ags; 
    }

    @Override
    public void prettyPrint() {
        int nPrinted = 0;
        prettyWrite("global ");
        for(AspName navn: nvn){
            if(nPrinted > 0){
                prettyWrite(", ");
            }
            navn.prettyPrint(); ++nPrinted;
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
     for (int i = 0; i < nvn.size(); i ++) {
            if (curScope.hasDefined(nvn.get(i).name)) {
                RuntimeValue.runtimeError("Name already assigned in current scope!", this);
            }
            if (curScope.hasGlobalName(nvn.get(i).name)) {
                RuntimeValue.runtimeError("Name already assigned in global scope!", this);
            }
            curScope.registerGlobalName(nvn.get(i).name);
        }
	    return new RuntimeNoneValue();    
    }

}
