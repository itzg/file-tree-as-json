package app;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Serializes a {@link Path} as just its filename
 */
public class FilenameSerializer extends JsonSerializer<Path> {

    @Override
    public void serialize(Path value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getFileName().toString());
    }
}
