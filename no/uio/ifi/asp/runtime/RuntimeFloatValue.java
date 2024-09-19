package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;
import java.lang.Math; 

public class RuntimeFloatValue extends RuntimeValue{
    double floatValue; 

    public RuntimeFloatValue(double v) {
	    floatValue = v;
    }

    @Override
    String typeName() {
	    return "float";
    }

    @Override 
    public String toString() {
	    return Double.toString(floatValue);
    }

    public boolean getBoolValue(String what, AspSyntax where){
        return floatValue != 0.0;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
	    return floatValue;
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
	    return (long)floatValue;
    }

    //Override-er alle metodene som er lovlige operasjoner på en float
    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) { 
        if (v instanceof RuntimeNoneValue) { 
            return new RuntimeBoolValue(false);
        }
        if (v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(floatValue == v.getFloatValue("== operand", where));
        }
        if (v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(floatValue == v.getFloatValue("== operand", where));
        }
        runtimeError("'==' undefined for "+typeName()+"!", where);
	    return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
	    return new RuntimeBoolValue(floatValue == 0.0); 
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }
        else if (v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(floatValue != v.getFloatValue("not operand", where));
        }
        else if (v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(floatValue != v.getFloatValue("not operand", where));
        }
        runtimeError("'!=' undefined for "+typeName()+"!", where);
        return null;  // Required by the compiler!
    }

    //Håndterer +float
    @Override
    public RuntimeValue evalPositive(AspSyntax where){
        return new RuntimeFloatValue(floatValue); 
    }

    //Håndterer -float
    @Override
    public RuntimeValue evalNegate(AspSyntax where){
        return new RuntimeFloatValue(-1 * floatValue); 
    }

    //Håndtere float + float og/eller int + float 
    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where){
        //Hvis det er en float + float
        if (v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(floatValue + v.getFloatValue("+ operand", where)); 
            //Hvis det er en float + int
        } else if (v instanceof RuntimeIntValue){
            return new RuntimeFloatValue(floatValue + v.getFloatValue("+ operand", where));
        } else {
            //Alt annet skal gi en error 
            runtimeError("Type error for +.", where);
	        return null;  // Required by the compiler
        }
    }

    //Håndterer float - float og/eller float - int 
    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where){
        //Hvis det er en float - float
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(floatValue - v.getFloatValue("- operand", where)); 
            //Hvis det er float - int 
        } else if(v instanceof RuntimeIntValue){
            return new RuntimeFloatValue(floatValue - v.getFloatValue("- operand", where));
        } else {
            //Alt annet skal gi en error 
            runtimeError("Type error for -.", where);
	        return null;  // Required by the compiler
        }
    }

    //Håndterer float * float og/eller float * int
    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where){
        //Hvis det er float * float 
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(floatValue * v.getFloatValue("* operand", where));
            //Hvis det er float * int 
        } else if(v instanceof RuntimeIntValue){
            return new RuntimeFloatValue(floatValue * v.getFloatValue("* operand", where));
        } else {
            //Ant annet skal gi error
            runtimeError("Type error for *.", where);
	        return null;  // Required by the compiler
        }
    }

    //Håndterer float / float og/eller float / int
    @Override 
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where){
        //Hvis det er float / float 
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(floatValue / v.getFloatValue("/ operand", where));
            //Hvis det er float / int 
        } else if (v instanceof RuntimeIntValue){
            return new RuntimeFloatValue(floatValue / v.getFloatValue(null, where));
        } else {
            //Alt annet skal gi error
            runtimeError("Type error for /.", where);
	        return null;  // Required by the compiler
        }
    }

    //Håndterer float // float og/eller float // int 
    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where){
        //Hvis det float // float 
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(Math.floor(floatValue / v.getFloatValue("// operand", where)));
            //Hvis det er float // int 
        } else if (v instanceof RuntimeIntValue){
            return new RuntimeFloatValue(Math.floor(floatValue / v.getFloatValue("// operand", where)));
        } else {
            //Alt annet skal gi error 
            runtimeError("Type error for //.", where);
	        return null;  // Required by the compiler
        }
    }

    //Håndterer float % float og/eller float % int 
    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where){
        //Hvis det er float % float 
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(floatValue - v.getFloatValue("% operand", where) * Math.floor(floatValue / v.getFloatValue("% operand", where)));
            //Hvis det er float % int 
        } else if (v instanceof RuntimeIntValue){
            return new RuntimeFloatValue(floatValue - v.getFloatValue("% operand", where) * Math.floor(floatValue / v.getFloatValue("% operand", where)));
        } else {
            //Alt annet skal gi error 
            runtimeError("Type error for %.", where);
	        return null;  // Required by the compiler
        }
    }

    //Håndterer float < float og/eller float < int 
    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where){
       //Hvis det er float < float 
       if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(floatValue < v.getFloatValue("< operand", where));
            //Hvis det er float < int 
       } else if (v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(floatValue < v.getFloatValue("< operand", where));
       } else {
            //Alt annet skal gi error 
            runtimeError("Type error for <.", where);
	        return null;  // Required by the compiler
        }
    }

    //Håndterer float <= float og/eller float <= int
    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where){
        //Hvis det er float <= float 
       if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(floatValue <= v.getFloatValue("<= operand", where));
            //Hvis det er float <= int 
       } else if (v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(floatValue <= v.getFloatValue("<= operand", where));
       } else {
            //Alt annet skal gi error 
            runtimeError("Type error for <=.", where);
	        return null;  // Required by the compiler
        }
    }

    //Håndterer float > float og/eller float > int
    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where){
        //Hvis det er float > float 
       if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(floatValue > v.getFloatValue("> operand", where));
            //Hvis det er float > int 
       } else if (v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(floatValue > v.getFloatValue("> operand", where));
       } else {
            //Alt annet skal gi error 
            runtimeError("Type error for >.", where);
	        return null;  // Required by the compiler
        }
    }

    //Håndterer float >= float og/eller float >= int
    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where){
        //Hvis det er float >= float 
       if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(floatValue >= v.getFloatValue(">= operand", where));
            //Hvis det er float >= int 
       } else if (v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(floatValue >= v.getFloatValue(">= operand", where));
       } else {
            //Alt annet skal gi error 
            runtimeError("Type error for >=.", where);
	        return null;  // Required by the compiler
        }
    }
}
