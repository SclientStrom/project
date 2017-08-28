package com.jetair.shopping.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.jetair.shopping.dao.BaseDao;
import com.jetair.shopping.dao.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @Title: BaseDaoImpl.java
 * @Package com.jetair.pip.shopping.dao.impl
 * @author gymeng
 * @date 2017年4月11日 下午6:52:04
 * @version V1.0
 */

public abstract class RedisBaseDaoImpl<K, V extends Cacheable<K>> extends RedisBase<K, V> implements BaseDao<K, V> {

	@Override
	public List<V> findAll(String key) {
		List<V> results = null;
		try {
			final RedisTemplate<K, V> rt = this.redisTemplate;
			results = this.redisTemplate.execute(new RedisCallback<List<V>>() {
				public List<V> doInRedis(RedisConnection connection) throws DataAccessException {
					final List<V> rs = new ArrayList<>();
					Set<byte[]> keys = connection.keys(key.getBytes());
					if (keys != null) {
						ValueOperations<K, V> vo = rt.opsForValue();
						keys.forEach(kk -> rs.add((V) vo.get(new String(kk))));
					}
					return rs;
				}
			});
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return results;
	}
	public Set<V> findAllSet(String key) {
		try {
			final RedisTemplate<K, V> rt = this.redisTemplate;
			return  this.redisTemplate.execute(new RedisCallback<Set<V>>() {
				public Set<V> doInRedis(RedisConnection connection) throws DataAccessException {
					Set<byte[]> keys = connection.keys(key.getBytes());
					Set<V> results = new HashSet<>();
					if (keys != null) {
						SetOperations<K, V> vo = rt.opsForSet();
						keys.forEach(kk -> {
							results.addAll(vo.members((K) new String(kk)));
						});
					}
					return results;
				}
			});
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return null;
	}
	
	@Override
	public V find(K key) {
		return this.getValue(key);
	}

	public Long evict(K key) {
		final String keyf = key.toString();
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.del(keyf.getBytes());
			}
		});
	}

	protected Long removeAll(String key) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				long count = 0;
				Set<byte[]> keys = connection.keys(key.getBytes());
				for (byte[] bs : keys) {
					connection.del(bs);
					count++;
				}
				return count;
			}
		});
	}

	protected Set<byte[]> findKeys(String key) {
		return redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
			public Set<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.keys(key.getBytes());

			}
		});
	}

	@Override
	public int saveAll(List<V> all) {
		int count = 0;
		for (V v : all) {
			count += this.save(v);
		}

		return count;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int save(V v) {
		try {
			if (v.keyMembers()) {
				this.setOperations.add(v.genKey(), v);
			} else {
				this.set(v.genKey(), v, v.getExpireSecond());
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return -1;
		}
		return 1;
	}
}
