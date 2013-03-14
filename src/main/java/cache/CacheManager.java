/**
 * 
 */
package cache;

/**
 * @author zoza
 * 
 */
public interface CacheManager {

    public void init() throws Exception;

    public void shutdown();

    public Object get(Object key);

    public Object get(String region, Object key);

    public void delete(Object key);

    public void delete(String region, Object key);

    public void put(Object key, Object value);

    public void put(String region, Object key, Object value);

    // public Object putIfAbsent(Object key , Object value);
    // public Object putIfAbsent(String region,Object key , Object value);

}
