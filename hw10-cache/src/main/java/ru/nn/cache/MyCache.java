package ru.nn.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private final List<HwListener<K, V>> listeners;
    private final Map<K, V> cache;

    public MyCache() {
        listeners = new ArrayList<>();
        cache = new WeakHashMap<>();
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notify(key, value, "put");
    }

    @Override
    public void remove(K key) {
        V value = cache.get(key);
        cache.remove(key);
        notify(key, value, "remove");
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    private void notify(K key, V value, String action) {
        for (var listener : listeners) {
            listener.notify(key, value, action);
        }
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
