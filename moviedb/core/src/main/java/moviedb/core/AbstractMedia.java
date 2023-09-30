package moviedb.core;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMedia {

    protected String name;
    protected int releaseYear;
    protected List<String> genres;
    protected double rating;

    public AbstractMedia(String name, int releaseYear, List<String> genres, double rating) {
        setName(name);
        setReleaseYear(releaseYear);
        setGenres(genres);
        setRating(rating);
    }

    public AbstractMedia() {
        genres = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        int currYear = LocalDate.now().getYear();
        if (releaseYear <= currYear && releaseYear > 1800) {
            this.releaseYear = releaseYear;
        } else {
            throw new IllegalArgumentException("Release year must be between 1800 and " + currYear);
        }
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void addGenre(String genre) {
        genres.add(genre);
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        if (rating <= 10 && rating >= 1) {
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Rating must be between 1 and 10");
        }
    }

    public String genreToString() {
        StringBuilder sb = new StringBuilder();
        for (String genre : this.genres) {
            sb.append(genre);
            if (!genres.get(genres.size() - 1).equals(genre)) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        return this.toString().equals(o.toString());
    }

    // From spotbugs: Overriding equals should also require overriding hashCode
    public int hashCode() {
        return 1;   // Method is not used, just returns an arbitrary number
    }

    @Override
    public abstract String toString();

}
