import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MovieLibrary implements ILibrary<Movie> {
	
	private List<Movie> movies;
	private String moviesPath;
		
	public MovieLibrary(String moviesPath) throws FileNotFoundException {
		this.moviesPath = moviesPath;
		movies = parseMovies(moviesPath);
	}
	
	public void add(Movie movie) {
		movies.add(movie);
		writeMovies();
	}
	
	public void remove(int index) {
		movies.remove(index);
		writeMovies();
	}
	
	private void writeMovies() {
		
		try {
			PrintWriter printWriter = new PrintWriter(moviesPath);
			printWriter.println(Movie.getCsvHeaderString());
			for (Movie movie : movies) {
				String csvRecord = movie.movieCsvRecord();
				printWriter.println(csvRecord);
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private List<Movie> parseMovies(String moviesPath) throws FileNotFoundException {
		
		FileReader reader = new FileReader(moviesPath);
		Scanner scanner = new Scanner(reader);
		
		// Read the movie from CSV
		List<Movie> movies = new ArrayList<Movie>();
		scanner.nextLine(); // skip header line
		while (scanner.hasNextLine()) {
			String csvRecord = scanner.nextLine();
			Movie movie = Movie.parseMovie(csvRecord);
			movies.add(movie);
		}
		scanner.close();
		
		return movies;

	}
	
	@Override
	public String toString() {
		String s = "";
		int index = 0;
		for (Movie movie : movies) {
			s += index + ": " + movie.toString() + "\n";
			index++;
		}
		return s;
	}
	
}
