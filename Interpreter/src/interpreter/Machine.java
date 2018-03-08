package interpreter;

import java.util.*;
import java.util.function.BiFunction;

public class Machine{

  Stack<Object> dataStack;
  Stack<?> returnAddressStack;
  int instructionPointer;
  ArrayList<?> code;
  
  Map<String, BiFunction<?, ?, ?>> dispatchMap;

// Init machine
  public Machine(String input) {
    this.dataStack = new Stack<Object>();
    this.dispatchMap = new HashMap<String, BiFunction<?, ?, ?>>();

    this.code = parse(input);
 
    BiFunction<Integer, Integer, Integer> plusFunction = Machine::plus;
    BiFunction<Integer, Integer, Integer> minusFunction = Machine::minus;
    BiFunction<Integer, Integer, Integer> multiplyFunction = Machine::mul;
    BiFunction<Integer, Integer, Integer> divideFunction = Machine::div;
      // Init dispatch map
    dispatchMap.put("+", plusFunction);
    dispatchMap.put("-", minusFunction);
    dispatchMap.put("*", multiplyFunction);
    dispatchMap.put("/", divideFunction);
  }

// Let the machine run stack code

  public void run() {
    while (this.instructionPointer < this.code.size()) {

      Object opcode = code.get(instructionPointer);
      this.instructionPointer += 1;
      this.dispatch(opcode);
      
    }
    System.out.println(this.dataStack.peek());
  }

  public void dispatch(Object op) {
	  
      if (op instanceof String && dispatchMap.containsKey(op)) {
        BiFunction function = (BiFunction)this.dispatchMap.get(op);
        
        int a1 = (Integer) this.dataStack.pop();
        int a2 = (Integer) this.dataStack.pop();
        int result = (Integer) function.apply(a1, a2);
        this.dataStack.push(result);
        
      } else if (op instanceof Integer) {
        this.push(op);
      } else if (op instanceof String) {
        this.push(op);
      }
  }


  public ArrayList parse(String input) {

    ArrayList result = new ArrayList();
    String[] splitted = input.split(" ");

    int codeLengthPointer = 0;

    

    while (codeLengthPointer < splitted.length) {
      try {
        int value = Integer.parseInt(splitted[codeLengthPointer]);
        result.add(value);
      } catch (NumberFormatException e) {
        String value = splitted[codeLengthPointer];
        result.add(value);
      }
      codeLengthPointer++;
    }

    return result;

  }

  public Object pop() {
    return this.dataStack.pop();
  }

  public void push(Object a) {
    this.dataStack.push(a);
  }


  public static Integer plus (Integer a, Integer b) {
	  return a + b;
  }

  public static Integer minus (Integer a, Integer b) {
	  return b - a;
  }
  
  public static Integer mul (Integer a, Integer b) {
	  return a * b;
  }
  
  public static Integer div (Integer a, Integer b) {
	  return b / a;
  }


}
