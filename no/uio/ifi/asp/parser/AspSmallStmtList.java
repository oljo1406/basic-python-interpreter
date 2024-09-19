package no.uio.ifi.asp.parser;
import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSmallStmtList extends AspStmt {
    ArrayList<AspSmallStmt> smallstmts = new ArrayList<>();
    static boolean colonSist = false;
    AspSmallStmtList(int n) {
        super(n);
    }
    public static AspSmallStmtList parse(Scanner s){
        enterParser("small stmt list");
        AspSmallStmtList assl = new AspSmallStmtList(s.curLineNum()); 
        assl.smallstmts.add(AspSmallStmt.parse(s));
        
        while(true){
            if (s.curToken().kind == newLineToken) break; //colonSist = true; 
            skip(s, semicolonToken);
            if (s.curToken().kind == newLineToken) {
                colonSist = true; 
                break; 
            }
            assl.smallstmts.add(AspSmallStmt.parse(s));
        }

        skip(s, newLineToken);
        leaveParser("small stmt list");
        return assl; 

    }
    @Override
    public void prettyPrint() {
        int nPrinted = 0;
        for(AspSmallStmt smalstmt: smallstmts){
            if(nPrinted > 0){
                
                prettyWrite("; "); 
                
            }
            smalstmt.prettyPrint(); ++nPrinted;
        }
        prettyWriteLn();

    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = null;
        if (!smallstmts.isEmpty()) {
            for (int i = 0; i < smallstmts.size(); i++) {
                v = smallstmts.get(i).eval(curScope);
            }
        }
    return v;
    }
}
