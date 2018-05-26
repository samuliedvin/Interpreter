import java.util.Scanner;

public class DemoREPL {
    // where characters begin. incremented after each draw

    /**
     * @param startingCoordinates: 0 => x, 1 => y
     * @param characterSize
     * @param scale
     */
    private static int[] startingCoordinates = {10, 10};
    private static int characterSize = 30;
    private static double scale = 1.0;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double initialInput = -1;
        System.out.println("Demonstration REPL");
        System.out.print("Choose a number between 1 and 200:\n> ");
        while (initialInput == -1) {
            try {
                initialInput = Double.parseDouble(scanner.nextLine());
                if (initialInput < 1 || initialInput > 200){
                    initialInput = -1;
                    throw new Exception("wrong input");
                }
            } catch (Exception e) {
                System.out.print("Invalid parameter. Please choose a number between 1 and 200.\n> ");
            }
        }
        scale = initialInput / 100;
        //round scale to 2 decimals
        scale = (double) Math.round(scale * 100d) / 100d;
        characterSize *= scale;
        System.out.printf("Scale set to %s. Press X to exit.\n", scale);

        char input;
        String[] code;

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
            for (String instruction : code) {
                machine.run(instruction);
            }
            startingCoordinates[0] += characterSize + 5 * scale; // move right to draw the next character. adds spacing
        }
        System.out.println("Goodbye.");
        System.exit(0);
    }

    /**
     *
     * @param request: the letter input by user
     * @return : stack language command to create requested letter
     */
    private static String[] transformRequestToCode(char request) {
        String code[];
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
            default:
                code = new String[0];
        }
        return code;
    }

    private static String[] initializeA() {
        String[] code = new String[3];

        int x1, y1, x2, y2;

        x1 = startingCoordinates[0];
        y1 = startingCoordinates[1] + characterSize;
        x2 = x1 + characterSize / 2;
        y2 = startingCoordinates[1];
        code[0] = x1 + " " + y1 + " " + x2 + " " + y2 + " " + "line";

        x1 = startingCoordinates[0] + characterSize / 2;
        y1 = startingCoordinates[1];
        x2 = startingCoordinates[0] + characterSize;
        y2 = startingCoordinates[1] + characterSize;
        code[1] = x1 + " " + y1 + " " + x2 + " " + y2 + " " + "line";

        x1 = startingCoordinates[0] + characterSize / 4;
        y1 = startingCoordinates[1] + characterSize / 2;
        x2 = startingCoordinates[0] + characterSize / 4 * 3;
        y2 = y1;
        code[2] = x1 + " " + y1 + " " + x2 + " " + y2 + " " + "line";

        return code;
    }

    private static String[] initializeO() {
        String[] code = new String[1];

        int x, y, r;

        x = startingCoordinates[0] - characterSize / 10; // offset added to center the circle
        y = startingCoordinates[1];
        r = characterSize;
        code[0] = x + " " + y + " " + r + " " + "circle";

        return code;
    }

    private static String[] initializeK() {
        String[] code = new String[3];

        int x1, y1, x2, y2;

        x1 = startingCoordinates[0];
        y1 = startingCoordinates[1];
        x2 = x1;
        y2 = y1 + characterSize;
        code[0] = x1 + " " + y1 + " " + x2 + " " + y2 + " " + "line";

        x1 = startingCoordinates[0];
        y1 = startingCoordinates[1] + characterSize / 2;
        x2 = startingCoordinates[0] + characterSize;
        y2 = startingCoordinates[1];
        code[1] = x1 + " " + y1 + " " + x2 + " " + y2 + " " + "line";

        x1 = startingCoordinates[0] + characterSize / 2;
        y1 = startingCoordinates[1] + characterSize / 4;
        x2 = startingCoordinates[0] + characterSize;
        y2 = startingCoordinates[1] + characterSize;
        code[2] = x1 + " " + y1 + " " + x2 + " " + y2 + " " + "line";

        return code;
    }

    private static String[] initializeP() {
        String[] code = new String[2];

        int x1, y1, x2, y2, rectangleWidth, rectangleHeight;

        x1 = startingCoordinates[0];
        y1 = startingCoordinates[1];
        x2 = startingCoordinates[0];
        y2 = startingCoordinates[1] + characterSize;
        code[0] = x1 + " " + y1 + " " + x2 + " " + y2 + " " + "line";

        x1 = startingCoordinates[0];
        y1 = startingCoordinates[1];
        rectangleWidth = characterSize ;
        rectangleHeight = characterSize / 2;
        code[1] = x1 + " " + y1 + " " + rectangleWidth + " " + rectangleHeight + " " + "rect";

        return code;
    }
}