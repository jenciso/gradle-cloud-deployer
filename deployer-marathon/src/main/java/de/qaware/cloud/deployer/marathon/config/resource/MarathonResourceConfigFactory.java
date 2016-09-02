package de.qaware.cloud.deployer.marathon.config.resource;

import de.qaware.cloud.deployer.commons.config.resource.BaseResourceConfigFactory;
import de.qaware.cloud.deployer.commons.config.resource.ContentType;
import de.qaware.cloud.deployer.commons.error.ResourceConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MarathonResourceConfigFactory extends BaseResourceConfigFactory<MarathonResourceConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarathonResourceConfigFactory.class);

    @Override
    public List<MarathonResourceConfig> createConfigs(List<File> files) throws ResourceConfigException {

        LOGGER.info("Reading marathon config files...");

        List<MarathonResourceConfig> resourceConfigs = new ArrayList<>();
        for (File file : files) {
            String filename = file.getName();
            ContentType contentType = retrieveContentType(file);
            String content = readFileContent(file);
            resourceConfigs.add(new MarathonResourceConfig(filename, contentType, content));
        }

        LOGGER.info("Finished reading marathon config files...");

        return resourceConfigs;
    }
}
