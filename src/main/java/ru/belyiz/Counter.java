package ru.belyiz;

import java.util.Objects;

/**
 * The object to calculate coefficients from the files rows and processed them
 */
public class Counter {

    /**
     * Extension of files that will be used to calculate the coefficients
     */
    private String filesExtension;

    /**
     * The number of pure lines or line with spaces only
     */
    private long emptyLines = 0;

    /**
     * The number of lines containing at least one character other than a space
     */
    private long notEmptyLines = 0;

    /**
     * The number of annotations @Deprecated
     */
    private int deprecations = 0;

    /**
     * Create a counter is available only with the extension of the processed files
     *
     * @param filesExtension extension of files that will be used to calculate the coefficients
     */
    public Counter(String filesExtension) {
        if (filesExtension == null) {
            throw new IllegalArgumentException("Files extension must not be NULL");
        }
        this.filesExtension = filesExtension;
    }

    /**
     * Process one line of file to calculate coefficients. All result will be added to old values
     *
     * @param line the line of processed file
     */
    public void processLine(String line) {
        if (line.trim().isEmpty()) {
            emptyLines++;
        } else {
            notEmptyLines++;

            if (line.contains("@Deprecated")) {
                deprecations++;
            }
        }
    }

    /**
     * Summation of the values of the two counters
     *
     * @param counter the counter whose values will be added to the values of current counter
     */
    public void plus(Counter counter) {
        emptyLines += counter.emptyLines;
        notEmptyLines += counter.notEmptyLines;
        deprecations += counter.deprecations;
    }

    /**
     * @return the total number of processed files rows
     */
    private long getTotalLinesCount() {
        return emptyLines + notEmptyLines;
    }

    @Override
    public String toString() {
        return "Counts for files [*" + filesExtension + "] :\n" +
                emptyLines + " empty lines\n" +
                notEmptyLines + " not empty lines\n" +
                getTotalLinesCount() + " total lines\n" +
                deprecations + " deprecations\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Counter counter = (Counter) o;
        return Objects.equals(filesExtension, counter.filesExtension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filesExtension);
    }
}
