package org.lance.cloud.demo;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA2 加解密示例
 *
 * @author Lance
 */
public class RSA2Demo {

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        String content = "加密测试";

        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCztxboEWtoxmPX\n" +
                "kuNCDL7T18C/LbsbAUFTnkwNTH2KX6FIpnxLOOm3fZBs7VRKsiyD2uWa8MK+UK2i\n" +
                "tqOPi/VfrMGtt5LEmFF9SwY2JxaxP1q6BVj0slU9Giy3Z+DQ97hAIi/du2lV7FXn\n" +
                "dt4wRInlttCNrAne12l76Mzh1RsE44kZ6LoaNPnI+cafbyjthx4TXzliHLuVlRE4\n" +
                "tP693baT0JU2vXc+yw6kOrG8UlDUKRZ9K3M8MkegSjqDqD5fw8RSEsOCQO7z0l2W\n" +
                "OjVx1cJ5p3YMnfv4LKuPWNoJZuVrZ5EsKiSSWAixghEZAcSpdetJ3k13plScMxjw\n" +
                "aUx3AcITAgMBAAECggEACrEa2MWoiTw8Vmfzw0s00y+j1PvohMvHUi05wxTI50GX\n" +
                "O9ZD0MnbBcKfpAtel28QPOv5+wTV4TK1kdeh1XlzKLUDdM1WGwIg3v5mqTQcIoFt\n" +
                "aWHGEBDIB3GWNXclpekG+jHOfTeYJbeAyNuGjoKVXahJshQJJkvUh72q6sid6eVy\n" +
                "BzMiFp6yLsXTHtmdWBMy/m2Wo2dNISDyervRVCTxUKMZ+XmLiA008CW3tFZzYC4Z\n" +
                "F/fqfd9++d6vZAzfyNvsyTgNWRXS6APuMkH6nMSBYs0MesYXhewcJo1PtgCiQ+Zd\n" +
                "+XU3oujpe7BgF8O6zFactPZHos5Wt5iIvRIYg0dmCQKBgQDghbXAetcafSQl1nks\n" +
                "ZV3sMzl2/TmERMhmvwJjOP2rODSokTpj8DjENKEQ+GlzkeCNi6jbr2ruqVLtGtsO\n" +
                "+mKXn+ph421kKdXe5TLnYAtVCy6lLkPgdrm2KA9a0r/spszetzewnJBEAKIQIktF\n" +
                "Rpy3Yj9pQfv9Jpd9ZInGzTe/lwKBgQDM6TcohLlH7JB/UiM2Q/juiPs1bVyETdb/\n" +
                "+512KYb7bjqDfY7T6GzNAZSEM1Ih9hbQX/IuHgPDPaLZaMi7MvkyItZWYkdMKXar\n" +
                "ltHNZY/7XRwWrK1XlOhqbY9W8dH1x0E2tAgdJuwNI0Ki3efmqVaKZ3UYAg9az+/C\n" +
                "T3qRU2Og5QKBgC9na7lo9jOMim5GuyzJdiwhoJdj3Zn0n885svpaFQzqgAu7JfXl\n" +
                "yUZ7yin4GOOiwCOCgjNn9Q7Qx0Zybh/ac6iJxgXG69jDEHnQMJAzchERnFaZzxD8\n" +
                "YDd6e8t0RbtkeErpqiNq2vbQn2cCppvIzdvWLYc+vEJ1vHRoHsLPn16fAoGAdTYy\n" +
                "RwsksO+w+COT4Mag7lGM4NLdyRB9m4/iUmUOwWQcNHrt6GlbUylEOxrgMG0EP82i\n" +
                "Sp8Ap/3p2VMG/4343aTv1hoBjp9pMl3jRQqDgdWQ66d6wE59Y+OnyqQo5d5Ef3Yb\n" +
                "7hEFlb5Fr8QfkuHqjuD/KeyDu/6VtyCfi+GXa8ECgYEAp1dmCrUkj/sp3rLmMcFY\n" +
                "6znbZ96UOu+FXNGIdiVEGzRbxX08KKoLwhSDRljTqe8eCCN3ab7tIU1RYadpROOn\n" +
                "msLZVetCl5wByz3Vxw1ssP1pZgQHSctsk09dEWys6KqMihLbDze4Ipz9OQPgfFWj\n" +
                "jT5mZD7chH2YsNqmAYuT6HA=";

        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs7cW6BFraMZj15LjQgy+\n" +
                "09fAvy27GwFBU55MDUx9il+hSKZ8Szjpt32QbO1USrIsg9rlmvDCvlCtorajj4v1\n" +
                "X6zBrbeSxJhRfUsGNicWsT9augVY9LJVPRost2fg0Pe4QCIv3btpVexV53beMESJ\n" +
                "5bbQjawJ3tdpe+jM4dUbBOOJGei6GjT5yPnGn28o7YceE185Yhy7lZUROLT+vd22\n" +
                "k9CVNr13PssOpDqxvFJQ1CkWfStzPDJHoEo6g6g+X8PEUhLDgkDu89Jdljo1cdXC\n" +
                "ead2DJ37+Cyrj1jaCWbla2eRLCokklgIsYIRGQHEqXXrSd5Nd6ZUnDMY8GlMdwHC\n" +
                "EwIDAQAB";

        KeyPairGenerator rsaKeyPairGenerator = KeyPairGenerator.getInstance("RSA");
        rsaKeyPairGenerator.initialize(2048);


        // 私钥加密
        byte[] privateKeyBytes = Base64.decodeBase64(privateKey);
        // PKCS8EncodedKeySpec 用于私钥
        PKCS8EncodedKeySpec privateEncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

        KeyFactory rsaKeyFactory = KeyFactory.getInstance("RSA");
        PrivateKey rsaPrivateKey = rsaKeyFactory.generatePrivate(privateEncodedKeySpec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, rsaPrivateKey);

        byte[] result = cipher.doFinal(content.getBytes());
        System.out.println("加密: " + Base64.encodeBase64String(result));


        // 公钥解密
        byte[] publicKeyBytes = Base64.decodeBase64(publicKey);
        // X509EncodedKeySpec 用于公钥
        X509EncodedKeySpec publicEncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);

        PublicKey rsaPublicKey = rsaKeyFactory.generatePublic(publicEncodedKeySpec);

        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, rsaPublicKey);

        result = cipher.doFinal(result);
        System.out.println("解密：" + new String(result));
    }
}
