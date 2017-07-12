package utils.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesService {

    private static final Logger LOG = LoggerFactory.getLogger(PropertiesService.class);
    private static PropertiesService instance = null;
    private final Properties properties = new Properties();

    private PropertiesService() {
        try {
            loadProperties(System.getProperty("env"));
        } catch (final IOException e) {
            throw new IllegalStateException("Failed to load environment configuration file", e);
        }
    }

    public static String getProperty(final String propertyName) {
        if (instance == null) {
            instance = new PropertiesService();
        }
        return System.getProperty(propertyName, instance.properties.getProperty(propertyName));
    }

    private void loadProperties(final String resource) throws IOException {
        LOG.info(String.format("Reading environment properties: %s.properties", resource));
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(String.format("properties/%s.properties", resource));
        if (inputStream == null) {
            throw new IOException("Unable to open stream for resource " + resource);
        }
        final Properties props = new Properties();
        props.load(inputStream);
        inputStream.close();
        for (final String propertyName : props.stringPropertyNames()) {
            if (propertyName.startsWith("+")) {
                loadProperties(propertyName.substring(1));
            }
        }
        properties.putAll(props);
    }

}
