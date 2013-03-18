/**
 * 
 */
package cache.hazelcast;

import javax.naming.OperationNotSupportedException;

import cache.BaseCacheManager;

import com.hazelcast.core.AtomicNumber;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

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
        return getHazelcastMapByRegion(region).get(key);
    }

    @Override
    public Object get(Object key) {
        return get(DEFAULT_REGION, key);
    }

    @Override
    public void delete(Object key) {
        delete(DEFAULT_REGION, key);

    }

    public void delete(String region, Object key) {
        getHazelcastMapByRegion(region).remove(key);

    }

    @Override
    public void put(Object key, Object value) {
        put(DEFAULT_REGION, key, value);

    }

    public void put(String region, Object key, Object value) {
        getHazelcastMapByRegion(region).put(key, value);

    }

    @Override
    public Boolean replace(Object key, Object value) {
        return replace(DEFAULT_REGION, key, value);
    }

    public Boolean replace(String region, Object key, Object value) {
        Object replace = getHazelcastMapByRegion(region).replace(key, value);
        if (replace == null) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean putIfAbsent(Object key, Object value) {
        return putIfAbsent(DEFAULT_REGION, key, value);
    }

    public Boolean putIfAbsent(String region, Object key, Object value) {
        Object result = getHazelcastMapByRegion(region).putIfAbsent(key, value);
        if (result == null) {
            return true;
        }
        return false;
    }

    public void clearAll() throws OperationNotSupportedException {
        throw new OperationNotSupportedException(
                "clearAll not supported for HZ");

    }

    public void clearRegion(String region) {
        getHazelcastMapByRegion(region).clear();
    }

    private IMap<Object, Object> getHazelcastMapByRegion(String region) {
        if (region == null) {
            throw new NullPointerException(REGION_CANT_BE_NULL);
        }
        return hazelcastInstance.getMap(region);
    }

    @Override
    protected Long atomicAndGet(String region, Object key, long delta) {
        if (region == null) {
            throw new NullPointerException(REGION_CANT_BE_NULL);
        }
        if (delta <= 0) {
            throw new IllegalArgumentException(DELTA_MUST_BE_POSITIVE);
        }
        if (key == null) {
            throw new NullPointerException(KEY_SHOULDT_BE_NULL);
        }
        AtomicNumber atomicNumber = hazelcastInstance.getAtomicNumber(region
                + "." + key);
        return atomicNumber.addAndGet(delta);
    }

}
