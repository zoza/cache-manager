import org.junit.AfterClass;
import org.junit.BeforeClass;

import cache.CacheManager;
import cache.ehcache.EhCacheCacheManager;

/**
 * 
 */

/**
 * @author zoza
 * 
 */
public class TestEhCache extends BaseTestCache {
    static CacheManager cacheManager;

    @BeforeClass
    public static void start() throws Exception {
        cacheManager = new EhCacheCacheManager();
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

}
