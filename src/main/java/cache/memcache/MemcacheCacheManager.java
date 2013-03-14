/**
 * 
 */
package cache.memcache;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import cache.BaseCacheManager;

/**
 * @author zoza
 * 
 */
public class MemcacheCacheManager extends BaseCacheManager {

    public MemcacheCacheManager() {
        super();
    }

    private static final String BINARY_KEY = "binary";
    private static final String REGION_DELIMITER = ".";
    private static final String DEFAULT_CONFIG_FILE = "/memcache.ini";

    private MemcachedClient memcache = null;
    private Properties config = new Properties();
   
    public MemcachedClient getMemcache() {
        return memcache;
    }

    
    
    public void shutdown() {
        memcache.shutdown();
        memcache = null;
    }

   
    public synchronized void init() throws Exception {

        if (getConfigFile() == null) {
            setConfigFile(DEFAULT_CONFIG_FILE);
        }

        InputStream stream = getClass().getResourceAsStream(getConfigFile());

        config.load(stream);

        Boolean binary = Boolean.valueOf(config.getProperty(BINARY_KEY));

        List<InetSocketAddress> serverList = new ArrayList<InetSocketAddress>();

        for (Object key : config.keySet()) {
            if (key.toString().startsWith("server")) {
                String value = config.getProperty(key.toString());

                // split localhost:11211
                int indexOf = value.indexOf(":");
                serverList.add(new InetSocketAddress(value
                        .substring(0, indexOf), Integer.parseInt(value
                        .substring(indexOf + 1, value.length()))));
            }
        }

        if (serverList.size() == 0) {
            throw new Exception(
                    "server property is mandatory. add server=localhost:11211 to memcache.ini file");
        }

        if (binary) {
            memcache = new MemcachedClient(new BinaryConnectionFactory(),
                    serverList);
        } else {
            memcache = new MemcachedClient(serverList);
        }

    }

    public Object get(Object key) {
        return get(DEFAULT_REGION, key);
    }

    public Object get(String region, Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return memcache.get(region + REGION_DELIMITER + key.toString());
    }

    public void put(Object key, Object value) {
        put(DEFAULT_REGION, key, value);
    }

    public void put(String region, Object key, Object value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }

        memcache.set(getFullKey(region, key),
                getExpirationByRegion(region), value);
    }

    public void delete(Object key) {
        delete(DEFAULT_REGION, key);
    }

    public void delete(String region, Object key) {
        memcache.delete(getFullKey(region, key));
    }

    private String getFullKey(String region, Object key) {
        return region + REGION_DELIMITER + key;
    }

    private Integer getExpirationByRegion(String region) {
        if (region == null) {
            throw new NullPointerException("region cant be null");
        }
        String ttl = config.getProperty(region);
        if (ttl == null) {
            ttl = config.getProperty(DEFAULT_REGION);
        }
        return Integer.parseInt(ttl);
    }

}
