/**
 * 
 */
package cache.memcache;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.naming.OperationNotSupportedException;

import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;
import cache.BaseCacheManager;

/**
 * @author zoza
 * 
 */
public class MemcacheCacheManager extends BaseCacheManager {

    private static final String KEY_SHOULD_T_BE_NULL = "key should't be null";
    private static final String KEY_AND_VALUE_SHOULD_T_BE_NULL = "key and value should't be null";

    public MemcacheCacheManager() {
        super();
    }

    private static final String BINARY_KEY = "binary";
    private static final String REGION_DELIMITER = ".";
    private static final String DEFAULT_CONFIG_FILE = "/memcache.ini";
    private Long timeout = 1000L;
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

        InputStream stream = getClass()
                .getResourceAsStream(DEFAULT_CONFIG_FILE);

        config.load(stream);

        Boolean binary = Boolean.valueOf(config.getProperty(BINARY_KEY));

        timeout = Long.parseLong(config.getProperty("timeout"));

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
            throw new NullPointerException(KEY_SHOULD_T_BE_NULL);
        }
        return memcache.get(region + REGION_DELIMITER + key.toString());
    }

    public void put(Object key, Object value) {
        put(DEFAULT_REGION, key, value);
    }

    public void put(String region, Object key, Object value) {
        if (key == null || value == null) {
            throw new NullPointerException(KEY_AND_VALUE_SHOULD_T_BE_NULL);
        }

        memcache.set(getFullKey(region, key), getExpirationByRegion(region),
                value);
    }

    public void delete(Object key) {
        delete(DEFAULT_REGION, key);
    }

    public void delete(String region, Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        memcache.delete(getFullKey(region, key));

    }

    public void clearAll() {
        memcache.flush();
    }

    public void clearRegion(String region) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("clearRegion not supported for Memcache");
    }

    private String getFullKey(String region, Object key) {
        if (region == null) {
            throw new NullPointerException("region cant be null");
        }
        return region + REGION_DELIMITER + key;
    }

    private Integer getExpirationByRegion(String region) {
        String ttl = null;
        if (region != null) {
            ttl = config.getProperty(region);
        }

        if (ttl == null) {
            ttl = config.getProperty(DEFAULT_REGION);
        }
        return Integer.parseInt(ttl);
    }

    public Boolean replace(Object key, Object value) {
        return replace(DEFAULT_REGION, key, value);
    }

    public Boolean replace(String region, Object key, Object value) {
        if (key == null || value == null) {
            throw new NullPointerException(KEY_AND_VALUE_SHOULD_T_BE_NULL);
        }
        Boolean result = null;
        OperationFuture<Boolean> future = memcache.replace(
                getFullKey(region, key), getExpirationByRegion(region), value);
        try {
            result = future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            future.cancel();
        }
        return result;
    }

    public Boolean putIfAbsent(Object key, Object value) {
        return putIfAbsent(DEFAULT_REGION, key, value);
    }

    public Boolean putIfAbsent(String region, Object key, Object value) {
        Boolean result = null;
        OperationFuture<Boolean> future = memcache.add(getFullKey(region, key),
                getExpirationByRegion(region), value);
        try {
            result = future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            future.cancel();
        }
        return result;
    }

}
