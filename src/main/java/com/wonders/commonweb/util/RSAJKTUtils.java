package com.wonders.commonweb.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSAJKTUtils {

    private static String PUBLIC_KEY_STR  = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsuBgQtBXy3qeLnvPUhYjL3/CFSKK93wUERJELrzuglvmCD5KQaOuV8Acs4VY7S0GaJq3JT+BXGgVhLbHUHdpqJzTnIHuxDiGjyDTGKtMoiY3tGiNquQlg1GBBW58dRd/9Jf3mOnFPGpTOOIm5hGpAn6Rf97/7THS1xuaMCdkoCYpXHvaFboIsGbRIzMmI+P2TjsRUElBEvgaV4HZ+v5o/aslDqxYMgYIm+rRWx+Kku60h04WNh2ZKGfRV44hVT8IizE5e/2QvJu+nRyv3k+Ix5z5WzwIiYaa3cEkRBXXLBhNWLJwuu567XmD43LIDwXUMUv4rrNgyAHRkFvQ+eYfhwIDAQAB";
    private static String PRIVATE_KEY_STR = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCy4GBC0FfLep4ue89SFiMvf8IVIor3fBQREkQuvO6CW+YIPkpBo65XwByzhVjtLQZomrclP4FcaBWEtsdQd2monNOcge7EOIaPINMYq0yiJje0aI2q5CWDUYEFbnx1F3/0l/eY6cU8alM44ibmEakCfpF/3v/tMdLXG5owJ2SgJilce9oVugiwZtEjMyYj4/ZOOxFQSUES+BpXgdn6/mj9qyUOrFgyBgib6tFbH4qS7rSHThY2HZkoZ9FXjiFVPwiLMTl7/ZC8m76dHK/eT4jHnPlbPAiJhprdwSREFdcsGE1YsnC67nrteYPjcsgPBdQxS/ius2DIAdGQW9D55h+HAgMBAAECggEBAJnyN4ZpLpYlwot47NXVzZRsnMl5wCX8uHx0vw+GdLLmipQfn+LcDwjggxMRuZXuASz3spWxERFJVvOwJtue2eVOi2SQAsEHcO8vrd32V27Z+2kd7obb8VkSRTs6eox/nBUS0Pnef2xeiZ9UK2woWM+XxHtLnfEyxyVwUQQOczDMFGAH8Crb3po8/bi2mPH982xFAm9WEYcgWlceYixBd37eucc3yB1p4DqoeMaMBUY9aeu6d6BVotIi3oCphqW+Neuz9hDUpJgb+lveXzTwuqRfrPjwrkkyEXoA/ru3fEkSA481Cx8N995OQUFg2npzpzV/BnbLCv+xbSXF1GAGJWECgYEA7+I9AAoI6Ls1ZXsRIDby6GB39rgDFBskjZBDyDnVq7B+Ss5owac9aq0AKHTE7srwWHBwUUSkvODI/RCvbyghfd/lbCAhNdPmur3nxvUUr7qxEEkCvPFFPKtdtH+dUtsdVfav7oxLbKbgs7ijmLcfrrMxwAr2LDb2A5dsUn0aE7cCgYEAvuTfkYtahL/qERibSpunHbRalibuBjznApDySxgqFFul+JodjBH/gX7Xnh/ssINcG3Fq3UNKj0/6SqCVQLlZrDqpohUoPcjtuGgTCoviywxEOWLqYh8HIMTTNHOe6Qdto9K7WlFYL0RN5fWh9O/n/aDAE7aJTjUPwjGC50ihcrECgYEA5kSoBaecUbuTUCzIjcHxfrtLrNuKqPw9JF79kfBieYLcWHa2/F3LiOE0q6EbgZXxDRQx3PElqeGlTbd8kBlXvPr1wcs91evpKg0ttkCAcQem/Fj5deGscsaVFrUBkg2fpWs+CqFFrbCrNhnhgLEYipyc/xoGoP7JPT1Xz/8izxUCgYB06OUHVSo9zO+EQuawfb2Okqs79GGoTlpIlw7c8NKsnyh5paFc7JTn8RAKlpC11e1uHTsOgazDMn8ef1SJTDrgIRlM29pGZK06V/r5ZYyjQEL0RmE3cLtD1WdoYzs6ikMapu/5M4JniFSQ9quWv+r7yRx5tjxHOnYJP5tlHHVMMQKBgEqTtpThsU7mld1w5bVsjOws+waTKH1GXust7DBuxILGJ4LODn+YAP2h1T8mNbiyGaE25V+Ct9EBdwFW73JFFj3kxSyGEqvonXkMF1CsgEiU+jeOXhOYAD+MF8zcekdxvY/7v1tl4sic+cCZdQJJ/pgIVY719A0zUKM8n7v8GyO/";
    private static String KEY_ALGORITHM = "RSA";


    private static PublicKey generatePublicKeyFrom(String publicKey)  throws Exception{
        byte[] keyBytes = decode(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePublic(spec);
        } catch (Exception  e) {
            return null;
        }
    }
    private static PrivateKey generatePrivateKeyFrom(String privateKey) throws Exception {
        byte[] keyBytes = decode(privateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePrivate(spec);
        } catch (Exception e) {
            return null;
        }
    }


    public static String encryptByPrivateKey(String data, String privateKey) {
        try {
            PrivateKey pk = generatePrivateKeyFrom(privateKey);
            Cipher c = Cipher.getInstance(KEY_ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, pk);
            byte[] bytes = c.doFinal(data.getBytes());
            return encode(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptByPublicKey(String data, String publicKey) {
        try {
            PublicKey pk = generatePublicKeyFrom(publicKey);
            Cipher c = Cipher.getInstance(KEY_ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, pk);
            byte[] bytes = c.doFinal(decode(data));
            return new String(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encode(byte[] bytes) throws Exception {
        return new String(Base64.encodeBase64(bytes));
    }
    public static byte[] decode(String base64) throws Exception {
        return Base64.decodeBase64(base64.getBytes());
    }

    public static void main(String[] args) throws Exception{
        System.out.println(encryptByPrivateKey("440982199306284752", PRIVATE_KEY_STR));
        System.out.println(decryptByPublicKey("SP+R7HU6b+wLlWQ2z1KZvhdUQnPZeNPrWU6Ek5/IQfiZYxJkHtQn0T0hliLlPNPxSi3+OQizg+OQ9eAhXr4mBdFm9evPCJrLM8js/aw88knx5uWCPX6suJLCNOhhRYU5kgTPJv1B7A2riGuAJ3+GlGtzt0lH/JD8UzBjA3c6tzeOc0fhY8kjq59CE9AqVKSDDHN+AlYn9+E3zK7xMG7ZcdbtSomDTdZJPWnqOjqm6fft6sHWukNBXzWTR7BIABew+9vsbr8Lq90Nh0a7xtOwIcJL/+Q/xiCgiLpAYckTXZChj5Tf90GyztejsmtTrJhy8LborNvsp54TUGxhcxkDUw==",PUBLIC_KEY_STR));

    }

}
