import java.util.*;

public class REPL{

  public static void main(String[] args) {

      Scanner scanner = new Scanner(System.in);

      String code;

      Machine machine;

      System.out.println("Welcome to Stack lang REPL");

      machine = new Machine();
      
      while(true) {
        try {

          System.out.print("> ");
          code = scanner.nextLine();
          machine.run(code);

        } catch (Exception e) {
          e.printStackTrace();
          break;
        }
      }
      
      scanner.close();

  }

}
