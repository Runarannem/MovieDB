package moviedb.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import moviedb.json.JsonFileHandler;

public class MoviedbWebClient {

    private MovieDatabase movieDatabase;
    private SeriesDatabase seriesDatabase;
    private static ObjectMapper movieObjectMapper = JsonFileHandler.createMovieObjectMapper();
    private static ObjectMapper seriesObjectMapper = JsonFileHandler.createSeriesObjectMapper();
    private final URI baseUri;


    public MoviedbWebClient(String baseUri) {
        this.baseUri = URI.create(baseUri);
    }

    public MovieDatabase getMovieDatabase() throws IOException, InterruptedException {
        if (movieDatabase == null) {
            HttpRequest request = HttpRequest.newBuilder(URI.create(baseUri + "/movies"))
                    .GET()
                    .build();
            final HttpResponse<String> response =
                    HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
            movieDatabase = movieObjectMapper.readValue(response.body(), MovieDatabase.class);
        }
        return movieDatabase;
    }

    public SeriesDatabase getSeriesDatabase() throws IOException, InterruptedException {
        if (seriesDatabase == null) {
            HttpRequest request = HttpRequest.newBuilder(URI.create(baseUri + "/series"))
                    .GET()
                    .build();
            final HttpResponse<String> response =
                    HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
            seriesDatabase = seriesObjectMapper.readValue(response.body(), SeriesDatabase.class);
        }
        return seriesDatabase;
    }

    // updates remote server to match that of the movieDatabase taken as parameter
    public void updateRemoteMovies(MovieDatabase movieDatabase) {
        String json;
        try {
            json = movieObjectMapper.writeValueAsString(movieDatabase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUri + "/movies"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        final HttpResponse<String> response;
        try {
            HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // updates remote server to match that of the seriesDatabase taken as parameter
    public void updateRemoteSeries(SeriesDatabase seriesDatabase) {
        String json;
        try {
            json = seriesObjectMapper.writeValueAsString(seriesDatabase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUri + "/series"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        final HttpResponse<String> response;
        try {
            HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isRunning() {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUri + "/ping"))
                .GET()
                .build();
        final HttpResponse<String> response;
        try {
            response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return false;
        }
        return response.body().equals("pong");
    }

}
