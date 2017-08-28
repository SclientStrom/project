package com.jetair.shopping.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.Callable;

public class RedisCache implements Cache {
    protected final Logger logger = LoggerFactory.getLogger(RedisCache.class);
    private RedisTemplate<String, Object> redisTemplate;
    private String name;
    Long expireSecond = Long.valueOf(86400L);

    public RedisCache() {
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return this.redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getExpireSecond() {
        return this.expireSecond;
    }

    public void setExpireSecond(Long expireSecond) {
        this.expireSecond = expireSecond;
    }

    public String getName() {
        return this.name;
    }

    public Object getNativeCache() {
        return this.redisTemplate;
    }

    public ValueWrapper get(Object key) {
        final String keyf = this.getName() + ":" + KeyGenerator.toString(key);
        Object object = null;

        try {
            object = this.redisTemplate.execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] key = keyf.getBytes();
                    byte[] value = connection.get(key);
                    RedisCache.this.logger.debug("redis cache get ==> {}", keyf);
                    return value == null?null:SerializationUtil.unserializeByFST(value);
                }
            });
        } catch (Exception var5) {
            this.logger.error(var5.getMessage(), var5);
        }

        if(object != null) {
            this.logger.debug("redis cache get ==> {} : {}", keyf, object);
            return new SimpleValueWrapper(object);
        } else {
            return null;
        }
    }

    public void put(Object key, final Object value) {
        try {
            final String keyf = this.getName() + ":" + KeyGenerator.toString(key);
            this.redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] keyb = keyf.getBytes();
                    byte[] valueb = SerializationUtil.serializeByFST(value);
                    connection.set(keyb, valueb);
                    boolean result = false;
                    if(RedisCache.this.expireSecond.longValue() > 0L) {
                        result = connection.expire(keyb, RedisCache.this.expireSecond.longValue()).booleanValue();
                    }

                    RedisCache.this.logger.debug("redis cache put:", keyf);
                    return Long.valueOf(result?1L:-1L);
                }
            });
        } catch (Exception var5) {
            this.logger.error(var5.getMessage(), var5);
        }

    }

    public void evict(Object key) {
        try {
            final String keyf = this.getName() + ":" + KeyGenerator.toString(key);
            this.redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    RedisCache.this.logger.debug("redis cache evict ==> {}", keyf);
                    return connection.del(new byte[][]{keyf.getBytes()});
                }
            });
        } catch (Exception var3) {
            this.logger.error(var3.getMessage(), var3);
        }

    }

    public void clear() {
        try {
            this.redisTemplate.execute(new RedisCallback<String>() {
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.flushDb();
                    return "ok";
                }
            });
        } catch (Exception var2) {
            this.logger.error(var2.getMessage(), var2);
        }

    }

    public <T> T get(Object key, Class<T> arg1) {
        return (T)(this.get(key).get());
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return (T)this.get(key).get();
    }

    public ValueWrapper putIfAbsent(Object key, Object value) {
        this.put(key, value);
        return new SimpleValueWrapper(value);
    }
}
