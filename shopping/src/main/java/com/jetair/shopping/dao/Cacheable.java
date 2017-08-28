package com.jetair.shopping.dao;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jetair.shopping.common.exception.KeyIncompleteException;

/**
 * @Title: Cacheable.java
 * @Package com.jetair.pip.shopping.model
 * @author gymeng
 * @date 2017年4月11日 下午5:38:51
 * @version V1.0
 */
public abstract class Cacheable<K> implements Serializable {

    private static final long serialVersionUID = -1397564761447497919L;

    public abstract K genKey() throws KeyIncompleteException;

    public abstract boolean keyMembers();

    @JsonProperty("exp")
    private long expireSecond = -1;

    public long getExpireSecond() {
        return expireSecond;
    }

    public void setExpireSecond(long expireSecond) {
        this.expireSecond = expireSecond;
    }

    protected static String stringKeyGenerator(Object... items) throws KeyIncompleteException {
        StringBuilder  sb = new StringBuilder ();
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                throw new KeyIncompleteException();
            }
            if (i != 0 ) {
                sb.append(":");
            }
            sb.append(items[i].toString());
        }
        return sb.toString();

    }
    protected static String stringKeyPrice(Object... items) throws KeyIncompleteException {
        StringBuilder  sb = new StringBuilder ();
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                throw new KeyIncompleteException();
            }
            if (i != 0 && i!=items.length-1 ) {
                sb.append(":");
            }
            sb.append(items[i].toString());
        }
        return sb.toString();

    }
}
