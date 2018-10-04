import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import java.util.*;
import java.util.function.Consumer;

import javax.crypto.Mac;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class Machine {
    // luodaan jFrame
    // System.out.println("as".equalsIgnoreCase(null));

    private Stack<Object> dataStack; 
    private Stack<Object> codeStack;
    private Design d;
    private Map<String, Consumer<Machine>> dispatchMap;

    // Init machine
    public Machine() {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        d = new Design();
        frame.add(d);
        frame.setSize(400, 400);
        frame.setVisible(true);

        dataStack = new Stack<Object>();
        dispatchMap = new HashMap<String, Consumer<Machine>>();
        codeStack = new Stack<Object>();
        
        // Init dispatch map
        dispatchMap.put("+", new Plus());
        dispatchMap.put("-", new Minus());
        dispatchMap.put("*", new Multiply());
        dispatchMap.put("/", new Divide());
        dispatchMap.put("print", new PrintStack());
        dispatchMap.put("read", new ReadInput());
        dispatchMap.put(">", new Greater());
        dispatchMap.put("<", new Lesser());
        dispatchMap.put("==", new Equals());
        dispatchMap.put(">=", new GreaterOrEqual());
        dispatchMap.put("<=", new LesserOrEqual());
        dispatchMap.put("!=", new NotEquals());
        dispatchMap.put("and", new And());
        dispatchMap.put("or", new Or());
        dispatchMap.put("not", new Not());
        dispatchMap.put("dup", new Duplicate());
        dispatchMap.put("rot", new Rotate());
        dispatchMap.put("swap", new Swap());
        dispatchMap.put("drop", new Drop());
        dispatchMap.put("over", new Over());
        dispatchMap.put("nip", new Nip());
        dispatchMap.put("tuck", new Tuck());
        dispatchMap.put("if", new IfThen());
        dispatchMap.put("do", new Do());
        dispatchMap.put("point", new Point());
        dispatchMap.put("line", new Line());
        dispatchMap.put("circle", new Circle());
        dispatchMap.put("rect", new Rect());
        dispatchMap.put("clear", new Clear());
        dispatchMap.put("triangle", new Triangle());


    }


    // Let the machine run stack code
    public void run(String input) {

        parse(input);

        while (!codeStack.empty()) {

            Object opcode = codeStack.pop();
            this.dispatch(opcode);
        }
    }

    /**
     * Parse user inputs and push them to codestack
     *
     * @param input : Raw input from REPL
     */
    public void parse(String input) {

        String[] splitted = input.split(" ");

        int codeLengthPointer = splitted.length - 1; // Parse input string tokens in reverse order.

        while (codeLengthPointer >= 0) {
            try {
                int value = Integer.parseInt(splitted[codeLengthPointer]);
                this.codeStack.push(value);
            } catch (NumberFormatException e) {

                boolean booleanValue; 
                String value = splitted[codeLengthPointer];
                
                if (value.equals("//")) break;				// Enables commenting!
                
				if (value.equals("true")) { 					// Check if boolean
                    booleanValue = true;
                    this.codeStack.push(booleanValue);
                } else if (value.equals("false")) {			// Check if boolean
                    booleanValue = false;
                    this.codeStack.push(booleanValue);
                } else if (value.matches("([\"'])(\\\\?.)*?\\1")) {		// Check if string (" ") -- Shameless Regex copypaste B)
                		value = value.substring(1, value.length()-1); 
                		this.codeStack.push(value);
                } else if (dispatchMap.containsKey(value)) {	// If not string, check if reserved word
                		this.codeStack.push(value);
				} else {										// If not a number, boolean, string or reserved, then do nothing and tell user
					// System.out.println("Invalid argument: " + value);
				}
            } // catch
            codeLengthPointer--;
        } // while
    } // parse

    /**
     * Check if input variable matches an operation
     * Call a function if it does, push to datastack if it doesnt
     *
     * @param op User input
     */
    public void dispatch(Object op) {

        if (op instanceof String && dispatchMap.containsKey(op)) { // does the input contain an operation?
            Consumer<Machine> function = dispatchMap.get(op);
            try {
                function.accept(this);      // call the function
            } catch (EmptyStackException e) {
                System.out.println("Your stack does not have enough items to perform the operation \"" + op + "\"");
            } catch (ClassCastException cce) {
                System.out.println("Operation \"" + op + "\" does not match the stack contents.");
            }
        } else {
            dataStack.push(op); //  input was not an operation, push input to datastack
        }
    } // dispatch


	
	/* 
	 * 
	 * Machine getters & setters
     *
     */
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

    public Design getDesign() {
        return d;
    }

    public void setDesign(Design newDesign) {
        this.d = newDesign;
    }
}


