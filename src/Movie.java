
public class Movie {
	
	private String title;
	private int runtime;
	private float rating;
	
	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getTitle() {
		return title;
	}

	public int getRuntime() {
		return runtime;
	}

	public Movie(String title, int runtime, float rating) {
		super();
		this.title = title;
		this.runtime = runtime;
		this.rating = rating;
	}
	
	public String movieCsvRecord() {
		return String.format("%s,%d,%.2f", title, runtime, rating);
	}

	public static String getCsvHeaderString() {
		return "title,runtime (minutes),imdb_rating";
	}

	public static Movie parseMovie(String csvRecord) {
		String[] values = csvRecord.split(",");
		String title = values[0];
		int runtime = Integer.parseInt(values[1]);
		float rating = Float.parseFloat(values[2]);
		return new Movie(title, runtime, rating);
	}
	
	@Override
	public String toString() {
		return String.format("%s: Runtime %dm, Rating %.2f", title, runtime, rating);
	}
	
}
