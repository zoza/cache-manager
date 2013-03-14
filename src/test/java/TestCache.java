import org.junit.Test;

import cache.CacheManager;
import cache.hazelcast.HazelcastCacheManager;

public class TestCache {
    @Test
    public void testInit() throws Exception {
        CacheManager c =  new HazelcastCacheManager();
        c.init();
        long start = System.currentTimeMillis();
        System.out.println("start:"+start);
        for (int i = 0; i < 1000; i++) {
            c.put("cust", "test"+i, "zoza2dfsgfhfjhfg"+i);
            Object object = c.get("cust", "test"+i);
          //  System.out.println(object);
        }
        long end = System.currentTimeMillis();
        System.out.println("end:"+end+". took: "+(end-start));
        c.shutdown();
    }
}
