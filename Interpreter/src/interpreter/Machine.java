import java.util.*;
import java.util.function.Consumer;

public class Machine{

    Stack<Object> dataStack;
    Stack<Integer> returnAddressStack;
    int instructionPointer;
    Stack<Object> code;

    Map<String, Consumer<Stack<Object>>> dispatchMap;

    // Init machine
    public Machine() {
        this.dataStack = new Stack<Object>();

        this.dispatchMap = new HashMap<String, Consumer<Stack<Object>>>();

        code = new Stack<Object>();

        // Basic calculations
        Consumer<Stack<Object>> plus = new Plus();
        Consumer<Stack<Object>> minus = new Minus();
        Consumer<Stack<Object>> mul = new Multiply();
        Consumer<Stack<Object>> div = new Divide();
        
        // Comparators
        Consumer<Stack<Object>> gt = new Greater();
        Consumer<Stack<Object>> lt = new Lesser();
        Consumer<Stack<Object>> eq = new Equals();
        Consumer<Stack<Object>> goe = new GreaterOrEqual();
        Consumer<Stack<Object>> loe = new LesserOrEqual();
        Consumer<Stack<Object>> neq = new NotEquals();
        
        // Logic operators
        Consumer<Stack<Object>> and = new And();
        Consumer<Stack<Object>> or = new Or();
        Consumer<Stack<Object>> not = new Not();

        Consumer<Stack<Object>> print = new PrintStack();

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
    }

// Let the machine run stack code

    public void run(String input) {

        parse(input);

        while (!code.empty()) {

            Object opcode = code.pop();
            this.dispatch(opcode);

        }
    }

    public void dispatch(Object op) {

        if (op instanceof String && dispatchMap.containsKey(op)) {
            Consumer<Stack<Object>> function = this.dispatchMap.get(op);
            try {
                function.accept(this.dataStack);
            } catch (EmptyStackException e) {
                System.out.println("Your stack dont have enough items to do operation \"" + op + "\"");
            }
        } else { 
        		this.push(op);
        }
    }


    public void parse(String input) {

        String[] splitted = input.split(" ");

        int codeLengthPointer = splitted.length -1;

        while (codeLengthPointer >= 0) {
            try {
                int value = Integer.parseInt(splitted[codeLengthPointer]);
                this.code.push(value);
            } catch (NumberFormatException e) {
                boolean booleanValue;
                String value = splitted[codeLengthPointer];
                
                if (value.equals("true")){
                     booleanValue = true;
                     this.code.push(booleanValue);
                } else if (value.equals("false")) {
                    booleanValue = false;
                    this.code.push(booleanValue);
                } else {
                    this.code.push(value);
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

class Equals implements Consumer<Stack<Object>> {
    @Override
    public void accept(Stack<Object> st) {
        int a = ((Integer) st.pop());
        int b = ((Integer) st.pop());
        boolean result = (a == b);
        st.push(result);
    }
}

class Greater implements Consumer<Stack<Object>> {
    @Override
    public void accept(Stack<Object> st) {
        int a = ((Integer) st.pop());
        int b = ((Integer) st.pop());
        boolean result = (a > b);
        st.push(result);
    }
}
class Lesser implements Consumer<Stack<Object>> {
    @Override
    public void accept(Stack<Object> st) {
        int a = ((Integer) st.pop());
        int b = ((Integer) st.pop());
        boolean result = (a < b);
        st.push(result);
    }
}
class NotEquals implements Consumer<Stack<Object>> {
    @Override
    public void accept(Stack<Object> st) {
        int a = ((Integer) st.pop());
        int b = ((Integer) st.pop());
        boolean result = (a != b);
        st.push(result);
    }
}

class GreaterOrEqual implements Consumer<Stack<Object>> {
    @Override
    public void accept(Stack<Object> st) {
        int a = ((Integer) st.pop());
        int b = ((Integer) st.pop());
        boolean result = (a >= b);
        st.push(result);
    }
}
class LesserOrEqual implements Consumer<Stack<Object>> {
    @Override
    public void accept(Stack<Object> st) {
        int a = ((Integer) st.pop());
        int b = ((Integer) st.pop());
        boolean result = (a <= b);
        st.push(result);
    }
}

class And implements Consumer<Stack<Object>> {
	@Override
    public void accept(Stack<Object> st) {
        boolean a = ((Boolean) st.pop());
        boolean b = ((Boolean) st.pop());
        boolean result = (a && b);
        st.push(result);
    }
}

class Or implements Consumer<Stack<Object>> {
	@Override
    public void accept(Stack<Object> st) {
        boolean a = ((Boolean) st.pop());
        boolean b = ((Boolean) st.pop());
        boolean result = (a || b);
        st.push(result);
    }
}

class Not implements Consumer<Stack<Object>> {
	@Override
    public void accept(Stack<Object> st) {
        boolean a = ((Boolean) st.pop());
        boolean result = (!a);
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

