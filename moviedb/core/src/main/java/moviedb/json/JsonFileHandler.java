package moviedb.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.nio.file.Path;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import moviedb.core.AbstractObservableDatabase;
import moviedb.core.LocalDatabaseSaver;
import moviedb.core.MovieDatabase;
import moviedb.core.SeriesDatabase;
import moviedb.json.internal.MovieModule;
import moviedb.json.internal.SeriesModule;

import static moviedb.core.LocalDatabaseSaver.persistenceDir;
import static moviedb.core.LocalDatabaseSaver.sep;

public class JsonFileHandler {

    private final File file;
    private final ObjectMapper mapper;

    public JsonFileHandler(Path path) {
        initializeFiles();
        String[] pathArray = path.toString().split(Pattern.quote(System.getProperty("file.separator")));
        if (pathArray[pathArray.length - 1].equals("movies.json")) {
            mapper = createMovieObjectMapper();
        } else {
            mapper = createSeriesObjectMapper();
        }
        file = new File(path.toString());
    }

    public static ObjectMapper createSeriesObjectMapper() {
        SimpleModule module = createSeriesJacksonModule();
        return new ObjectMapper().registerModule(module);
    }

    public static SimpleModule createSeriesJacksonModule() {
        return new SeriesModule();
    }

    public static SimpleModule createMovieJacksonModule() {
        return new MovieModule();
    }

    public static ObjectMapper createMovieObjectMapper() {
        SimpleModule module = createMovieJacksonModule();
        return new ObjectMapper().registerModule(module);
    }

    public MovieDatabase readMovieDatabase() throws IOException {
        try {
            return mapper.readValue(file, MovieDatabase.class);
        } catch (MismatchedInputException e) { // json-file is empty, return empty movieDatabase-object
            return new MovieDatabase();
        }
    }

    public SeriesDatabase readSeriesDatabase() throws IOException {
        try {
            return mapper.readValue(file, SeriesDatabase.class);
        } catch (MismatchedInputException e) { // json-file is empty, return empty seriesDatabase-object
            return new SeriesDatabase();
        }
    }

    public void writeDatabase(AbstractObservableDatabase database) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, database);
    }

    // checks if the persistence files already exist, and makes the directory and the files if not
    private void initializeFiles() {
        boolean created;
        File moviesFile = new File(persistenceDir + sep + "movies.json");
        File seriesFile = new File(persistenceDir + sep + "series.json");
        if (! (moviesFile.isFile() && seriesFile.isFile())) {
            File persistenceDir = new File(LocalDatabaseSaver.persistenceDir);
            created = persistenceDir.mkdirs(); // makes the folders
            if (created) {
                boolean moviesCreated;
                boolean seriesCreated;
                try {
                    // creates empty json files
                    moviesCreated = moviesFile.createNewFile();
                    seriesCreated = seriesFile.createNewFile();
                    if (! moviesCreated || ! seriesCreated) {
                        throw new RuntimeException("Could not create persistence files!");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                throw new RuntimeException("Could not create directory for persistence files!");
            }
        }
    }

}
