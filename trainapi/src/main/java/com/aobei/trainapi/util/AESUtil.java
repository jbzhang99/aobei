package com.aobei.trainapi.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";
   

    /**
     * AES加
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptData(String data,String key) throws Exception {
        Base64 base64 = new Base64();
        // 创建密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        // 初始化
        cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
        return new String(base64.encodeToString(cipher.doFinal(data.getBytes())));
    }

    /**
     * AES解码
     * @param base64Data
     * @return
     * @throws Exception
     */
    public static String decryptData(String base64Data,String key) throws Exception {
        Base64 base64 = new Base64();
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, getKey(key));
        return new String(cipher.doFinal(base64.decodeBase64(base64Data)));
    }
    /**
     * 生成key
     */

    private static SecretKeySpec getKey(String key){
        return  new SecretKeySpec(DigestUtils.md5Hex(key).toLowerCase().getBytes(), ALGORITHM);
    }

}