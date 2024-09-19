package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeStringValue extends RuntimeValue{
    boolean boolValue; 
    String stringValue;

    public RuntimeStringValue(String v) {
	    stringValue = v;
    }

    @Override
    String typeName() {
	    return "string";
    }

    @Override
    public String showInfo() {
        if (stringValue.indexOf("\'") >= 0){
            return '"' + stringValue + '"';
        }
        else{
            return "'" + stringValue + "'";
        }
    }

    @Override 
    public String toString() {
	    return stringValue;
    }

    @Override
    public String getStringValue(String what, AspSyntax where){
        return stringValue;
    }

   @Override
    public boolean getBoolValue(String what, AspSyntax where){
        return stringValue != "";
    }

    //Override-er alle metodene som er lovlige operasjoner på en float
    
    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) { 
        if (v instanceof RuntimeNoneValue) { 
            return new RuntimeBoolValue(false);
        }
        else if (v instanceof RuntimeStringValue){
            return new RuntimeBoolValue(stringValue.equals(v.getStringValue("== operand", where))); 
        }
        runtimeError("Type error for ==.", where); 
        return null;  // Required by the compiler
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
	    return new RuntimeBoolValue(stringValue == "");
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true); 
        }
        else if (v instanceof RuntimeStringValue){
            return new RuntimeBoolValue(stringValue != v.getStringValue("!= operand", where)); 
        }
	    runtimeError("Type error for !=.", where); 
	    return null;  // Required by the compiler
    }

    //Håndtere string + string
    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where){
        //Hvis det er string + string
        if (v instanceof RuntimeStringValue){
            return new RuntimeStringValue(stringValue + v.getStringValue("+ operand", where)); 
        } 
        runtimeError("Type error for +.", where);
        return null;  // Required by the compiler
    }

    //Håndterer string * int
    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where){
        //Hvis det er en string * int 
        if(v instanceof RuntimeIntValue){
            String newString = ""; 
            for(int i = 0; i < v.getIntValue("* operand", where); i++){
                newString += stringValue;
            }
            return new RuntimeStringValue(newString);
        } 
        runtimeError("Type error for *.", where);
	    return null;  // Required by the compiler
    }

    //Håndtere string < string
    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where){
        //Hvis det er string < string
        if(v instanceof RuntimeStringValue){
            return new RuntimeBoolValue(stringValue.compareTo(v.getStringValue("< operand", where)) < 0); //TODO: kan være feil bruk av < 0
        }
        runtimeError("Type error for <.", where);
	    return null;  // Required by the compiler
    }

    //Håndtere string <= string
    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where){
        //Hvis det er string <= string
        if(v instanceof RuntimeStringValue){
            return new RuntimeBoolValue(stringValue.compareTo(v.getStringValue("<= operand", where)) <= 0); //TODO: kan være feil bruk av <= 0
        } 
        runtimeError("Type error for <=.", where);
	    return null;  // Required by the compiler
    }

    //Håndtere string > string
    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where){
        //Hvis det er string > string
        if(v instanceof RuntimeStringValue){
            return new RuntimeBoolValue(stringValue.compareTo(v.getStringValue("> operand", where)) > 0); //TODO: kan være feil bruk av > 0
        } 
        runtimeError("Type error for >.", where);
	    return null;  // Required by the compiler 
    }

    //Håndtere string >= string
    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where){
        //Hvis det er string >= string
        if(v instanceof RuntimeStringValue){
            return new RuntimeBoolValue(stringValue.compareTo(v.getStringValue(">= operand", where)) >= 0); //TODO: kan være feil bruk av > 0
        } 
        runtimeError("Type error for >=.", where);
	    return null;  // Required by the compiler
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        char letter = ' ';
        if (v instanceof RuntimeIntValue){
            if (v.getIntValue("noe", where) < stringValue.length()){
                letter = stringValue.charAt((int)v.getIntValue("noe", where));
                return new RuntimeStringValue(Character.toString(letter)); 
            }
        }
        runtimeError("Subscription '[...]' undefined for "+typeName()+"!", where);
        return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where){
        return new RuntimeIntValue((long)stringValue.length());
    }
 }


