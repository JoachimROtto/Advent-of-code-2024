package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.Arrays;
import java.util.List;

public class DaySeventeen extends Day {

    public DaySeventeen(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 17);
        expectations=new long[]{0,109685330781408L}; //2,1,0,4,6,2,4,2,0 ,
        example = Arrays.asList(
                "Register A: 117440",
                "Register B: 0",
                "Register C: 0" ,
                "",
                "Program: 0,3,5,4,3,0");
        /*
        Part 1: There are 3 registers and a program consisting of alternating op-code and operand. A program
        is started, performs the calculation and generates an output. Wich output?
        Part 2; Register A can now be chosen. What is the minimum value that ensures that the output and
        the program are identical?
        */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        return eval(input, bPart2); //1928
    }

    private long eval(List<String> input, boolean bPart2){
        if (bPart2){
            // The length of the result grows in steps of Register A (A>= 8^0->2, 8^1->4, 8^2->6 .....)
            // correct length is 32 -> 8^15-8^16; result of steps with 1/10/100/1000 shows tail result is
            // mostly unchanged, so start steps with 100000000 and search for last 5 characters (incl. ,)
            // in common ->109401472088832, smaller steps greater match ->109685330781382L
            for (long j = 109685330781382L; j <(8*35184372088932L)  ; j+=1) {
                //String result =compute(input,j);
                // System.out.println(j + " " + result.length());
                // System.out.println(result);

                if (compute(input,j).substring(1).equals(input.get(4).substring(9)))
                    return j;

                //   if (result.substring(result.length()-27).equals(input.get(4).substring(input.get(4).length()-27)))
                //   System.out.println(j+ ": " +result.substring(1));

            }
        }
        if(!bPart2)
            System.out.println(compute(input, -1).substring(1));
        return 0;
    }

    private String compute (List<String> input, long registerA){
        StringBuilder result=new StringBuilder();
        String[] program= input.get(4).replace("Program: ", "").split(",");
        long[] register= new long[]{
                Integer.parseInt(input.get(0).replace("Register A: ","")),
                Integer.parseInt(input.get(1).replace("Register B: ","")),
                Integer.parseInt(input.get(2).replace("Register C: ","")),
                -1,
                -1
        };
        if ( registerA!=-1 )
            register[0]=registerA;

        for (long i = 0; i < program.length; i+=2) {
            register = applyInstruction(program[(int)i], Integer.parseInt(program[(int)i+1]), register);
            if (register[3]!=-1){
                i=register[3]-2;
                register[3]=-1;
            }
            if (register[4]!=-1){
                result.append(",").append(register[4]);
                register[4]=-1;
            }
        }
        return result.toString();
    }

    private long[] applyInstruction(String opcode, int operand, long[] register) {
        long newOperand=(operand<7 && operand>3)?register[operand-4]:operand;
        switch (opcode){
            case "0"-> register[0]>>=newOperand;
            case "1"-> register[1]^=operand;
            case "2"-> register[1]= newOperand%8;
            case "3"-> {
                if (register[0]!=0)
                    register[3]=operand;
            }
            case "4"-> register[1]^=register[2];
            case "5"-> register[4]=newOperand%8;
            case "6"-> register[1]= register[0]>>newOperand;
            case "7"->register[2]= register[0]>>newOperand;
        }
        return register;
    }
}