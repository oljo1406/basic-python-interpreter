package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


abstract class AspSmallStmt extends AspSyntax {
    AspSmallStmt(int n) {
        super(n);
    }
    
    static AspSmallStmt parse(Scanner s){
        enterParser("small stmt");
        AspSmallStmt ass = null;
        switch (s.curToken().kind){
            case nameToken:
                if (s.anyEqualToken()){
                    ass = AspAssignment.parse(s);   break;
                }
                ass = AspExprStmt.parse(s);   break;
            
            case returnToken:
                ass = AspReturnStmt.parse(s);   break;
            
            case passToken:
                ass = AspPassStmt.parse(s);   break;
            
            case globalToken:
                ass = AspGlobalStmt.parse(s);   break;
            
            default:
                parserError("Expected an expression small statment, but found a " + s.curToken().kind + "!", s.curLineNum());
        }
        leaveParser("small stmt");
        return ass;
    }
}
