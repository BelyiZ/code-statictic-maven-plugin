package ru.belyiz

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import java.io.File
import java.io.IOException
import java.util.*

/**
 * Goal to calcutate some coefficients by parsins files in sources directory
 */
@Mojo(name = "codeLineCount", defaultPhase = LifecyclePhase.COMPILE)
class CodeLinesCounterMojo : AbstractMojo() {

    /**
     * Results of calculation within the extensions of processed files
     */
    private val fileTypes = HashMap<String, Counter>()

    /**
     * Path to root folder with source files specified in the POM-file
     * This folder will be used for recursively searching of files and calculate coefficients
     */
    @Parameter(defaultValue = "${'$'}{project.build.sourceDirectory}", readonly = true, required = true)
    private val sourceDirectory = File("")

    /**
     * Encoding of sourse files
     */
    @Parameter(defaultValue = "${'$'}{project.build.sourceEncoding}")
    private val encoding = "UTF-8"

    @Throws(MojoExecutionException::class)
    override fun execute() {
        if (!sourceDirectory.exists()) {
            log.error("Source directory \"$sourceDirectory\" is not valid.")
            return
        }

        FileUtils.processFiles(sourceDirectory) { file ->
            val extension = FileUtils.getFileExtension(file)
            val counter = fileTypes.computeIfAbsent(extension, { Counter(it) })
            val fileAbsolutePath = file.absolutePath
            try {
                FileUtils.processFileLines(fileAbsolutePath, encoding, { counter.processLine(it) })
            } catch (e: IOException) {
                log.error("Error while reading source file [$fileAbsolutePath]", e)
            }
        }

        writeResultsToLog()
    }

    /**
     * Write calculation results to the process log
     * If count of processed files extensions more than one, then will be printed general result for all processed files
     */
    private fun writeResultsToLog() {
        var generalCounter = Counter(".*")

        for ((key, counter) in fileTypes) {
            log.info(counter.toString())
            generalCounter += counter
        }
        if (fileTypes.size > 1) {
            log.info(generalCounter.toString())
        }
    }
}