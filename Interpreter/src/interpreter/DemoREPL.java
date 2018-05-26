import java.util.Scanner;

public class DemoREPL {
    // where characters begin. incremented after each draw
    static int[] startingCoordinates = {10, 10};
    static int characterDefaultSize = 30;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double initialInput = -1;
        double scale = 2.0;
        System.out.println("Demonstration REPL");
        System.out.print("Choose a number between 1 and 200:\n> ");
        while (initialInput < 0) {
            try {
                initialInput = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException nfE) {
                System.out.println("Invalid parameter. Please choose a number between 1 and 200");
            }
        }
        scale = initialInput / 100;
        //round scale to 2 decimals
        scale = (double) Math.round(scale * 100d) / 100d;
        System.out.printf("Scale set to %s. Press X to exit.\n", scale);

        char input;
        String code;

        Machine machine = new Machine();
        boolean keepGoing = true;
        // make scanner only read single characters
        //scanner.useDelimiter("");

        while (keepGoing) {
            System.out.print("A, O, K, P, ? > ");
            input = Character.toUpperCase(scanner.next().charAt(0));
            if (input == 'X') {
                keepGoing = false;
                continue;
            }
            if ("AOKP".indexOf(input) == -1) {
                System.out.println("That character is not supported.");
                continue;
            }
            code = transformRequestToCode(input);
            machine.run(code);
            startingCoordinates[0] += scale * characterDefaultSize;
        }
        System.out.println("Goodbye.");
        System.exit(0);
    }

    private static String transformRequestToCode(char request) {
        String code = "";
        switch (request) {
            case 'A':
                code = initializeA();
                break;
            case 'O':
                code = initializeO();
                break;
            case 'K':
                code = initializeK();
                break;
            case 'P':
                code = initializeP();
                break;
        }
        System.out.println(code);
        return code;
    }

    private static String initializeA() {
        int x1 = startingCoordinates[0];
        int y1 = startingCoordinates[1] + characterDefaultSize;
        int x2 = x1 + characterDefaultSize / 2;
        int y2 = y1 - characterDefaultSize;

        String code = x1 + " " + y1 + " " + x2 + " " + y2;
        return (code + " line");
    }

    private static String initializeO() {
        String code = "";

        return code;
    }

    private static String initializeK() {
        String code = "";

        return code;
    }

    private static String initializeP() {
        String code = "";

        return code;
    }
}