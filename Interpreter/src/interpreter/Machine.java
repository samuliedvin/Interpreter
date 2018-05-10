import java.util.*;
import java.util.function.Consumer;

public class Machine{

    private Stack<Object> dataStack;
    private Stack<Integer> returnAddressStack;
    private Stack<Object> codeStack;

    Map<String, Consumer<Machine>> dispatchMap;

    // Init machine
    public Machine() {
    	
        dataStack = new Stack<Object>();
		dispatchMap = new HashMap<String, Consumer<Machine>>();
        codeStack = new Stack<Object>();

        // Basic calculations
        Consumer<Machine> plus = new Plus();
        Consumer<Machine> minus = new Minus();
        Consumer<Machine> mul = new Multiply();
        Consumer<Machine> div = new Divide();
        
        // Comparators
        Consumer<Machine> gt = new Greater();
        Consumer<Machine> lt = new Lesser();
        Consumer<Machine> eq = new Equals();
        Consumer<Machine> goe = new GreaterOrEqual();
        Consumer<Machine> loe = new LesserOrEqual();
        Consumer<Machine> neq = new NotEquals();
        
        // Logic operators
        Consumer<Machine> and = new And();
        Consumer<Machine> or = new Or();
        Consumer<Machine> not = new Not();
        
        // ”dup”, ”rot”, ”swap”, ”drop”, ”over”, ”nip”, ”tuck” 
        Consumer<Machine> dup = new Duplicate();
        Consumer<Machine> rot = new Rotate();
        Consumer<Machine> swap = new Swap();
        Consumer<Machine> drop = new Drop();
        Consumer<Machine> over = new Over();
        Consumer<Machine> nip = new Nip();
        Consumer<Machine> tuck = new Tuck();


        Consumer<Machine> print = new PrintStack();

        // Init dispatch map

        dispatchMap.put("+", plus);
        dispatchMap.put("-", minus);
        dispatchMap.put("*", mul);
        dispatchMap.put("/", div);
        dispatchMap.put(".", print);
        dispatchMap.put(">", gt);
        dispatchMap.put("<", lt);
        dispatchMap.put("==", eq);
        dispatchMap.put(">=", goe);
        dispatchMap.put("<=", loe);
        dispatchMap.put("!=", neq);
        dispatchMap.put("and", and);
        dispatchMap.put("or", or);
        dispatchMap.put("not", not);
        dispatchMap.put("dup", dup);
        dispatchMap.put("rot", rot);
        dispatchMap.put("swap", swap);
        dispatchMap.put("drop", drop);
        dispatchMap.put("over", over);
        dispatchMap.put("nip", nip);
        dispatchMap.put("tuck", tuck);
    }

// Let the machine run stack code

    public void run(String input) {

        parse(input);

        while (!codeStack.empty()) {

            Object opcode = codeStack.pop();
            this.dispatch(opcode);

        }
    }

    public void dispatch(Object op) {

        if (op instanceof String && dispatchMap.containsKey(op)) {
            Consumer<Machine> function = this.dispatchMap.get(op);
            try {
                function.accept(this);
            } catch (EmptyStackException e) {
                System.out.println("Your stack dont have enough items to do operation \"" + op + "\"");
            }
        } else { 
        		this.dataStack.push(op);
        }
    }


    public void parse(String input) {

        String[] splitted = input.split(" ");

        int codeLengthPointer = splitted.length -1;

        while (codeLengthPointer >= 0) {
            try {
                int value = Integer.parseInt(splitted[codeLengthPointer]);
                this.codeStack.push(value);
            } catch (NumberFormatException e) {
                boolean booleanValue;
                String value = splitted[codeLengthPointer];
                
                if (value.equals("true")){
                     booleanValue = true;
                     this.codeStack.push(booleanValue);
                } else if (value.equals("false")) {
                    booleanValue = false;
                    this.codeStack.push(booleanValue);
                } else {
                    this.codeStack.push(value);
                };
                
            }
            codeLengthPointer--;
        }
    }

    public Object pop() {
        return this.dataStack.pop();
    }

    public void push(Object a) {
        this.dataStack.push(a);
    }

    
    	public Stack<Object> getCodeStack() {
    		return codeStack;
    	}
    	
    	public void setCodeStack(Stack<Object> newCodeStack) {
    		this.codeStack = newCodeStack;
    	}
    	
    	public Stack<Object> getDataStack() {
    		return dataStack;
    	}
    	
    	public void setDataStack(Stack<Object> newDataStack) {
    		this.dataStack = newDataStack;
    	}
}


// Lets gou:

