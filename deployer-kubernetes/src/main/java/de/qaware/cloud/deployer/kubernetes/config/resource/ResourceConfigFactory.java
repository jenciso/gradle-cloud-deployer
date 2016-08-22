package de.qaware.cloud.deployer.kubernetes.config.resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceConfigFactory {

    private static final String KUBERNETES_CONFIG_SEPARATOR = "---";
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceConfig.class);

    private ResourceConfigFactory() {
    }

    public static List<ResourceConfig> getConfigs(List<File> files) throws IOException {

        LOGGER.info("Reading config files...");

        List<ResourceConfig> resourceConfigs = new ArrayList<>();
        for (File file : files) {
            String filename = file.getName();
            String fileEnding = FilenameUtils.getExtension(file.getName());
            ContentType contentType = retrieveContentType(fileEnding);
            String content = FileUtils.readFileToString(file, Charset.defaultCharset());
            resourceConfigs.add(new FileResourceConfig(filename, contentType, content));
        }

        resourceConfigs = splitConfigs(resourceConfigs, KUBERNETES_CONFIG_SEPARATOR);

        LOGGER.info("Finished reading config files...");

        return resourceConfigs;
    }

    private static List<ResourceConfig> splitConfigs(List<ResourceConfig> resourceConfigs, String splitString) throws IOException {
        List<ResourceConfig> splitResourceConfigs = new ArrayList<>();
        for (ResourceConfig resourceConfig : resourceConfigs) {
            List<String> splitContents = splitContent(resourceConfig.getContent(), splitString);
            for (String splitContent : splitContents) {
                if (resourceConfig instanceof FileResourceConfig) {
                    FileResourceConfig fileResourceConfig = (FileResourceConfig) resourceConfig;
                    splitResourceConfigs.add(new FileResourceConfig(fileResourceConfig.getFilename(), resourceConfig.getContentType(), splitContent));
                } else {
                    splitResourceConfigs.add(new ResourceConfig(resourceConfig.getContentType(), splitContent));
                }
                LOGGER.info("- " + resourceConfig);
            }
        }
        return splitResourceConfigs;
    }

    private static ContentType retrieveContentType(String fileEnding) {
        switch (fileEnding) {
            case "json":
                return ContentType.JSON;
            case "yml":
                return ContentType.YAML;
            default:
                throw new IllegalArgumentException("Unknown content type for file ending: " + fileEnding);
        }
    }

    private static List<String> splitContent(String content, String splitString) {
        return Arrays.asList(content.split(splitString));
    }
}

