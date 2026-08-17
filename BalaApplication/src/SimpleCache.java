import java.util.concurrent.*;

public class SimpleCache<K, V> {

	private ConcurrentHashMap<K, CacheValue<V>> cacheMap = new ConcurrentHashMap<>();
	
	public static void main(String[] args) throws InterruptedException {
		SimpleCache<String, String> simpleCache = new SimpleCache<>();
		simpleCache.putValue("Bala", "57", 2);
		System.out.println("Immediate fetch from cache is " + simpleCache.getValueFromCache("Bala"));
		System.out.println("Waiting for a few seconds for the cache to expire");
		Thread.sleep(3000);
		System.out.println("Fetch again from cache is " + simpleCache.getValueFromCache("Bala"));
	}
	
	public void putValue(K key, V value, long milliSeconds) {
		cacheMap.put(key, new CacheValue<>(value, milliSeconds));
	}
	
	public V getValueFromCache(K key) {
		CacheValue<V> cachedItem = cacheMap.get(key);
		if (cachedItem == null ) { return null;}
		if (cachedItem.isExpired()) { 
			cacheMap.remove(key);
			return null; }
		return cachedItem.getValue();
	}
	
	public void clearSpecificCache(K key) {
		cacheMap.remove(key);
	}
	
	public void clearCache() {
		cacheMap.clear();
	}

}

class CacheValue<V> {
	private final V value;
	private final long milliSeconds;
	
	public CacheValue(V value, long milliSeconds) {
		this.value = value;
		this.milliSeconds = System.currentTimeMillis() + (milliSeconds * 1000L);
	}
	
	public boolean isExpired() {
        return System.currentTimeMillis() > this.milliSeconds;
    }

    public V getValue() {
        return value;
    }
	
}