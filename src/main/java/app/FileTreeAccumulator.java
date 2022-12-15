package app;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Deque;
import java.util.LinkedList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileTreeAccumulator extends SimpleFileVisitor<Path> {

    private final Deque<FileTreeEntry> stack = new LinkedList<>();
    private FileTreeEntry root;

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        log.debug("PRE DIR: {}", dir);

        final FileTreeEntry ours;
        if (!stack.isEmpty()) {
            ours = FileTreeEntry.dir(dir);
            //noinspection DataFlowIssue
            stack.peek().addChild(ours);
        }
        else {
            ours = FileTreeEntry.root();
            root = ours;
        }
        stack.push(ours);

        return FileVisitResult.CONTINUE;
    }

    public FileTreeEntry getRoot() {
        return root;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        log.debug("FILE: {}", file);
        if (!stack.isEmpty()) {
            stack.peek().addChild(FileTreeEntry.file(file));
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        log.debug("POST DIR: {}", dir);
        stack.pop();
        return FileVisitResult.CONTINUE;
    }
}
