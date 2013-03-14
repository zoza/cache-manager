/**
 * 
 */
package cache;

/**
 * @author zoza
 * 
 */
public abstract class BaseCacheManager implements CacheManager {
    private String configFile;
    protected static final String DEFAULT_REGION = "defaultRegion";

    public BaseCacheManager() {

    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

}
