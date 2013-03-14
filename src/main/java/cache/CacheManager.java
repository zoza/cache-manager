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

    public void clearAll() throws OperationNotSupportedException;

    public void clearRegion(String region)
            throws OperationNotSupportedException;

}
