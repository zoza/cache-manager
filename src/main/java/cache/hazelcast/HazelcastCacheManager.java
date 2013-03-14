/**
 * 
 */
package cache.hazelcast;

import cache.BaseCacheManager;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * @author zoza
 * 
 */
public class HazelcastCacheManager extends BaseCacheManager {
    private HazelcastInstance hazelcastInstance;

    public HazelcastInstance getHazelcast() {
        return hazelcastInstance;
    }

    public void init() throws Exception {                
        hazelcastInstance = Hazelcast.newHazelcastInstance();
    }

    public void shutdown() {
        Hazelcast.shutdownAll();
    }

    public Object get(String region, Object key) {
        return hazelcastInstance.getMap(region).get(key);
    }

    public Object get(Object key) {
        return get(DEFAULT_REGION, key);
    }

    public void delete(Object key) {
        delete(DEFAULT_REGION, key);

    }

    public void delete(String region, Object key) {
        hazelcastInstance.getMap(region).remove(key);

    }

    public void put(Object key, Object value) {
        put(DEFAULT_REGION, key, value);

    }

    public void put(String region, Object key, Object value) {
        hazelcastInstance.getMap(region).put(key, value);

    }

}
