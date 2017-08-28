package com.jetair.shopping.common.cache;

import org.nustaq.serialization.FSTConfiguration;

public class SerializationUtil {
    static FSTConfiguration configuration = FSTConfiguration.createStructConfiguration();

    public SerializationUtil() {
    }

    public static byte[] serializeByFST(Object obj) {
        return configuration.asByteArray(obj);
    }

    public static Object unserializeByFST(byte[] sec) {
        return configuration.asObject(sec);
    }
}
