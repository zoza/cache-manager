import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cache.CacheManager;
import cache.memcache.MemcacheCacheManager;

public class TestMemcache extends BaseTestCache {
    static CacheManager cacheManager;

    @BeforeClass
    public static void start() throws Exception {
        cacheManager = new MemcacheCacheManager();
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
    public void clearCustomRegion() {
        // for memcache clearRegion is NA
    }

    @Override
    @Test
    public void clearDefaultRegion() {
        // for memcache clearRegion is NA
    }
}
