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

    public Object get(Object key) {
        return get(DEFAULT_REGION, key);
    }

    public Object get(String region, Object key) {
        Ehcache cache = getEhcacheByRegion(region);
        Element element = cache.get(key);
        if (element != null) {
            return element.getObjectValue();
        }

        return null;
    }

    public void delete(Object key) {
        delete(DEFAULT_REGION, key);
    }

    public void delete(String region, Object key) {
        Ehcache cache = getEhcacheByRegion(region);
        cache.remove(key);
    }

    public void put(Object key, Object value) {
        put(DEFAULT_REGION, key, value);
    }

    public void put(String region, Object key, Object value) {
        Ehcache cache = getEhcacheByRegion(region);
        cache.put(new Element(key, value));
    }

    public Boolean replace(Object key, Object value) {
        return replace(DEFAULT_REGION, key, value);
    }

    public Boolean replace(String region, Object key, Object value) {
        Ehcache cache = getEhcacheByRegion(region);
        Element replace = cache.replace(new Element(key, value));
        if (replace == null) {
            return false;
        }
        return true;
    }

    public Boolean putIfAbsent(Object key, Object value) {
        return putIfAbsent(DEFAULT_REGION, key, value);
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
            throw new NullPointerException("region cant be null");
        }

        Ehcache cache = ehcache.getEhcache(region);
        if (cache == null) {
            ehcache.addCache(region);
            cache = ehcache.getEhcache(region);
        }

        return cache;
    }

}
