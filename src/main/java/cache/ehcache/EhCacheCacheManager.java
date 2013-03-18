/**
 * 
 */
package cache.ehcache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import cache.BaseCacheManager;

/**
 * @author zoza
 * 
 */
public class EhCacheCacheManager extends BaseCacheManager {

    private CacheManager ehcache = null;

    public CacheManager getEhCache() {
        return ehcache;
    }

    public void init() throws Exception {
        ehcache = CacheManager.create();
    }

    public void shutdown() {
        ehcache.shutdown();
        ehcache = null;
    }

    public Object get(String region, Object key) {
        Ehcache cache = getEhcacheByRegion(region);
        Element element = cache.get(key);
        if (element != null) {
            return element.getObjectValue();
        }
        return null;
    }

    public void delete(String region, Object key) {
        Ehcache cache = getEhcacheByRegion(region);
        cache.remove(key);
    }

    public void put(String region, Object key, Object value) {
        Ehcache cache = getEhcacheByRegion(region);
        cache.put(new Element(key, value));
    }

    public Boolean replace(String region, Object key, Object value) {
        Ehcache cache = getEhcacheByRegion(region);
        Element replace = cache.replace(new Element(key, value));
        if (replace == null) {
            return false;
        }
        return true;
    }

    public Boolean putIfAbsent(String region, Object key, Object value) {
        Ehcache cache = getEhcacheByRegion(region);
        Element replace = cache.putIfAbsent(new Element(key, value));
        if (replace == null) {
            return true;
        }
        return false;
    }

    public void clearAll() {
        ehcache.removalAll();

    }

    public void clearRegion(String region) {
        ehcache.removeCache(region);
    }

    private Ehcache getEhcacheByRegion(String region) {
        if (region == null) {
            throw new NullPointerException(REGION_CANT_BE_NULL);
        }

        Ehcache cache = ehcache.getEhcache(region);
        if (cache == null) {
            ehcache.addCache(region);
            cache = ehcache.getEhcache(region);
        }
        return cache;
    }

    @Override
    protected synchronized Long atomicAndGet(String region,
                                             Object key,
                                             long delta) {
        if (delta <= 0) {
            throw new IllegalArgumentException(DELTA_MUST_BE_POSITIVE);
        }
        if (key == null) {
            throw new NullPointerException(KEY_SHOULDT_BE_NULL);
        }

        Ehcache cache = getEhcacheByRegion(region);
        Long oldValue = (Long) get(region, key);
        if (oldValue == null) {
            oldValue = 0L;
        }

        Long newValue = oldValue + delta;
        cache.put(new Element(key, newValue));
        return newValue;

    }
}
