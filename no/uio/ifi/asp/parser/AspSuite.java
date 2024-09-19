package no.uio.ifi.asp.parser;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;
import java.util.ArrayList;

public class AspSuite extends AspSyntax {
    ArrayList<AspStmt> stmts = new ArrayList<>();
    AspSmallStmtList assl;
    AspSuite(int n) {
        super(n);
    }

    public static AspSuite parse(Scanner s){
        enterParser("suite");
        AspSuite as = new AspSuite(s.curLineNum()); 
        if (s.curToken().kind == newLineToken){
            skip(s, newLineToken);
            skip(s, indentToken);
            while(true){
                if (s.curToken().kind == dedentToken) break;
                as.stmts.add(AspStmt.parse(s)); 
            }
            skip(s, dedentToken);
        } else {
            as.assl = AspSmallStmtList.parse(s);
        }
        leaveParser("suite");
        return as; 
    }

    @Override
    public void prettyPrint() {
        if (assl == null){
            prettyWriteLn("");
            prettyIndent();
            for (AspStmt stmt: stmts){
                stmt.prettyPrint();
            }
            prettyDedent();
        } else {
            assl.prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        if(!stmts.isEmpty()){
            for (AspStmt elem : stmts) {
                elem.eval(curScope);
            }
        } else {
            assl.eval(curScope);
        }
        return null;
    }
}
