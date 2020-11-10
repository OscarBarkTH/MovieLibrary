import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.*;

public class MovieLibraryManager {
	
	// State
	ILibrary<Movie> movieLibrary;
	
	// GUI
	// Contains everything
	JFrame frame;
	
	// Used to add Movie
	JButton addButton;
	JTextField titleField;
	JTextField runtimeField;
	JTextField ratingField;
	
	// Used to remove movie (and see movies)
	JButton removeButton;
	JList<String> movieList;
	
	// Labels of buttons. Also used register correct action to correct button.
	private final String ADD_BUTTON_LABEL = "Add";
	private final String REMOVE_BUTTON_LABEL = "Remove";
	
	public MovieLibraryManager(String moviesPath) {
		
		// Initialize library (state model)
		try {
			movieLibrary = new MovieLibrary(moviesPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("No file found at " + moviesPath);
			System.out.println("Exiting");
			System.exit(0);
		}
		
		// Build the JFrame
		frame = new JFrame("Movie Library Manager");
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// JList
		movieList = new JList<String>(getMovieStrings());
		movieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Wrap JList in scrollable JScrollPane
		JScrollPane scrollPane = new JScrollPane(movieList);

		// Add button and text fields
		JPanel addPanel = new JPanel(new GridLayout(8, 1));
		addButton = new JButton(ADD_BUTTON_LABEL);
		titleField = new JTextField();
		runtimeField = new JTextField();
		ratingField = new JTextField();
		
		// Add labels to text fields
		JLabel titlePrompt = new JLabel("Enter title:");
		titlePrompt.setVerticalAlignment(SwingConstants.BOTTOM);
		JLabel runtimePrompt = new JLabel("Enter runtime:");
		runtimePrompt.setVerticalAlignment(SwingConstants.BOTTOM);		
		JLabel ratingPrompt = new JLabel("Enter rating:");
		ratingPrompt.setVerticalAlignment(SwingConstants.BOTTOM);		
		
		// Put buttons and text field in a single panel
		addPanel.add(titlePrompt);
		addPanel.add(titleField);
		addPanel.add(runtimePrompt);
		addPanel.add(runtimeField);
		addPanel.add(ratingPrompt);
		addPanel.add(ratingField);
		addPanel.add(new JPanel());
		addPanel.add(addButton);
		
		// Remove button
		removeButton = new JButton(REMOVE_BUTTON_LABEL);
		
		// Add components to frame
		frame.add(scrollPane, BorderLayout.WEST);
		frame.add(addPanel, BorderLayout.CENTER);
		frame.add(removeButton, BorderLayout.EAST);
		
		// Create and register listener
		ButtonListener listener = new ButtonListener();
		addButton.addActionListener(listener);
		removeButton.addActionListener(listener);
		
		// Finally, set visible
		frame.setVisible(true);
		
	}
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			// If the add button was pressed, parse a movie from the text fields and add it to the library
			if (e.getActionCommand().equals(ADD_BUTTON_LABEL)) {
				try {
					Movie movie = parseMovieFromFields();
					movieLibrary.add(movie);
					System.out.println("Attempt to add movie " + movie.toString());
				} catch (IllegalArgumentException ex) {
					System.out.println("Failed to parse movie from text fields.");
				}
			} 
			
			// If the remove button was pressed, check which movie in the list of movies is currently selected and remove that movie
			else if (e.getActionCommand().equals(REMOVE_BUTTON_LABEL)) {
				int index = movieList.getSelectedIndex(); // assume JList indices match the movie library indices, may not always be true
				if (index == -1) {
					System.out.println("Must select a movie to remove");
					return;
				}
				movieLibrary.remove(index);
				System.out.println("Attempt to remove movie " + index);
			}
			refreshFrame();
		}
		
	}
	
	// Clear text fields and fill the JList with the current library of movies
	private void refreshFrame() {
		movieList.setListData(getMovieStrings());
		titleField.setText("");
		runtimeField.setText("");
		ratingField.setText("");
	}
	
	// Given the currently entered text in the text fields, parse a Movie-object
	private Movie parseMovieFromFields() throws IllegalArgumentException {
		Movie movie = null;
		try {
			String title = titleField.getText();
			int runtime = Integer.parseInt(runtimeField.getText());
			float rating = Float.parseFloat(ratingField.getText());
			movie = new Movie(title, runtime, rating);
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to parse movie from text fields. Try again.");
		}
		return movie;
	}
	
	// Get a String[] list with movie index and title for each movie in the library.
	// A bit trickier than it should have to be, but the MovieLibrary-class does not have a b etter way to expose the needed information currently
	private String[] getMovieStrings() {
		String allMovies = movieLibrary.toString();
		String[] individualMovies = allMovies.split("\n");
		for (int i=0; i<individualMovies.length; i++) {
			individualMovies[i] = individualMovies[i].split(": Runtime")[0];
		}
		return individualMovies;
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MovieLibraryManager("movies.csv");
			}
		});
	}
	
}
