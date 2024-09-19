package no.uio.ifi.asp.runtime;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeDictValue extends RuntimeValue{
    Dictionary<String, RuntimeValue> dict = new Hashtable<String, RuntimeValue>();

    public RuntimeDictValue(Dictionary<String, RuntimeValue> d) {
	    dict = d;
    }

    @Override
    String typeName() {
	    return "dict";
    }

    @Override 
    public String toString() {
        String str = "{";
	    Enumeration<String> keys = dict.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            RuntimeValue value = dict.get(key);
            str += "'" + key + "'" + ": " + value.showInfo(); 
            str += ", ";
        }
        str = str.substring(0, str.length() - 2);
        str += "}";
        return str;
    }

    @Override
    public String showInfo() {
        return toString();
    }
    
    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
	    return (!dict.isEmpty());
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
        public RuntimeValue evalNot(AspSyntax where){
            if (dict.isEmpty()){
                return new RuntimeBoolValue(true);
            }
            return new RuntimeBoolValue(false);
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
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(dict.size());
        }
    
    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        String key; 
        if (v instanceof RuntimeIntValue){
            key = Integer.toString((int)v.getIntValue("noe", where));
            if(dict.get(key) != null){
                return dict.get(key);
            } else {
                runtimeError("Subscription '[...]' invalid key "+typeName()+"!", where);
            }
        }
        if (v instanceof RuntimeFloatValue){
            key = String.valueOf(v.getFloatValue("noe", where)); 
            if(dict.get(key) != null){
                return dict.get(key);
            } else {
                runtimeError("Subscription '[...]' invalid key "+typeName()+"!", where);
            }
        }
        if (v instanceof RuntimeStringValue){
            key = v.getStringValue("noe", where);
            if(dict.get(key) != null){
                return dict.get(key);
            } else {
                runtimeError("Subscription '[...]' invalid key "+typeName()+"!", where);
            }
        }
        
        runtimeError("Subscription '[...]' undefined for "+typeName()+"!", where);
        return null;  // Required by the compiler!
    }
    
    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where){
        String key; 
        if (inx instanceof RuntimeIntValue){
            key = Integer.toString((int)inx.getIntValue("noe", where));
            if(dict.get(key) != null){
                dict.put(key, val);
            } else {
                runtimeError("assignElem '[...]' key has no value "+typeName()+"!", where);
            }
        }
        if (inx instanceof RuntimeFloatValue){
            key = String.valueOf(inx.getFloatValue("noe", where)); 
            if(dict.get(key) != null){
                dict.put(key, val);
            } else {
                runtimeError("assignElem '[...]' key has no value "+typeName()+"!", where);
            }
        }
        if (inx instanceof RuntimeStringValue){
            key = inx.getStringValue("noe", where);
            if(dict.get(key) != null){
                dict.put(key, val);
            } else {
                runtimeError("assignElem '[...]' key has no value "+typeName()+"!", where);
            }
        }
        runtimeError("assignElem '[...]' undefined for "+typeName()+"!", where);  // Required by the compiler!
    }
}
