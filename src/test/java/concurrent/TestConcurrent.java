/**
 * 
 */
package concurrent;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cache.CacheManager;
import cache.memcache.MemcacheCacheManager;

/**
 * @author zoza
 * 
 */
public class TestConcurrent {
   
    //100 threads
    // HZ 1300-2500
    //mem 2800-3100
    //eh 9200- 29000
    
    
    //10 threads
    // HZ 1300-2500
    //mem 3600-3800
    //eh 9000-37000
    public void testSyncronize() throws Exception {
        int count = 100000;
        ExecutorService tp = Executors.newFixedThreadPool(100);
        CacheManager cm = new MemcacheCacheManager();
        cm.init();
        CountDownLatch cdl = new CountDownLatch(count);
        long start = System.currentTimeMillis();
       
       
        for (int i = 0; i < count; i++) {
            String randomStringKey = UUID.randomUUID().toString();
            Task task = new Task(String.valueOf(i), cdl, cm,randomStringKey);
            tp.execute(task);
        }

        cdl.await();
        System.out.println("time:(millis) "
                + (System.currentTimeMillis() - start));

    }
}
