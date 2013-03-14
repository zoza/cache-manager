import java.util.UUID;

import javax.naming.OperationNotSupportedException;

import junit.framework.Assert;

import org.junit.Test;

import cache.BaseCacheManager;
import cache.CacheManager;
import cache.memcache.MemcacheCacheManager;

/**
 * 
 */

/**
 * @author zoza
 * 
 */
public abstract class BaseTestCache {
    public String getRandomString() {
        return UUID.randomUUID().toString();
    }

    @Test
    public void replacePositive() {
        String randomStringKey = getRandomString();
        String randomStringValue = getRandomString();
        String randomStringValue2 = getRandomString();
        getCacheManager().put(randomStringKey, randomStringValue);

        Boolean result = getCacheManager().replace(randomStringKey,
                randomStringValue2);
        Assert.assertEquals(Boolean.TRUE, result);
        String value = (String) getCacheManager().get(randomStringKey);
        Assert.assertEquals(randomStringValue2, value);
    }

    protected abstract CacheManager getCacheManager();

    @Test
    public void replaceNegative() {
        String randomStringKey = getRandomString();
        String randomStringValue = getRandomString();

        Boolean result = getCacheManager().replace(randomStringKey,
                randomStringValue);
        Assert.assertEquals(Boolean.FALSE, result);
        String value = (String) getCacheManager().get(randomStringKey);
        Assert.assertEquals(null, value);
    }

    @Test
    public void putIfAbsentPositive() {
        String randomStringKey = getRandomString();
        String randomStringValue = getRandomString();

        Boolean result = getCacheManager().putIfAbsent(randomStringKey,
                randomStringValue);
        Assert.assertEquals(Boolean.TRUE, result);

        String value = (String) getCacheManager().get(randomStringKey);
        Assert.assertEquals(randomStringValue, value);
    }

    @Test
    public void putIfAbsentNegative() {
        String randomStringKey = getRandomString();
        String randomStringValue = getRandomString();
        String randomStringValue2 = getRandomString();
        getCacheManager().put(randomStringKey, randomStringValue);

        Boolean result = getCacheManager().putIfAbsent(randomStringKey,
                randomStringValue2);
        Assert.assertEquals(Boolean.FALSE, result);

        String value = (String) getCacheManager().get(randomStringKey);
        Assert.assertEquals(randomStringValue, value);
    }

    @Test(expected=NullPointerException.class)
    public void regionNull() {
        String randomStringKey = getRandomString();
        String randomStringValue = getRandomString();

        getCacheManager().put(null, randomStringKey, randomStringValue);
        String value = (String) getCacheManager().get(null, randomStringKey);
        Assert.assertEquals(randomStringValue, value);
    }

    @Test
    public void differentRegions() {
        String randomStringKey = getRandomString();
        String randomRegion = getRandomString();
        String randomStringValue = getRandomString();

        getCacheManager().put(randomRegion, randomStringKey, randomStringValue);
        String value = (String) getCacheManager().get(randomRegion + "2",
                randomStringKey);
        Assert.assertEquals(null, value);
    }

    @Test
    public void sameCustomRegions() {
        String randomStringKey = getRandomString();
        String randomRegion = getRandomString();
        String randomStringValue = getRandomString();

        getCacheManager().put(randomRegion, randomStringKey, randomStringValue);
        String value = (String) getCacheManager().get(randomRegion,
                randomStringKey);
        Assert.assertEquals(randomStringValue, value);
    }

    @Test
    public void sameCustomRegionsAndDefault() {
        String randomStringKey = getRandomString();
        String randomRegion = getRandomString();
        String randomStringValue = getRandomString();

        getCacheManager().put(randomRegion, randomStringKey, randomStringValue);
        String value = (String) getCacheManager().get(randomRegion,
                randomStringKey);
        Assert.assertEquals(randomStringValue, value);
        value = (String) getCacheManager().get(randomStringKey);
        Assert.assertEquals(null, value);
    }

    @Test
    public void clearCustomRegion() throws OperationNotSupportedException {
        String randomStringKey = getRandomString();
        String randomRegion = getRandomString();
        String randomStringValue = getRandomString();

        getCacheManager().put(randomStringKey, randomStringValue);

        getCacheManager().put(randomRegion, randomStringKey, randomStringValue);
        String value = (String) getCacheManager().get(randomRegion,
                randomStringKey);
        Assert.assertEquals(randomStringValue, value);

        getCacheManager().clearRegion(randomRegion);

        value = (String) getCacheManager().get(randomRegion, randomStringKey);
        Assert.assertEquals(null, value);
        // default region is still here
        value = (String) getCacheManager().get(randomStringKey);
        Assert.assertEquals(randomStringValue, value);
    }

    @Test
    public void clearDefaultRegion() throws OperationNotSupportedException {
        String randomStringKey = getRandomString();
        String randomRegion = getRandomString();
        String randomStringValue = getRandomString();

        getCacheManager().put(randomRegion, randomStringKey, randomStringValue);

        getCacheManager().put(randomStringKey, randomStringValue);

        getCacheManager().clearRegion(BaseCacheManager.DEFAULT_REGION);

        // custom region is still here
        String value = (String) getCacheManager().get(randomRegion,
                randomStringKey);
        Assert.assertEquals(randomStringValue, value);

        value = (String) getCacheManager().get(randomStringKey);
        Assert.assertEquals(null, value);

    }

    @Test
    public void clearAll() throws OperationNotSupportedException {
        String randomStringKey = getRandomString();
        String randomRegion = getRandomString();
        String randomStringValue = getRandomString();

        getCacheManager().put(randomRegion, randomStringKey, randomStringValue);
        getCacheManager().put(randomStringKey, randomStringValue);

        getCacheManager().clearAll();

        String value = (String) getCacheManager().get(randomRegion,
                randomStringKey);
        Assert.assertEquals(null, value);

        value = (String) getCacheManager().get(randomStringKey);
        Assert.assertEquals(null, value);
    }

    // @Test
    public void testInit() throws Exception {
        CacheManager c = new MemcacheCacheManager();
        c.init();
        long start = System.currentTimeMillis();
        System.out.println("start:" + start);
        for (int i = 0; i < 1000; i++) {
            c.put("cust", "test" + i, "zoza2dfsgfhfjhfg" + i);
            Object object = c.get("cust", "test" + i);
            // System.out.println(object);
        }
        long end = System.currentTimeMillis();
        System.out.println("end:" + end + ". took: " + (end - start));
        c.shutdown();
    }
}
