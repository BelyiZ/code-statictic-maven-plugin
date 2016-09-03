package ru.belyiz

import java.util.*

/**
 * The object to calculate coefficients from the files rows and processed them
 * Create a counter is available only with the extension of the processed files
 *
 * @property filesExtension extension of files that will be used to calculate the coefficients
 * @constructor create an empty counter
 */
class Counter(private val filesExtension: String?) {

    /** The number of pure lines or line with spaces only */
    private var emptyLines: Long = 0

    /** The number of lines containing at least one character other than a space */
    private var notEmptyLines: Long = 0

    /** The total number of processed files rows */
    private val totalLinesCount: Long
        get() = emptyLines + notEmptyLines

    /** The number of annotations @Deprecated */
    private var deprecations = 0

    init {
        if (filesExtension == null) {
            throw IllegalArgumentException("Files extension must not be NULL")
        }
    }

    /**
     * Process one line of file to calculate coefficients. All result will be added to old values

     * @param line the line of processed file
     */
    fun processLine(line: String) {
        if (line.isBlank()) {
            emptyLines++
        } else {
            notEmptyLines++

            if (line.contains("@Deprecated")) {
                deprecations++
            }
        }
    }

    /**
     * Summation of the values of the two counters

     * @param counter the counter whose values will be added to the values of current counter
     */
    operator fun plus(counter: Counter): Counter {
        emptyLines += counter.emptyLines
        notEmptyLines += counter.notEmptyLines
        deprecations += counter.deprecations
        return this
    }

    override fun toString(): String {
        return "Counts for files [*" + filesExtension + "] :\n" +
                emptyLines + " empty lines\n" +
                notEmptyLines + " not empty lines\n" +
                totalLinesCount + " total lines\n" +
                deprecations + " deprecations\n"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Counter) return false
        return filesExtension == other.filesExtension
    }

    override fun hashCode(): Int = Objects.hash(filesExtension)
}
