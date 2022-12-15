package app;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "file-tree-as-json")
@Slf4j
public class FileTreeAsJson implements Callable<Integer> {

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Show usage")
    boolean usageHelp;

    @Option(names = "--debug", description = "Enable debug logs")
    void setDebug(boolean enabled) {
        ((Logger) LoggerFactory.getLogger(FileTreeAsJson.class)).setLevel(
            enabled ? Level.DEBUG : Level.INFO
        );
    }

    @Option(names = {"-o", "--output"}, paramLabel = "FILE",
        description = "If not provided, JSON is output to stdout"
    )
    Path outputFile;

    @Parameters(arity = "1..*", paramLabel = "PATH",
        description = "One or more paths to process"
    )
    List<Path> roots;

    public static void main(String[] args) {
        System.exit(
            new CommandLine(new FileTreeAsJson())
                .execute(args)
        );
    }

    @Override
    public Integer call() throws Exception {
        final ObjectWriter objectWriter = new ObjectMapper()
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .writer();

        final Map<Path, FileTreeEntry> roots = this.roots.stream()
            .collect(Collectors.toMap(path -> path, path -> {
                try {
                    return walkRoot(path);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to process root path", e);
                }
            }));

        if (outputFile != null) {
            System.out.printf("Wrote JSON to %s%n", outputFile);
            objectWriter.writeValue(outputFile.toFile(), roots);
        }
        else {
            System.out.println(objectWriter.writeValueAsString(roots));
        }

        return 0;
    }

    private FileTreeEntry walkRoot(Path rootPath) throws IOException {
        final FileTreeAccumulator accumulator = new FileTreeAccumulator();
        Files.walkFileTree(rootPath, accumulator);
        return accumulator.getRoot();
    }
}
