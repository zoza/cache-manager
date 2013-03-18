/**
 * 
 */
package concurrent;

import java.util.concurrent.CountDownLatch;

import cache.CacheManager;

/**
 * @author zoza
 * 
 */
public class Task implements Runnable {
    String name;
    CountDownLatch latch;
    CacheManager cm;
    String randomStringKey;
    public Task(String name, CountDownLatch cdl, CacheManager cm,String randomStringKey) {
        this.name = name;
        latch = cdl;
        this.cm = cm;
        this.randomStringKey= randomStringKey;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
       // System.out.println("task: " + name + " " + System.currentTimeMillis());
        try {

           // String randomStringKey = UUID.randomUUID().toString();

            cm.incrementAndGet(randomStringKey);
            cm.incrementAndGet(randomStringKey);
            Long long1 = cm.incrementAndGet(randomStringKey);
           // Assert.assertEquals(Long.valueOf(3), long1);

        } catch (Exception e) {

            e.printStackTrace();
        }
        latch.countDown();
    }

}
