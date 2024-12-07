package de.gfed.AoC_2024.days;

import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.*;

public class DaySeven extends Day {

    public DaySeven(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 7);
        expectations=new long[]{663613490587L, 110365987435001L};
        example = Arrays.asList(
                "190: 10 19",
                "3267: 81 40 27",
                "83: 17 5",
                "156: 15 6",
                "7290: 6 8 6 15",
                "161011: 16 10 13",
                "192: 17 8 14",
                "21037: 9 7 18 13",
                "292: 11 6 16 20");
        /*
        Part 1: Each line consists of a result and a number of operands. Can the result be correct using the operands,
        +, * and strict left-to-right? Add correct results.
        Part 2: What if there is additional concatenation of the next two operands or the next operand and the current result?
         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        long result =0;
        for (String line : input) {
            result+=validResult(line,bPart2);
        }
        return result;
    }

    private long validResult(String input, boolean bPart2){
        Stack <Long> operands = new Stack<>();
        String [] operandsArray=input.substring(input.indexOf(":") + 2).split(" ");
        for (int i =operandsArray.length-1; i>-1; i--) {
            operands.push(Long.parseLong(operandsArray[i]));
        }
        long currentOperand=operands.pop();
        return validResultRec(Long.parseLong(input.substring(0,input.indexOf(":"))), currentOperand , operands,bPart2);
    }

    private long validResultRec(long result, long intermediateResult, Stack<Long> operands, boolean bPart2){
        if (operands.empty())
            return result==intermediateResult?result:0;
        long currentOperand=operands.pop();

        if(validResultRec(result, intermediateResult + currentOperand, (Stack<Long>) operands.clone(), bPart2)==result
                || validResultRec(result, intermediateResult * currentOperand, (Stack<Long>) operands.clone(), bPart2)==result)
            return result;

        if (!bPart2)
            return 0;

        if (validResultRec(result, Long.parseLong(intermediateResult +""+ currentOperand), (Stack<Long>) operands.clone(),bPart2)==result)
            return result;

        if (operands.empty())
            return 0;

        currentOperand=Long.parseLong(currentOperand +"" + operands.pop());
        operands.push(currentOperand);
        if (validResultRec(result, intermediateResult, (Stack<Long>) operands.clone(), bPart2)==result)
            return result;
        return 0;
    }
}