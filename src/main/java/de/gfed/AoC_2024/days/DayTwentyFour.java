package de.gfed.AoC_2024.days;


import de.gfed.AoC_2024.AoCInputConnector;
import de.gfed.AoC_2024.Day;

import java.util.*;

public class DayTwentyFour extends Day {
    Set<String> part2Log=new HashSet<>();
    public DayTwentyFour(boolean debugMode, AoCInputConnector inputConnector) {
        super(debugMode, inputConnector, 24);
        expectations=new long[]{69201640933606L,0}; //dhq,hbs,jcp,kfp,pdg,z18,z22,z27
        example = Arrays.asList(
                "x00: 1",
                "x01: 1",
                "x02: 1",
                "y00: 0",
                "y01: 1",
                "y02: 0",
                "",
                "x00 AND y00 -> z00",
                "x01 XOR y01 -> z01",
                "x02 OR y02 -> z02");
        /*
            Part1: The input is a list of inputs and a list of linked inputs to intermediate values or outputs.
            As a result zNN is a binary representation of a number. Which one?
            Part 2: xNN and yNN are also numbers and zNN should actually be the sum. But unfortunately the result
            is wrong because 4 pairs of outputs are swapped. Which 2*4 outputs are these?

         */
    }

    @Override
    protected long evalInput(boolean bPart2) {
        Map<String, String> substitution=new HashMap<>();
        List<String[]> gateSetting =getGateSettings(input, substitution);
        Map<String, Boolean> gateInput= computeGate(gateSetting, getGatesInput(input));

        if (bPart2){
            gateSetting =getGateSettings(input, substitution);
            List<String> wrongOutput = getWrongZs(gateInput);
            for (String[] setting : gateSetting) {
                if(!setting[1].equals("XOR") && setting[3].startsWith("z"))
                    part2Log.add(setting[3]); // First set
            }

            for (String wo :wrongOutput) {
                expandWrongOutputs(gateSetting, List.of(wo));
            }

            for (String old : part2Log) {
                for (String replace : part2Log) {
                    substitution.clear();
                    substitution.put(old, replace);
                    substitution.put(replace, old);
                    gateInput=computeGate(getGateSettings(input, substitution), getGatesInput(input));
                    wrongOutput = getWrongZs(gateInput);
                    if(wrongOutput.size()!=12)
                        System.out.println(old+ " "+ replace + " " +wrongOutput.size());
                }
            }

            substitution.put("z18", "pdg");
            substitution.put("pdg", "z18");
            substitution.put("z22", "jcp");
            substitution.put("jcp", "z22");
            substitution.put("z27", "dhq");
            substitution.put("dhq", "z27");

            gateSetting =getGateSettings(input, substitution);
            gateInput=computeGate(getGateSettings(input, substitution), getGatesInput(input));
            wrongOutput = getWrongZs(gateInput);
            for (String wo :wrongOutput) {
                expandWrongOutputs(gateSetting, List.of(wo));
            }
            part2Log.add("hbs");
            part2Log.add("kfp");
            substitution.put("hbs", "kfp");
            substitution.put("kfp", "hbs");
            gateInput=computeGate(getGateSettings(input, substitution), getGatesInput(input));
            wrongOutput = getWrongZs(gateInput);

            System.out.println(Arrays.toString(part2Log.stream().sorted().toArray()).replaceAll(" ",""));
            return 0;
        }
        return eval(gateInput,  "z");
    }

    private Map<String, Boolean> computeGate(List<String[]> gateSetting, Map<String, Boolean> gateInput) {
        int i=0;
        int timeOut=200;
        while (!gateSetting.isEmpty()){
            i = (i + 1) % gateSetting.size();
            String[] gate=gateSetting.get(i);
            timeOut--;
            if (timeOut<0)
                return gateInput;
            if(evalGate(gate, gateInput)) {
                timeOut=200;
                gateSetting.remove(i);
            }
        }
        return gateInput;
    }

    private List<String[]> getGateSettings(List<String> input, Map<String, String> substitution) {
        List<String[]> result=new ArrayList<>();

        for (String line : input) {
                if (line.contains("->")){
                    String old=line.substring(line.indexOf("-> ")+3);
                    if (substitution.containsKey(old)) {
                        String replace = substitution.get(old);
                        line = line.replace(old, replace);
                    }
                    result.add(line.replace("-> ", "").split(" "));
                }
        }
        return result;
    }

    private List<String> expandWrongOutputs(List<String[]> gateSetting, List<String> wrongOutput) {
        Set<String> result=new HashSet<>();
        Set<String> outputs = new HashSet<>(wrongOutput);
        Set<String> temp = new HashSet<>();
        boolean added=true;
        while (added){
            added=false;
            for (String output : outputs) {
                added = added || temp.addAll(connectedInputs(output,gateSetting));
            }
            if ( added ){
                result.addAll(temp);
                outputs=new HashSet<>(temp);
                temp.clear();
            }
        }
        result.addAll(wrongOutput);
        return new ArrayList<>(result);
    }

    private List<String> connectedInputs(String output, List<String[]> gateSetting) {
        List<String> result=new ArrayList<>();
        for (String[] setting : gateSetting) {
            if (setting[3].equals(output)){
                if (!setting[0].startsWith("x")&&!setting[0].startsWith("y"))
                    result.add(setting[0]);
                if (!setting[2].startsWith("x")&&!setting[2].startsWith("y"))
                    result.add(setting[2]);
            }
            if (result.size()==2) {
                if ((setting[1].equals("XOR") && !setting[3].startsWith("z")) || (!setting[1].equals("XOR") && setting[3].startsWith("z"))) {
                    part2Log.add(setting[3]);
                }
                return result;
            }
        }
        return result;
    }

    private List<String> getWrongZs(Map<String, Boolean> gateInput) {
        List<String> result=new ArrayList<>();
        char[] bug=Long.toBinaryString(
                (eval(gateInput,  "x")  + eval(gateInput,  "y")) ^
                        eval(gateInput,  "z")
        ).toCharArray();
        for (int i = 0; i < bug.length; i++) {
            if (bug[i]=='1')
                result.add("z" +((bug.length-i-1)<10?"0":"")  + (bug.length-i-1));
        }
        return result;
    }

    private boolean evalGate(String[] gate, Map<String, Boolean> gateInput) {
        if (!gateInput.containsKey(gate[0]) || !gateInput.containsKey(gate[2]))
            return false;
        boolean result = switch (gate[1]){
            case "AND" -> gateInput.get(gate[0]) && gateInput.get(gate[2]);
            case "OR" -> gateInput.get(gate[0]) || gateInput.get(gate[2]);
            case "XOR" -> gateInput.get(gate[0]) ^ gateInput.get(gate[2]);
            default -> throw new IllegalArgumentException("Unexpected value: " + gate[1]);
        };
        gateInput.put(gate[3],result );
        return true;
    }

    private Map<String, Boolean> getGatesInput(List<String> input) {
        Map<String, Boolean> result = new HashMap<>();
        for (String line : input) {
            if (line.contains(":"))
                result.put(line.split(":")[0], line.endsWith("1"));
        }
        return result;
    }

    private long eval(Map<String, Boolean> gateInput, String prefix){
        long result=0;
        List <String> zs=gateInput.entrySet().stream().filter(v ->v.getKey().startsWith(prefix) && v.getValue())
                .map(Map.Entry::getKey).toList();
        for (String z : zs) {
            result+= (long) Math.pow(2,Integer.parseInt(z.substring(1)));
        }
        return result;
    }
}