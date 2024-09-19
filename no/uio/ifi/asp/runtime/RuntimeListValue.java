package no.uio.ifi.asp.runtime;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;
import java.util.*; //for arraylisten 

public class RuntimeListValue extends RuntimeValue{
    ArrayList<RuntimeValue> list = new ArrayList<>(); //RuntimeValue så vi kan ha forskjellige typer i listen

    public RuntimeListValue(ArrayList<RuntimeValue> l){
        list = l; 
    }

    @Override
    String typeName() {
	return "list";
    }
   
    public ArrayList<RuntimeValue> getList(){
        return list;
    }

    @Override
    public String showInfo() {
    return toString();
    }


    @Override 
    public String toString() {
        String str = "[";
        for (int i = 0; i < list.size(); i++){
            str += list.get(i).showInfo();
            if (i < (list.size() - 1)){
                str += ", ";
            }
        }
        str += "]";
        return str;
    }

    public boolean getBoolValue(String what, AspSyntax where) {
        return (!list.isEmpty()); 
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(list.size());
        }
    
    @Override
    public RuntimeValue evalNot(AspSyntax where){
        if (list.isEmpty()){
            return new RuntimeBoolValue(true);
        }
        return new RuntimeBoolValue(false);
    }
    
    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue){
            long size = v.getIntValue("* operand", where);
            ArrayList<RuntimeValue> newList = new ArrayList<>();
            for (int i = 0; i < size; i++){
                newList.addAll(list);
            }
            return new RuntimeListValue(newList); 
        }
        runtimeError("'*' undefined for "+typeName()+"!", where);
        return null;  // Required by the compiler!
    }

   
    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue){
            return new RuntimeBoolValue(true);
        }
        runtimeError("'!=' undefined for "+typeName()+"!", where);
        return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue){
            return new RuntimeBoolValue(false);
        }
        runtimeError("'==' undefined for "+typeName()+"!", where);
        return null;  // Required by the compiler!
        }
        
    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue){
            if (v.getIntValue("noe", where) < list.size()){
                return list.get((int)v.getIntValue("noe", where));
        }
    }
        runtimeError("Subscription '[...]' undefined for "+typeName()+"!", where);
        return null;  // Required by the compiler!
        
    }

    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where){
        if (inx instanceof RuntimeIntValue){
            if (inx.getIntValue("noe", where) < list.size()){
                list.set((int)inx.getIntValue("noe", where), val);
            }
        } else{
            runtimeError("assignElem '[...]' undefined for "+typeName()+"!", where);  // Required by the compiler!
        }
    }
}
