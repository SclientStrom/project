package com.jetair.shopping.dao;

import java.util.List;
import java.util.Set;

public interface BaseDao<K,V extends Cacheable<K>> {

    public int save(V v);

    public List<V> findAll(String key);

    public Set<V> findAllSet(String key);

    public V find(K key);

    public Long evict(K key);

    public int saveAll(List<V>all);
}
