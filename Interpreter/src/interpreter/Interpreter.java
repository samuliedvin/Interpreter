package interpreter;

import java.util.*;

public class Interpreter{

  public static void main(String[] args) {

      Scanner scanner = new Scanner(System.in);

      String code;

      Machine machine;

      System.out.println("Welcome to Stack lang REPL");

      while(true) {
        try {

          System.out.print("> ");
          code = scanner.nextLine();
          machine = new Machine(code);
          machine.run();

        } catch (Exception e) {
          e.printStackTrace();
          break;
        }
      }
      
      scanner.close();

  }

}
