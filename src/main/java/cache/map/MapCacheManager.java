/**
 * 
 */
package cache.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cache.BaseCacheManager;

/**
 * 
 * ONLY for DEV\TEST use
 * !!! its cleared between JVM restarts !!!
 * @author zoza
 * 
 */
public class MapCacheManager extends BaseCacheManager {
    Map<Object, Map<Object, Object>> regions = new ConcurrentHashMap<Object, Map<Object, Object>>();

    public void init() throws Exception {
        regions.put(DEFAULT_REGION, new ConcurrentHashMap<Object, Object>());
    }

    public void shutdown() {

    }

    public Object get(Object key) {
        return get(DEFAULT_REGION, key);
    }

    public Object get(String region, Object key) {
        Map<Object, Object> regionMap = regions.get(region);
        if (regionMap != null) {
            return regionMap.get(key);
        } else {
            return regions.get(DEFAULT_REGION).get(key);
        }

    }

    public void delete(Object key) {
        // TODO Auto-generated method stub

    }

    public void delete(String region, Object key) {
        // TODO Auto-generated method stub

    }

    public void put(Object key, Object value) {
        // TODO Auto-generated method stub

    }

    public void put(String region, Object key, Object value) {
        // TODO Auto-generated method stub

    }

}
