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

    public Object get(String region, Object key) {
        return memcache.get(getFullKey(region, key));
    }

    public void put(String region, Object key, Object value) {
        if (value == null) {
            throw new NullPointerException(VALUE_SHOULDT_BE_NULL);
        }
        memcache.set(getFullKey(region, key), getExpirationByRegion(region),
                value);
    }

    public void delete(String region, Object key) {
        memcache.delete(getFullKey(region, key));
    }

    public void clearAll() {
        memcache.flush();
    }

    public void clearRegion(String region)
            throws OperationNotSupportedException {
        throw new OperationNotSupportedException(
                "clearRegion not supported for Memcache");
    }

    private String getFullKey(String region, Object key) {
        if (region == null) {
            throw new NullPointerException(REGION_CANT_BE_NULL);
        }
        if (key == null) {
            throw new NullPointerException(KEY_SHOULDT_BE_NULL);
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

    public Boolean replace(String region, Object key, Object value) {
        if (value == null) {
            throw new NullPointerException(VALUE_SHOULDT_BE_NULL);
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

    public Boolean putIfAbsent(String region, Object key, Object value) {
        if (value == null) {
            throw new NullPointerException(VALUE_SHOULDT_BE_NULL);
        }
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

    @Override
    protected Long atomicAndGet(String region, Object key, long delta) {
        Long result = null;
        if (delta <= 0) {
            throw new IllegalArgumentException(DELTA_MUST_BE_POSITIVE);
        }
        if (key == null) {
            throw new NullPointerException(KEY_SHOULDT_BE_NULL);
        }
        //this line is adding default init value if its not exists yet
        memcache.add(getFullKey(region, key), getExpirationByRegion(region),
                "0");
        result = memcache.incr(getFullKey(region, key), delta, 0,
                getExpirationByRegion(region));

        if (result == -1) {
            return null;
        }
        return result;
    }

}
