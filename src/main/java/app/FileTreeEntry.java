package app;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@JsonInclude(Include.NON_NULL)
public final class FileTreeEntry {

    @JsonSerialize(using = FilenameSerializer.class)
    private final Path path;
    private final List<FileTreeEntry> children;

    public static FileTreeEntry root() {
        return new FileTreeEntry(null, new ArrayList<>());
    }

    public static FileTreeEntry dir(Path path) {
        return new FileTreeEntry(path, new ArrayList<>());
    }

    public static FileTreeEntry file(Path path) {
        return new FileTreeEntry(path, null);
    }

    private FileTreeEntry(
        Path path,
        List<FileTreeEntry> children
    ) {
        this.path = path;
        this.children = children;
    }

    public void addChild(FileTreeEntry entry) {
        children.add(entry);
    }

}
