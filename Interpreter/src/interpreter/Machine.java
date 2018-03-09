package interpreter;

import java.util.*;
import java.util.function.Consumer;

public class Machine{

  Stack<Object> dataStack;
  Stack<Integer> returnAddressStack;
  int instructionPointer;
  ArrayList<Object> code;
  
  Map<String, Consumer<Stack<Object>>> dispatchMap;

// Init machine
  public Machine(String input) {
    this.dataStack = new Stack<Object>();
    
    this.dispatchMap = new HashMap<String, Consumer<Stack<Object>>>();

    this.code = parse(input);
 
    Consumer<Stack<Object>> plus = new Plus();
    Consumer<Stack<Object>> minus = new Minus();
    Consumer<Stack<Object>> mul = new Multiply();
    Consumer<Stack<Object>> div = new Divide();
    
    Consumer<Stack<Object>> print = new PrintStack();
    
    // Init dispatch map
    
    dispatchMap.put("+", plus);
    dispatchMap.put("-", minus);
    dispatchMap.put("*", mul);
    dispatchMap.put("/", div);
    dispatchMap.put(".", print);

  }

// Let the machine run stack code

  public void run() {
    while (this.instructionPointer < this.code.size()) {

      Object opcode = code.get(instructionPointer);
      this.instructionPointer += 1;
      this.dispatch(opcode);
      
    }
  }

  public void dispatch(Object op) {
	  
      if (op instanceof String && dispatchMap.containsKey(op)) {
        Consumer<Stack<Object>> function = this.dispatchMap.get(op);
        function.accept(this.dataStack);
      } else if (op instanceof Integer) {
        this.push(op);
      } else if (op instanceof String) {
        this.push(op);
      }
  }


  public ArrayList<Object> parse(String input) {

    ArrayList<Object> result = new ArrayList<Object>();
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
  
  public static String print(Stack<Object> a) {
	  String result = "";
	  while (!a.isEmpty()) {
		  result = (String) a.pop();
	  }
	  return result;
  }



}


// Lets gou:

/*
 * 
 * Nelilaskin:
 * 
 * */

class Plus implements Consumer<Stack<Object>> {
	@Override
	public void accept(Stack<Object> st) {
		int a = (Integer) st.pop();
		int b = (Integer) st.pop();		
		int result = a + b;
		st.push(result);
	}
}

class Minus implements Consumer<Stack<Object>> {
	@Override
	public void accept(Stack<Object> st) {
		int a = (Integer) st.pop();
		int b = (Integer) st.pop();		
		int result = b - a;
		st.push(result);
	}
}

class Multiply implements Consumer<Stack<Object>> {
	@Override
	public void accept(Stack<Object> st) {
		int a = (Integer) st.pop();
		int b = (Integer) st.pop();		
		int result = a * b;
		st.push(result);
	}
}

class Divide implements Consumer<Stack<Object>> {
	@Override
	public void accept(Stack<Object> st) {
		double a = ((Integer) st.pop()).doubleValue();
		double b = ((Integer) st.pop()).doubleValue();
		double result = b / a;
		st.push(result);
	}
}


/*
 * 
 * Tulostus
 * 
 */

class PrintStack implements Consumer<Stack<Object>> {
	
	@Override
	public void accept(Stack<Object> st) {
		while(!st.isEmpty()) {
			System.out.println(st.pop());
		}
		
	}
}

