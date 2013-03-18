/**
 * 
 */
package cache;

import javax.naming.OperationNotSupportedException;

/**
 * @author zoza
 * 
 */
public interface CacheManager {

    public void init() throws Exception;

    public void shutdown();

    public Object get(Object key);

    public Object get(String region, Object key);

    public Boolean replace(Object key, Object value);

    public Boolean replace(String region, Object key, Object value);

    public void delete(Object key);

    public void delete(String region, Object key);

    public void put(Object key, Object value);

    public void put(String region, Object key, Object value);

    public Boolean putIfAbsent(Object key, Object value);

    public Boolean putIfAbsent(String region, Object key, Object value);

    /**
     * <b>Clear all data</b></br> Hazelcast back-end is not support this feature
     * so OperationNotSupportedException throw
     * 
     * @throws OperationNotSupportedException
     */
    public void clearAll() throws OperationNotSupportedException;

    /**
     * <b>clear region data</b></br> Memcache back-end is not support this
     * feature so OperationNotSupportedException throw
     * 
     * @param region
     * @throws OperationNotSupportedException
     */
    public void clearRegion(String region)
            throws OperationNotSupportedException;

    public Long incrementAndGet(String region, Object key);

    public Long incrementAndGet(Object key);

    public Long incrementAndGet(String region, Object key, long delta);

    public Long incrementAndGet(Object key, long delta);

//    public Long decrementAndGet(String region, Object key);
//
//    public Long decrementAndGet(Object key);
//
//    public Long decrementAndGet(String region, Object key, long delta);
//
//    public Long decrementAndGet(Object key, long delta);

}
