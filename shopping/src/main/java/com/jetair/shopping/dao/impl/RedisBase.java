package com.jetair.shopping.dao.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @Title: RedisBase.java
 * @Package com.jetair.pip.shopping.dao.impl
 * @author gymeng
 * @date 2017年4月20日 上午10:37:50
 * @version V1.0
 */
public abstract class RedisBase<K, V> {
	protected static final Logger logger = LoggerFactory.getLogger(RedisBaseDaoImpl.class);
	// 实际参数的class start
	protected Class<K> kClass;

	protected Class<V> vClass;

	@Resource(name = "jedisTemplate")
	protected RedisTemplate<K, V> redisTemplate;

	protected ValueOperations<K, V> valueOperations;
	//
	protected HashOperations<K, String, V> hashOperations;
	//
	protected ListOperations<K, V> listOperations;

	protected SetOperations<K, V> setOperations;

	/**
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	protected void set(K key, V value, long expire) {
		if (expire <= 0) {
			valueOperations.set(key, value);
		} else {
			valueOperations.set(key, value, expire, TimeUnit.SECONDS);
		}
	}

	/**
	 * get value
	 * 
	 * @param key
	 * @return
	 */
	protected V getValue(K key) {
		return valueOperations.get(key);
	}

	/**
	 * key delete
	 * 
	 * @param key
	 */
	protected void delete(K key) {
		getRedisTemplate().delete(key);
	}

	/**
	 * key exist
	 * 
	 * @param key
	 * @return
	 */
	protected boolean hasKey(K key) {
		return getRedisTemplate().hasKey(key);
	}

	/**
	 * key expire
	 * 
	 * @param key
	 * @param timeout
	 * @param unit
	 * @return
	 */
	protected Boolean expire(K key, long timeout, TimeUnit unit) {
		return getRedisTemplate().expire(key, timeout, unit);
	}

	/**
	 * redistemplate是全局唯一的，子类不要出现对redistemplate的成员变量的设置(比如keyserializer,)
	 * 
	 * @return
	 */
	RedisTemplate<K, V> getRedisTemplate() {
		return redisTemplate;
	}

	/**
	 * 当需要更改serializer,可以直接通过connection.set等方法实现
	 * 
	 * @param callback
	 * @return
	 */
	protected <T> T execute(RedisCallback<T> callback) {
		return redisTemplate.execute(callback);
	}

	/**
	 * 获取JdkSerializationRedisSerializer
	 */
	@SuppressWarnings("unchecked")
	protected <T> RedisSerializer<T> getDefaultSerializer() {
		return (RedisSerializer<T>) redisTemplate.getDefaultSerializer();
	}

	/**
	 * 获取jackson2jsonredisserializer
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected RedisSerializer<V> getValueSerializer() {
		return (RedisSerializer<V>) redisTemplate.getValueSerializer();
	}

	/**
	 * 获取jackson2jsonredisserializer
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected RedisSerializer<K> getHashKeySerializer() {
		return (RedisSerializer<K>) redisTemplate.getHashKeySerializer();
	}

	/**
	 * UnifiedBusiness，opentravel:opentravel，pss:pss)

	 N

	 * 获取jackson2jsonredisserializer
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected RedisSerializer<V> getHashValueSerializer() {
		return (RedisSerializer<V>) redisTemplate.getHashValueSerializer();
	}

	public void init(Class<K> kClass, Class<V> vClass) throws Exception {
		
		this.kClass = kClass;
		this.vClass = vClass;
		valueOperations = redisTemplate.opsForValue();
		hashOperations = redisTemplate.opsForHash();
		listOperations = redisTemplate.opsForList();
		setOperations = redisTemplate.opsForSet();
		//
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<V>(this.vClass));
		redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer<String>(String.class));
		redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<V>(this.vClass));
	}
}