/**
 * Basic operations, +, -, *, /
 */

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

/**
 * Logic operators, ==, >, <, !=, >=, <=, and, or, not
 */
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

/**
 * Stack operations ”dup”, ”rot”, ”swap”, ”drop”, ”over”, ”nip”, ”tuck”
 */
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
        Object c = st.pop();
        Object b = st.pop();
        Object a = st.pop();

        st.push(b); // So lets push them on correct order
        st.push(c);
        st.push(a);
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


/**
 * Logic control conditions, if - else - then
 */
class IfThen implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
        Stack<Object> dataStack = m.getDataStack();
        Stack<Object> codeStack = m.getCodeStack();
        int stackSize = codeStack.size();
        int elseIndex = codeStack.indexOf("else");
        int thenIndex = codeStack.indexOf("then");
        boolean condition = (Boolean) dataStack.pop();


        //condition was TRUE. clear the possible ELSE part
        if (condition) {

            int startDeletingFromIndex = 0;
            int deleteTowardsIndex = 0;

            // check if ELSE was used
            if (elseIndex != -1) {
                deleteTowardsIndex = elseIndex;
            }

            // check if THEN was used
            if (thenIndex != -1) {
                startDeletingFromIndex = thenIndex;
            }

            if (startDeletingFromIndex < deleteTowardsIndex) {
                for (int i = 0; i <= deleteTowardsIndex - startDeletingFromIndex; i++) {
                    //remove all items from codeStack within ELSE
                    codeStack.remove(startDeletingFromIndex);
                }
            } else {
                codeStack.remove("then");
            }

        }

        //condition was FALSE. clear the necessary parts of code stack
        if (!condition) {

            // condition was false, no ELSE given, no THEN given -> clear line
            if (thenIndex == -1 && elseIndex == -1) codeStack.clear();
            else { //determine if there is either a THEN or an ELSE
                int startDeletingFromIndex = thenIndex;
                if (thenIndex == -1) {
                    startDeletingFromIndex = elseIndex;
                }
                if (elseIndex == -1) {
                    startDeletingFromIndex = thenIndex;
                }

                // remove elements from code stack that are ignored
                for (int i = 0; i < stackSize - startDeletingFromIndex; i++) {
                    codeStack.remove(startDeletingFromIndex);
                }
                // clean the "loop" string from the stack
                codeStack.remove(codeStack.lastIndexOf("then"));
            }
        }

        m.setCodeStack(codeStack);
        m.setDataStack(dataStack);
    }
}

/**
 * Logic control for DO - LOOP
 * limit index DO --operations-- LOOP ...
 */
