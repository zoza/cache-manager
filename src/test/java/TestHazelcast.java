import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cache.CacheManager;
import cache.hazelcast.HazelcastCacheManager;

/**
 * 
 */

/**
 * @author zoza
 * 
 */
public class TestHazelcast extends BaseTestCache {
    static CacheManager cacheManager;

    @BeforeClass
    public static void start() throws Exception {
        cacheManager = new HazelcastCacheManager();
        cacheManager.init();
    }

    @Override
    protected CacheManager getCacheManager() {
        return cacheManager;
    }

    @AfterClass
    public static void stop() {
        cacheManager.shutdown();
    }
    @Override
    @Test
    public void clearAll() {
        // for HZ clearAll is NA
    }
}
