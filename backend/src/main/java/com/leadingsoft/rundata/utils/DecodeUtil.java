package com.leadingsoft.rundata.utils;


import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

public class DecodeUtil {

  static {
    Security.addProvider(new BouncyCastleProvider());
  }


  public static String onDecrypt(String encryptedData, String sessionKey, String iv) throws Exception {
    // 被加密的数据
    byte[] dataByte = Base64.decodeBase64(encryptedData);
    // 加密秘钥
    byte[] keyByte = Base64.decodeBase64(sessionKey);
    // 偏移量
    byte[] ivByte = Base64.decodeBase64(iv);
    try {

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
      SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
      AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
      parameters.init(new IvParameterSpec(ivByte));

      cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
      byte[] resultByte = cipher.doFinal(dataByte);
      if (null != resultByte && resultByte.length > 0) {
        String result = new String(resultByte, "utf-8");

        return result;
      }
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidParameterSpecException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }
    return null;
  }

}