/*
 *
 * Nelilaskin:
 *
 * */

class Plus implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        int a = (Integer) st.pop();
        int b = (Integer) st.pop();
        int result = a + b;
        st.push(result);
        m.setDataStack(st);
    }
}

class Minus implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        int a = (Integer) st.pop();
        int b = (Integer) st.pop();
        int result = b - a;
        st.push(result);
        m.setDataStack(st);
    }
}

class Multiply implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        int a = (Integer) st.pop();
        int b = (Integer) st.pop();
        int result = a * b;
        st.push(result);
        m.setDataStack(st);
    }
}

class Divide implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        double a = ((Integer) st.pop()).doubleValue();
        double b = ((Integer) st.pop()).doubleValue();
        double result = b / a;
        st.push(result);
        m.setDataStack(st);
    }
}

class Equals implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        int a = ((Integer) st.pop());
        int b = ((Integer) st.pop());
        boolean result = (a == b);
        st.push(result);
        m.setDataStack(st);
    }
}

class Greater implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        int a = ((Integer) st.pop());
        int b = ((Integer) st.pop());
        boolean result = (a > b);
        st.push(result);
        m.setDataStack(st);
    }
}
class Lesser implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        int a = ((Integer) st.pop());
        int b = ((Integer) st.pop());
        boolean result = (a < b);
        st.push(result);
        m.setDataStack(st);
    }
}
class NotEquals implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        int a = ((Integer) st.pop());
        int b = ((Integer) st.pop());
        boolean result = (a != b);
        st.push(result);
        m.setDataStack(st);
    }
}

class GreaterOrEqual implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        int a = ((Integer) st.pop());
        int b = ((Integer) st.pop());
        boolean result = (a >= b);
        st.push(result);
        m.setDataStack(st);
    }
}
class LesserOrEqual implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        int a = ((Integer) st.pop());
        int b = ((Integer) st.pop());
        boolean result = (a <= b);
        st.push(result);
        m.setDataStack(st);
    }
}

class And implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        boolean a = ((Boolean) st.pop());
        boolean b = ((Boolean) st.pop());
        boolean result = (a && b);
        st.push(result);
        m.setDataStack(st);
    }
}

class Or implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        boolean a = ((Boolean) st.pop());
        boolean b = ((Boolean) st.pop());
        boolean result = (a || b);
        st.push(result);
        m.setDataStack(st);
    }
}

class Not implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        boolean a = ((Boolean) st.pop());
        boolean result = (!a);
        st.push(result);
        m.setDataStack(st);
    }
}

// ”dup”, ”rot”, ”swap”, ”drop”, ”over”, ”nip”, ”tuck” 

class Duplicate implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
		
		// Duplicate the top element on stack
        Object a = st.pop();
        st.push(a);
        st.push(a);
        m.setDataStack(st);
    }
}

class Rotate implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
		
		// Rotate top three elements on stack (a b c -> b c a)
        Object a = st.pop();
        Object b = st.pop();
        Object c = st.pop();
        
        st.push(a); // So lets push them on correct order
        st.push(c);
        st.push(b);
        m.setDataStack(st);

    }
}

class Drop implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
		Object a = st.pop();
        a = null;
        m.setDataStack(st);

    }
}

class Swap implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        Object a = st.pop();
        Object b = st.pop();
        st.push(a);
        st.push(b);
        m.setDataStack(st);

    }
} 

class Over implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        Object a = st.pop();
        Object b = st.pop();
        st.push(b);
        st.push(a);
        st.push(b);
        m.setDataStack(st);

    }
} 

class Nip implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        Object a = st.pop();
        Object b = st.pop();
        st.push(a);
        b = null;
        m.setDataStack(st);

    }
} 

class Tuck implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        Object a = st.pop();
        Object b = st.pop();
        st.push(a);
        st.push(b);
        st.push(a);
        m.setDataStack(st);

    }
} 


/*
 * 
 * IF-THEN -hommeli
 * 
 */

class IfThen implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> dataStack = m.getDataStack();
		Stack<Object> codeStack = m.getCodeStack();
		
		boolean statement = (Boolean) dataStack.pop();
		
		if(statement) {
			
		} else {
			
		} 
		m.setDataStack(dataStack);
		m.setCodeStack(codeStack);
	}
}

/*
 *
 * Tulostus
 *
 */

class PrintStack implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
    		Stack<Object> st = m.getDataStack();
        while(!st.isEmpty()) {
            System.out.println(st.pop());
        }
        m.setDataStack(st);
    }
    
}

