import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class MovieLibrary implements IMovieLibrary {
	
	private Movie[] movies;
	private String moviesPath;
	
	static final int MOVIE_LIBRARY_CAPACITY = 999;
	
	public MovieLibrary(String moviesPath) throws FileNotFoundException {
		this.moviesPath = moviesPath;
		movies = parseMovies(moviesPath);
	}
	
	public void addMovie(Movie movie) {
		Movie[] newMovies = new Movie[movies.length+1];
		for (int i=0; i<movies.length; i++) {
			newMovies[i] = movies[i];
		}
		newMovies[newMovies.length-1] = movie;
		movies = newMovies;
		writeMovies();
	}
	
	public void removeMovie(int index) {
		Movie[] newMovies = new Movie[movies.length-1];
		int i = 0;
		for (int j=0; j<newMovies.length; j++) {
			if (j==index) i++;
			newMovies[j] = movies[i];
			i++;
		}
		movies = newMovies;
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

	private Movie[] parseMovies(String moviesPath) throws FileNotFoundException {
		
		FileReader reader = new FileReader(moviesPath);
		Scanner scanner = new Scanner(reader);
		
		// Read the movie from CSV
		Movie[] movies = new Movie[MOVIE_LIBRARY_CAPACITY];
		int recordIndex = 0;
		scanner.nextLine(); // skip header line
		while (scanner.hasNextLine()) {
			String csvRecord = scanner.nextLine();
			movies[recordIndex] = Movie.parseMovie(csvRecord);
			recordIndex++;
		}
		scanner.close();
		
		// Remove empty spots in movies
		Movie[] moviesNew = new Movie[recordIndex];
		for (int i=0; i<recordIndex; i++) {
			moviesNew[i] = movies[i];
		}
		movies = moviesNew;
		
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
