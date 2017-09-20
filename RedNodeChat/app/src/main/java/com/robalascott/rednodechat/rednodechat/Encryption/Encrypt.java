package com.robalascott.rednodechat.rednodechat.Encryption;

import android.util.Log;

import org.java_websocket.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import se.simbio.encryption.Encryption;

/**
 * Created by robscott on 2017-07-26.
 */

public class Encrypt {
    private static String key = "LivingLoveProgress";
    private static String salt = "RandomInitVector";
    private static byte[] iv = {-89, -19, 17, -83, 86, 106, -31, 30, -5, -111, 61, -75, -84, 95, 120, -53};
    private static Encryption encryption = Encryption.getDefault(key, salt, iv);


    public static String encrypt( String value) {
        try {
            String temp  = encryption.encryptOrNull(value);
            Log.i("Encrypt",temp);
            return temp;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static String decrypt( String encrypted) {
        try {

            String temp  = encryption.decryptOrNull(encrypted);;
            Log.i("Decrypt",temp);
            return temp;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

}
