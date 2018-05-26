import java.util.Scanner;

public class DemoREPL {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String code;
        Machine machine;
        System.out.println("Demonstration REPL");
        machine = new Machine();
        double input = -1;
        double scale = 2.0;
        System.out.print("Choose a number between 1 and 200:\n> ");
        while (input < 0){
            try {
                input = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException nfE){
                System.out.println("Invalid parameter. Please choose a number between 1 and 200");
            }
        }
        scale = input / 100;
        //round scale to 2 decimals
        scale = (double) Math.round(scale * 100d) / 100d;
        System.out.printf("Scale set to %s.", scale);

        while (!scanner.nextLine().equals("exit")) {
            System.out.println("=> " + machine.getDataStack().toString());
            System.out.print("> ");
            code = scanner.nextLine();
            //machine.run(code);

        }

    }
}