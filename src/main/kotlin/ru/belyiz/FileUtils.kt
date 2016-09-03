package ru.belyiz

import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

/**
 * The set of static methods for working with files
 */
object FileUtils {

    /**
     * Recursively finds all files in the folder `root`
     * All found files will be processed by the `consumer`
     *
     * @param root     the path to the root directory of target files, it may be path to the single file
     * @param consumer function to process all found files
     */
    fun processFiles(root: File, consumer: (File) -> Unit) {
        if (root.isFile) {
            consumer.invoke(root)
            return
        }
        for (file in root.listFiles()!!) {
            if (file.isDirectory) {
                processFiles(file, consumer)
            } else {
                consumer.invoke(file)
            }
        }
    }

    /**
     * Open file, which path transmitted in `filePath` and process all rows by the `consumer`
     *
     * @param filePath the path target file
     * @param encoding charset which will be used to read file content
     * @param consumer function to process lines of the file
     * @throws IOException
     */
    @Throws(IOException::class)
    fun processFileLines(filePath: String, encoding: String, consumer: (String) -> Unit) =
            Files.lines(Paths.get(filePath), Charset.forName(encoding)).forEach(consumer)


    /**
     * Parse extension of file from the name
     *
     * @param file target file
     * @return parsed file extension or empty string
     */
    fun getFileExtension(file: File): String {
        val fileName = file.name

        val dotPosition = fileName.lastIndexOf(".")
        if (dotPosition != -1) {
            return fileName.substring(dotPosition)
        }
        return ""
    }
}
