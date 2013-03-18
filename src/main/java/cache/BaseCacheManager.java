/**
 * 
 */
package cache;

/**
 * @author zoza
 * 
 */
public abstract class BaseCacheManager implements CacheManager {
    public static final String DELTA_MUST_BE_POSITIVE = "delta must be positive";
    public static final String REGION_CANT_BE_NULL = "region can't be null";
    public static final String KEY_SHOULDT_BE_NULL = "key should't be null";
    public static final String VALUE_SHOULDT_BE_NULL = "key and value should't be null";
    public static final String DEFAULT_REGION = "defaultRegion";

    public BaseCacheManager() {

    }

    public Object get(Object key) {
        return get(DEFAULT_REGION, key);
    }

    public void put(Object key, Object value) {
        put(DEFAULT_REGION, key, value);
    }

    public void delete(Object key) {
        delete(DEFAULT_REGION, key);
    }

    public Boolean replace(Object key, Object value) {
        return replace(DEFAULT_REGION, key, value);
    }

    public Boolean putIfAbsent(Object key, Object value) {
        return putIfAbsent(DEFAULT_REGION, key, value);
    }

    public Long incrementAndGet(String region, Object key) {
        return incrementAndGet(region, key, 1L);
    }

    public Long incrementAndGet(Object key) {
        return incrementAndGet(DEFAULT_REGION, key);
    }

    public Long incrementAndGet(String region, Object key, long delta) {
        return atomicAndGet(region, key, delta);
    }

    public Long incrementAndGet(Object key, long delta) {
        return incrementAndGet(DEFAULT_REGION, key, delta);
    }

    // public Long decrementAndGet(String region, Object key) {
    // return decrementAndGet(region, key, 1L);
    // }
    //
    // public Long decrementAndGet(Object key) {
    // return decrementAndGet(DEFAULT_REGION, key);
    // }
    //
    // public Long decrementAndGet(String region, Object key, long delta) {
    // return atomicAndGet(region, key, delta, false);
    // }
    //
    // public Long decrementAndGet(Object key, long delta) {
    // return decrementAndGet(DEFAULT_REGION, key, delta);
    // }

    protected abstract Long atomicAndGet(String region, Object key, long delta);
}
