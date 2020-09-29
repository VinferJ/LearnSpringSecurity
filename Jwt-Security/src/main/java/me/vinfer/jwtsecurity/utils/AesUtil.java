package me.vinfer.jwtsecurity.utils;


import me.vinfer.jwtsecurity.exception.CryptException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Vinfer
 * @date 2020-09-20    20:24
 * @description aes加密解密工具类
 **/
public class AesUtil {

    public static final String ENCRYPT_MODE = "AES/CBC/PKCS5Padding";
    public static final String ENCRYPT_TYPE = "AES";

    public static String encrypt(String originText,String key,String iv){
        try {
            //获取cipher实例
            Cipher cipher = Cipher.getInstance(ENCRYPT_MODE);
            byte[] raw = key.getBytes();
            SecretKeySpec keySpec = new SecretKeySpec(raw, ENCRYPT_TYPE);
            //使用cbc模式时，需要一个向量iv，可以增加加密算法的强度
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            //初始化cipher，使用加密模式的cipher
            cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivParameterSpec);
            byte[] encryptedText = cipher.doFinal(originText.getBytes(StandardCharsets.UTF_8));
            //使用base64转码后再返回
            return new String(Base64.getEncoder().encode(encryptedText),StandardCharsets.UTF_8);
        }catch (Exception e){
            throw new CryptException(e.getMessage());
        }
    }

    public static String decrypt(String cipherText,String key,String iv){
        try {
            byte[] raw = key.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
            //获取cipher实例，入参要与加密时使用的加密方式一致
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            //根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示解密模式
            cipher.init(Cipher.DECRYPT_MODE, keySpec,ivParameterSpec);
            //先进行base64解码后再进行密文解密
            byte[] originText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return new String(originText,StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CryptException(e.getMessage());
        }
    }

}