class Do implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
        Stack<Object> dataStack = m.getDataStack();
        Stack<Object> codeStack = m.getCodeStack();
        Stack<Object> loopCodeStack = new Stack<Object>();

        //determine where the loop statement begins
        int stackSize, loopIndex;
        stackSize = codeStack.size();
        loopIndex = codeStack.indexOf("loop");

        //if no loop is found, exit method
        if (loopIndex == -1) {
            System.out.println("Cannot use 'do' without 'loop'");
            return;
        }

        // 'limit' 'index' DO ... LOOP
        int limit = 0, index = 0;
        try {
            index = (Integer) dataStack.pop();
            limit = (Integer) dataStack.pop();
        } catch (ClassCastException cce) { //operands were not integers
            System.out.println("Your operands cannot be used for a 'do' query.");
            return;
        }

        // index exceeds limit, clear the elements up to LOOP
        if (index >= limit) {
            for (int i = 0; i < stackSize - loopIndex; i++) {
                codeStack.remove(loopIndex);
            }
        }

        // the meat and bones of a loop
        while (index < limit) {
            for (int i = 1; i < stackSize - loopIndex; i++) {
                //dispatch methods without popping them
                m.dispatch(codeStack.elementAt(stackSize - i));
            }
            index++;
        }
        // clean the "loop" string from the codeStack. picks the last to avoid conflicts
        codeStack.remove(codeStack.lastIndexOf("loop"));
    }
}

/*
 *
 * Tästä tarkoitus saada luotua uusi jcomponent, mutta ei piirrä oikein.
 *
 */


class Point implements Consumer<Machine> {

    public void accept(Machine m) {
        Stack<Object> st = m.getDataStack();
        String color = (String) st.pop();
        int b = (Integer) st.pop();
        int a = (Integer) st.pop();
        Design md = m.getDesign();
        md.addDot(a, b, color);
        m.setDesign(md);
        m.setDataStack(st);
    }
}

class Line implements Consumer<Machine> {
    public void accept(Machine m) {
        Stack<Object> st = m.getDataStack();
        String color = (String) st.pop();
        int d = (Integer) st.pop();
        int c = (Integer) st.pop();
        int b = (Integer) st.pop();
        int a = (Integer) st.pop();
        Design md = m.getDesign();
        md.addLine(a, b, c, d, color);
        m.setDesign(md);
    }
}

class Circle implements Consumer<Machine> {
    public void accept(Machine m) {
        Stack<Object> st = m.getDataStack();
        String color = (String) st.pop();
        int r = (Integer) st.pop();
        int y = (Integer) st.pop();
        int x = (Integer) st.pop();
        Design md = m.getDesign();
        md.addCircle(x, y, r, color);
        m.setDesign(md);
    }
}

class Rect implements Consumer<Machine> {
    public void accept(Machine m) {
        Stack<Object> st = m.getDataStack();
        String color = (String) st.pop();
        int height = (Integer) st.pop();
        int width = (Integer) st.pop();
        int yPos = (Integer) st.pop();
        int xPos = (Integer) st.pop();
        Design md = m.getDesign();
        md.addRect(xPos, yPos, width, height, color);
        m.setDesign(md);
    }
}

class Clear implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
        Design md = m.getDesign();
        md.getGraphics().clearRect(0, 0, md.getWidth(), md.getHeight());
    }
}

class Triangle implements Consumer<Machine> {
    public void accept(Machine m) {
        Stack<Object> st = m.getDataStack();
        String color = (String) st.pop();
        int y3 = (Integer) st.pop();
        int x3 = (Integer) st.pop();
        int y2 = (Integer) st.pop();
        int x2 = (Integer) st.pop();
        int y1 = (Integer) st.pop();
        int x1 = (Integer) st.pop();
        Design md = m.getDesign();
        md.addTriangle(x1,y1,x2,y2,x3,y3,color);
        m.setDesign(md);
    }
}

/**
 * Read: Allows stack language program to read input from user.
 */

class ReadInput implements Consumer<Machine> {
	@Override
	public void accept(Machine m) {		
		Scanner sc = new Scanner(System.in);
		System.out.println("Program demands user input. Enter your entry to datastack.");
		System.out.print("Input: ");
		String input = sc.nextLine();
		m.parse(input);
	}
}
/**
 * Pop a value from stack and print it
 */
class PrintStack implements Consumer<Machine> {
    @Override
    public void accept(Machine m) {
        Stack<Object> st = m.getDataStack();
        if (!st.isEmpty()) {
            System.out.println(st.pop());
        }
        m.setDataStack(st);
    }

}

