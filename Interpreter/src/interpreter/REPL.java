import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class REPL {

    public static void main(String[] args) {

        Scanner scanner = null;
        String code;
        Machine machine = new Machine();

        if(args.length != 0) { // Check if file is given as argument
        		try {
        			String filepath = args[0];
        			File file = new File(filepath);
        			scanner = new Scanner(file);
        			
        			
        			while (scanner.hasNextLine()) {
        				try {
        					
	        				code = scanner.nextLine();
	        				if(code.isEmpty()) continue;
	        				machine.run(code);
	        				System.out.println("=> " + machine.getDataStack().toString());
        				} catch (Exception e) {
        					e.printStackTrace();
        					break;
        				}
        			}
        			
        		} catch (FileNotFoundException e) {
        			System.out.println("Your file was not found.");
        		}

        } else {						// If no file argument is given, launch REPL
            
        		System.out.println("Welcome to Stack lang REPL");
        		scanner = new Scanner(System.in);
        	
	        while (true) {
	            try {
	                System.out.print("> ");
	                code = scanner.nextLine();
	                if (code.equals("exit")) break; // lets have a chance to stop the program
	                machine.run(code);
	                System.out.println("=> " + machine.getDataStack().toString());
	                
	            } catch (Exception e) {
	                e.printStackTrace();
	                break;
	            }
	            
	        }
        }

        scanner.close();

    }

}
