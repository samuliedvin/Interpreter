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

        // IF-THEN-ELSE & DO LOOP
        Consumer<Machine> ifthen = new IfThen();
        Consumer<Machine> doLoop = new Do();
        Consumer<Machine> print = new PrintStack();

        //2D Tulostus
        Consumer<Machine> point = new Point();
        Consumer<Machine> line = new Line();
        Consumer<Machine> circle = new Circle();
        Consumer<Machine> rect = new Rect();
        Consumer<Machine> clear = new Clear();


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
        dispatchMap.put("if", ifthen);
        dispatchMap.put("do", doLoop);
        dispatchMap.put("point", point);
        dispatchMap.put("line", line);
        dispatchMap.put("circle", circle);
        dispatchMap.put("rect", rect);
        dispatchMap.put("clear", clear);


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

        int codeLengthPointer = splitted.length - 1;

        while (codeLengthPointer >= 0) {
            try {
                int value = Integer.parseInt(splitted[codeLengthPointer]);
                this.codeStack.push(value);
            } catch (NumberFormatException e) {
                boolean booleanValue;
                String value = splitted[codeLengthPointer];
                if (value.equals("true")) {
                    booleanValue = true;
                    this.codeStack.push(booleanValue);
                } else if (value.equals("false")) {
                    booleanValue = false;
                    this.codeStack.push(booleanValue);
                } else {
                    this.codeStack.push(value);
                }
                ;

            }
            codeLengthPointer--;
        }
    }

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

