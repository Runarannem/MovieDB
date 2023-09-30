package moviedb.core;

import java.util.List;

public class Movie extends AbstractMedia {
    private int runtime;

    public Movie(String name, int releaseYear, List<String> genres, int runtime, double rating) {
        super(name, releaseYear, genres, rating);
        setRuntime(runtime);

    }

    public Movie() {
        super();
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        if (runtime >= 1) {
            this.runtime = runtime;
        } else {
            throw new IllegalArgumentException("Runtime must be greater than 1 minute");
        }
    }

    @Override
    public String toString() {
        return "Name: " + name + " - "
                + "Release year: " + releaseYear + " - "
                + "Genre(s): " + genreToString() + " - "
                + "Runtime: " + runtime + " - "
                + "Rating: " + rating;
    }

}
