package lrucache;

import main.java.lrucache.LRUCache;
import main.java.lrucache.singlethread.SingleThreadLRUCache;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class SingleThreadTest {

    @Test
    public void fillCacheFully_WhenGetEntities_ThenTheyAreConsistentWithPutOnes() {
        LRUCache<String, String> lruCache = new SingleThreadLRUCache<>(3);
        lruCache.put("1", "test1");
        lruCache.put("2", "test2");
        lruCache.put("3", "test3");
        assertEquals("test1", lruCache.get("1").get());
        assertEquals("test2", lruCache.get("2").get());
        assertEquals("test3", lruCache.get("3").get());
    }

    @Test
    public void overfillCache_WhenPutOneMoreEntity_ThenTheOldestEntityDisappears() {
        LRUCache<String, String> lruCache = new SingleThreadLRUCache<>(3);
        lruCache.put("1", "test1");
        lruCache.put("2", "test2");
        lruCache.put("3", "test3");
        lruCache.put("4", "test4");
        assertFalse(lruCache.get("1").isPresent());
    }

    @Test
    public void runMultiThreadTask_WhenPutEntitiesInSingleThreadCache_ThenSomeEntitiesAreLost() {
        final int size = 500_000;
        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        LRUCache<Integer, String> lruCache = new SingleThreadLRUCache<>(size);
        CountDownLatch countDownLatch = new CountDownLatch(size);
        try {
            IntStream.range(0, size).<Runnable>mapToObj(key -> () -> {
                lruCache.put(key, "value" + key);
                countDownLatch.countDown();
            }).forEach(executorService::submit);
            countDownLatch.await();
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        } finally {
            executorService.shutdown();
        }
        System.out.println(lruCache.getListSize() + " < " + size);
        assertNotEquals(lruCache.getListSize(), size);
    }
}
