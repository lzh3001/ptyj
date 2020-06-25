package com.iccgame.sdk.common;

import android.util.Base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2015/10/28.
 */
public class ICC_AES {

    /**
     * 解码
     *
     * @param seed
     * @param encrypted
     * @return
     * @throws Exception
     */
    public static String decrypt(String seed, String encrypted) {
        try {
            byte[] bytes = Base64.decode(encrypted, Base64.DEFAULT);
            return new String(decrypt(seed.getBytes("UTF-8"), bytes), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解码
     *
     * @param seed
     * @param encrypted
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] seed, byte[] encrypted) throws Exception {
        byte[] raw = getRawKey(seed);
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(encrypted);
    }

    /**
     * 编码
     *
     * @param seed
     * @param cleartext
     * @return
     * @throws Exception
     */
    public static String encrypt(String seed, String cleartext) {
        try {
            byte[] bytes = encrypt(seed.getBytes("UTF-8"), cleartext.getBytes("UTF-8"));
            return new String(Base64.encode(bytes, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 编码
     *
     * @param seed
     * @param clear
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] seed, byte[] clear) throws Exception {
        byte[] raw = getRawKey(seed);
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(clear);
    }

    /**
     * @param seed
     * @return
     * @throws Exception
     */
    protected static byte[] getRawKey(byte[] seed) throws Exception {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        random.setSeed(seed);
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128, random);
        return generator.generateKey().getEncoded();
    }

// End Class
}
