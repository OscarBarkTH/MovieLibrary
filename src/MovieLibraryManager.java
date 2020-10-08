import java.io.FileNotFoundException;
import java.util.Scanner;

public class MovieLibraryManager {

	private IMovieLibrary library;
	
	private enum Command {
		ADD,
		REMOVE,
		DISPLAY,
		QUIT,
		UNKNOWN
	}
	
	public MovieLibraryManager(String moviesPath) {
		try {
			library = new MovieLibrary(moviesPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("No file found at " + moviesPath);
			System.out.println("Exiting");
			System.exit(0);
		}
	}
	
	public void start() {
		
		boolean running = true;
		Scanner scanner = new Scanner(System.in); 
		
		while (running) {
			
			String userInput = scanner.nextLine();
			Command command = parseCommand(userInput);
			
			if (command == Command.QUIT) {
				running = false;
				System.out.println("Exiting.");
				continue;
			} else if (command == Command.UNKNOWN) {
				System.out.println("Unknown command.");
				continue;
			}
			
			String[] arguments = parseArguments(userInput);
			
			switch (command) {
			case ADD:
				addCommand(arguments);
				break;
			case REMOVE:
				removeCommand(arguments);
				break;
			case DISPLAY:
				displayCommand();
				break;
			}
			
		}
		
		scanner.close();
		
	}

	private void addCommand(String[] arguments) {
		String title;
		int runtime;
		float rating;
		try {
			title = arguments[0];
			runtime = Integer.parseInt(arguments[1]);
			rating = Float.parseFloat(arguments[2]);
		} catch (Exception e) {
			System.out.println("Failed to parse movie attributes from arguments.");
			return;
		}
		Movie movie = new Movie(title, runtime, rating);
		library.addMovie(movie);
	}
	

	private void removeCommand(String[] arguments) {
		int index;
		try {
			index = Integer.parseInt(arguments[0]);
		} catch (Exception e) {
			System.out.println("Failed to parse index from arguments.");
			return;
		}
		library.removeMovie(index);
	}
	
	private void displayCommand() {
		System.out.println(library);
	}

    public static Command parseCommand(String userInput) {
    	String commandString = userInput.split(" ")[0];
    	switch(commandString) {
    		case "add":
				return Command.ADD;
    		case "remove":
    			return Command.REMOVE;
    		case "display":
				return Command.DISPLAY;
    		case "quit":
    		case "exit":
    			return Command.QUIT;
			default:
				return Command.UNKNOWN;
    	}
    }
    
    public static String[] parseArguments(String userInput) {
        String[] commandAndArguments = userInput.split(" ");
        String[] arguments = new String[commandAndArguments.length - 1];
        for (int i=1; i<commandAndArguments.length; i++) {
            arguments[i-1] = commandAndArguments[i];
        }
        return arguments; // contains only the arguments
    }
	
	public static void main(String[] args) {
		MovieLibraryManager manager = new MovieLibraryManager("movies.csv");
		manager.start();
	}

}
