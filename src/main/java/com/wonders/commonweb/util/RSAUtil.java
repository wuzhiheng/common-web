package com.wonders.commonweb.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import java.io.*;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author : wuzhiheng
 * @Description :
 * @Date Created in 下午5:20 2018/7/26
 */
public class RSAUtil {
    public static final String PUBLIC_KEY = "RSAPublicKey1";
    public static final String PRIVATE_KEY = "RSAPrivateKey";
    public static final String BASEPATH = "ds.properties";
    public static final java.security.Provider provider = new BouncyCastleProvider();
    /**
     * Constructor
     */
    public RSAUtil() throws Exception {
        java.security.Security.addProvider(provider);
    }

    /**
     * Generates the Keypair with the given keyLength.
     *
     * @param keyLength
     *            length of key
     * @return KeyPair object
     * @throws RuntimeException
     *             if the RSA algorithm not supported
     */
    public static KeyPair generateKeypair(int keyLength, String basePath) throws Exception {
        try {
            KeyPairGenerator kpg;
            try {
                kpg = KeyPairGenerator.getInstance("RSA");
            } catch (Exception e) {
                kpg = KeyPairGenerator.getInstance("RSA",provider);
            }
            kpg.initialize(keyLength);
            KeyPair keyPair = kpg.generateKeyPair();
            // saveKeyPair(keyPair, basePath);
            return keyPair;
        } catch (NoSuchAlgorithmException e1) {
            throw new RuntimeException("RSA algorithm not supported", e1);
        } catch (Exception e) {
            throw new Exception("other exceptions", e);
        }
    }
    /**
     * 保存密钥
     * @param kp
     * @throws Exception
     */
    public static void saveKeyPair(KeyPair kp, String basePath) throws Exception {
        basePath = StringUtils.isEmpty(basePath)?BASEPATH:basePath;
        File file = new File(basePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        String path = file.getPath();
        Properties prop = new Properties();// 属性集合对象
        InputStream fis = new BufferedInputStream(new FileInputStream(path));// 属性文件输入流
        prop.load(fis);// 将属性文件流装载到Properties对象中
        fis.close();// 关闭流
        // 修改公钥值的属性值
        prop.setProperty(PUBLIC_KEY,byteArrayToHexString(kp.getPublic().getEncoded()));
        // 修改私钥值的属性值
        prop.setProperty(PRIVATE_KEY,byteArrayToHexString(kp.getPrivate().getEncoded()));
        // 文件输出流
        BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(path));

        // 将Properties集合保存到流中
        prop.store(fos, "modify");
        fos.flush();
        fos.close();// 关闭流
        //System.out.println("获取修改后的属性值：password=" + prop.getProperty("password"));

    }
    /**
     * Decrypts a given string with the RSA keys
     *
     * @param encrypted
     *            full encrypted text
     * @param keys
     *            RSA keys
     * @return decrypted text
     * @throws RuntimeException
     *             if the RSA algorithm not supported or decrypt operation failed
     */
    public static String decrypt(String encrypted, KeyPair keys) throws Exception {
        Cipher dec;
        try {
            try {
                dec = Cipher.getInstance("RSA/NONE/NoPadding");
            } catch (Exception e) {
                dec = Cipher.getInstance("RSA/NONE/NoPadding",provider);
            }
            dec.init(Cipher.DECRYPT_MODE, keys.getPrivate());
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("RSA algorithm not supported", e);
        }
        String[] blocks = encrypted.split("\\s");
        StringBuffer result = new StringBuffer();
        try {
            for (int i = blocks.length - 1; i >= 0; i--) {
                byte[] data = hexStringToByteArray(blocks[i]);
                byte[] decryptedBlock = dec.doFinal(data);
                result.append(new String(decryptedBlock));
            }
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Decrypt error", e);
        }
        /**
         * Some code is getting added in first 2 digits with Jcryption need to investigate
         */
        return result.reverse().toString().substring(2);
    }

    /**
     * Parse url string (Todo - better parsing algorithm)
     *
     * @param url
     *            value to parse
     * @param encoding
     *            encoding value
     * @return Map with param name, value pairs
     */
    public static Map parse(String url, String encoding) {
        try {
            String urlToParse = URLDecoder.decode(url, encoding);
            String[] params = urlToParse.split("&");
            Map parsed = new HashMap();
            for (int i = 0; i < params.length; i++) {
                String[] p = params[i].split("=");
                String name = p[0];
                String value = (p.length == 2) ? p[1] : null;
                parsed.put(name, value);
            }
            return parsed;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unknown encoding.", e);
        }
    }

    /**
     * Return public RSA key modulus
     *
     * @param keyPair
     *            RSA keys
     * @return modulus value as hex string
     */
    public static String getPublicKeyModulus(KeyPair keyPair) {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return publicKey.getModulus().toString(16);
    }

    /**
     * Return public RSA key exponent
     *
     * @param keyPair
     *            RSA keys
     * @return public exponent value as hex string
     */
    public static String getPublicKeyExponent(KeyPair keyPair) {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return publicKey.getPublicExponent().toString(16);
    }

    /**
     * Max block size with given key length
     *
     * @param keyLength
     *            length of key
     * @return numeber of digits
     */
    public static int getMaxDigits(int keyLength) {
        return ((keyLength * 2) / 16) + 3;
    }

    /**
     * Convert byte array to hex string
     *
     * @param bytes
     *            input byte array
     * @return Hex string representation
     */
    public static String byteArrayToHexString(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            result.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return result.toString();
    }

    /**
     * Convert hex string to byte array
     *
     * @param data
     *            input string data
     * @return bytes
     */
    public static byte[] hexStringToByteArray(String data) {
        int k = 0;
        byte[] results = new byte[data.length() / 2];
        for (int i = 0; i < data.length();) {
            results[k] = (byte) (Character.digit(data.charAt(i++), 16) << 4);
            results[k] += (byte) (Character.digit(data.charAt(i++), 16));
            k++;
        }
        return results;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception{

		 KeyPair keyPair = generateKeypair(1024, "");
		 saveKeyPair(keyPair,null);

    }




}
