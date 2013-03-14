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
        Ehcache cache = ehcache.getEhcache(region);
        if (cache != null) {
            Element element = cache.get(key);
            if (element != null) {
                return element.getObjectValue();
            }
        }
        return null;
    }

    public void delete(Object key) {
        // TODO Auto-generated method stub

    }

    public void delete(String region, Object key) {
        // TODO Auto-generated method stub

    }

    public void put(Object key, Object value) {
        put(DEFAULT_REGION, key, value);
    }

    public void put(String region, Object key, Object value) {
        Ehcache cache = ehcache.getEhcache(region);
        if (cache == null) {
            ehcache.addCache(region);
            cache = ehcache.getEhcache(region);
        }
        cache.put(new Element(key, value));
    }

}
