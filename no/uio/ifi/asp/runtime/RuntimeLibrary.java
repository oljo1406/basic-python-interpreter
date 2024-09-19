// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {

    // input
    assign("input", new RuntimeFunc("input") {
        @Override
        public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
            checkNumParams(actualParams, 1, "input", where);
            System.out.print(actualParams.get(0).toString());
            String v1 = keyboard.nextLine();
            return new RuntimeStringValue(v1);
            }
        });

    // len
        assign("len", new RuntimeFunc("len") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "len", where);
                return actualParams.get(0).evalLen(where);
            }
        });

        // print
        assign("print", new RuntimeFunc("print") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                for (int i = 0; i < actualParams.size(); ++i) {
                    if (i > 0) System.out.print(" ");
                    System.out.print(actualParams.get(i).toString());
                }
                System.out.println();
                return new RuntimeNoneValue();
            }
        });

        //int 
        assign("int", new RuntimeFunc("int") { 
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "int", where);
                RuntimeValue toInt = actualParams.get(0); 
                if (toInt instanceof RuntimeIntValue){
                    return toInt; 
                }
                if (toInt instanceof RuntimeFloatValue){
                    long number = toInt.getIntValue("int", where);
                    return new RuntimeIntValue(number); 
                }
                if (toInt instanceof RuntimeStringValue){
                    String stringNumber = toInt.getStringValue("int", where);
                    long number = Long.parseLong(stringNumber);
                    return new RuntimeIntValue(number); 
                } 
                RuntimeValue.runtimeError(toInt.toString() + "is not a int, float or string", where);
                return null;
            }
        });

         //float 
         assign("float", new RuntimeFunc("float") { 
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "float", where);
                RuntimeValue toFloat = actualParams.get(0); 
                if (toFloat instanceof RuntimeFloatValue){
                    return toFloat; 
                }
                if (toFloat instanceof RuntimeIntValue){
                    double number = toFloat.getFloatValue("float", where);
                    return new RuntimeFloatValue(number); 
                }
                if (toFloat instanceof RuntimeStringValue){
                    String stringNumber = toFloat.getStringValue("floats", where);
                    double number = Double.parseDouble(stringNumber);
                    return new RuntimeFloatValue(number); 
                } 
                RuntimeValue.runtimeError(toFloat.toString() + "is not a int, float or string", where);
                return null;
            }
        });


        //string 
        assign("str", new RuntimeFunc("str") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "str", where);
                return new RuntimeStringValue(actualParams.get(0).showInfo()); 
            }
        });

        // range
        assign("range", new RuntimeFunc("range") {
            @Override public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 2, "range", where);
                RuntimeValue v1 = actualParams.get(0);
                RuntimeValue v2 = actualParams.get(1);
                ArrayList<RuntimeValue> liste = new ArrayList<RuntimeValue>();
                if (v1 instanceof RuntimeIntValue && v2 instanceof RuntimeIntValue){
                    for (int i = (int)v1.getIntValue("int", where); i < (int)v2.getIntValue("int", where); i++){
                        liste.add(new RuntimeIntValue(i)); 
                    }
                    return new RuntimeListValue(liste);
                }
                RuntimeValue.runtimeError(v1.toString() + " and/or " + v2.toString() + "is not a int", where);
                return null;
            }
            
        });
    }


    private void checkNumParams(ArrayList<RuntimeValue> actArgs, 
				int nCorrect, String id, AspSyntax where) {
	if (actArgs.size() != nCorrect)
	    RuntimeValue.runtimeError("Wrong number of parameters to "+id+"!",where);
    }
}
