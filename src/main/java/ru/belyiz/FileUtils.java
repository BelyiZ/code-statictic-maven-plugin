package ru.belyiz;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * The set of static methods for working with files
 */
public final class FileUtils {

    private FileUtils() {
    }

    /**
     * Recursively finds all files in the folder {@code root}
     * All found files will be processed by the {@code consumer}
     *
     * @param root     the path to the root directory of target files, it may be path to the single file
     * @param consumer function to process all found files
     */
    public static void processFiles(File root, Consumer<File> consumer) {
        if (root.isFile()) {
            consumer.accept(root);
            return;
        }
        for (final File file : root.listFiles()) {
            if (file.isDirectory()) {
                processFiles(file, consumer);
            } else {
                consumer.accept(file);
            }
        }
    }

    /**
     * Open file, which path transmitted in {@code filePath} and process all rows by the {@code consumer}
     *
     * @param filePath the path target file
     * @param encoding charset which will be used to read file content
     * @param consumer function to process lines of the file
     *
     * @throws IOException
     */
    public static void processFileLines(String filePath, String encoding, Consumer<String> consumer) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filePath), Charset.forName(encoding))) {
            stream.forEach(line -> consumer.accept(line));
        }
    }

    /**
     * Parse extension of file from the name
     *
     * @param file target file
     *
     * @return parsed file extension or empty string
     */
    public static String getFileExtension(File file) {
        String fileName = file.getName();

        int dotPosition = fileName.lastIndexOf(".");
        if (dotPosition != -1) {
            return fileName.substring(dotPosition);
        }
        return "";
    }
}
