package ru.belyiz;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Goal to calcutate some coefficients by parsins files in sources directory
 *
 * @goal codeLineCount
 * @phase compile
 */
public class CodeLinesCounterMojo extends AbstractMojo {

    /**
     * Results of calculation within the extensions of processed files
     */
    private Map<String, Counter> fileTypes = new HashMap<String, Counter>();

    /**
     * Path to root folder with source files specified in the POM-file
     * This folder will be used for recursively searching of files and calculate coefficients
     *
     * @parameter expression="${project.build.sourceDirectory}"
     * @readonly
     * @required
     */
    private final File sourceDirectory = new File("");

    /**
     * Encoding of sourse files
     *
     * @parameter expression="${encoding}"
     * default-value="${project.build.sourceEncoding}"
     */
    private final String encoding = "UTF-8";

    @Override
    public void execute() throws MojoExecutionException {
        if (!sourceDirectory.exists()) {
            getLog().error("Source directory \"" + sourceDirectory + "\" is not valid.");
            return;
        }

        FileUtils.processFiles(sourceDirectory, file -> {
            String extension = FileUtils.getFileExtension(file);
            Counter counter = fileTypes.computeIfAbsent(extension, Counter::new);
            String fileAbsolutePath = file.getAbsolutePath();
            try {
                FileUtils.processFileLines(fileAbsolutePath, encoding, counter::processLine);
            } catch (IOException e) {
                getLog().error("Error while reading source file [" + fileAbsolutePath + "]", e);
            }
        });

        writeResultsToLog();
    }

    /**
     * Write calculation results to the process log
     * If count of processed files extensions more than one, then will be printed general result for all processed files
     */
    private void writeResultsToLog() {
        Counter generalCounter = new Counter(".*");

        for (Map.Entry<String, Counter> entry : fileTypes.entrySet()) {
            Counter counter = entry.getValue();
            getLog().info(counter.toString());
            generalCounter.plus(counter);
        }
        if (fileTypes.size() > 1) {
            getLog().info(generalCounter.toString());
        }
    }
}