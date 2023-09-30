package moviedb.json.internal;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import moviedb.core.Series;
import moviedb.core.SeriesDatabase;

public class SeriesModule extends SimpleModule {
    
    private static final String NAME = "SeriesModule";

    public SeriesModule() {
        super(NAME, Version.unknownVersion());
        addSerializer(Series.class, new SeriesSerializer());
        addDeserializer(Series.class, new SeriesDeserializer());

        addSerializer(SeriesDatabase.class, new SeriesDatabaseSerializer());
        addDeserializer(SeriesDatabase.class, new SeriesDatabaseDeserializer());
    }

}
