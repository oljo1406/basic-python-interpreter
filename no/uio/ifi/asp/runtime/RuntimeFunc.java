package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.parser.AspFuncDef;
import no.uio.ifi.asp.parser.AspName;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeFunc extends RuntimeValue{
    
    AspFuncDef def;
    RuntimeScope defScope;
    String name;

    public RuntimeFunc(AspFuncDef func, RuntimeScope scope, String n){
        def = func;
        defScope = scope;
        name = n;
    }

    public RuntimeFunc(String n){
        name = n;
    }

    public String typeName(){
        return "function";
    }

    public String toString(){
        return name;
    }

    @Override
    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actPars, AspSyntax where) {
        try{
            if(def.getNameList().size() == actPars.size()){
                RuntimeScope newScope = new RuntimeScope(defScope);
                for(int i = 0; i < def.getNameList().size(); i++){
                    newScope.assign(def.getNameList().get(i).name, actPars.get(i));
                }
                def.getSuite().eval(newScope);
            } else {
                RuntimeValue.runtimeError("Number of args do not match", where);
            } 
        } catch (RuntimeReturnValue rrv) {
                return rrv.value;
            }
        return new RuntimeNoneValue();
    }
}
