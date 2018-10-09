public abstract class MathOp implements Runnable {
    public void execute(Stack<Data> ds) {
        int a = ((Integer) ds.pop());
        int b = ((Integer) ds.pop());
        int foo = operate(a, b);
        ds.push(foo);
    }

    abstract void operate();
}

public class Plus extends MathOp {
    private int operate() {
        return a + b;
    }
}

public class Minus extends MathOp {
    private int operate() {
        return a - b;
    }
}

public class Multiply extends MathOp {
    private int operate() {
        return a * b;
    }
}

public class Divide extends MathOp {
    private int operate() {
        return a / b;
    }
}