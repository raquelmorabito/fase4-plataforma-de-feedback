package br.com.fiap.report;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PemUtils {

    public static PublicKey loadPublicKey(String resourcePath) {
        try (InputStream is =
                     PemUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {

            if (is == null) {
                throw new RuntimeException("Public key PEM não encontrado");
            }

            String pem = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            pem = pem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(pem);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePublic(spec);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar public key", e);
        }
    }
}

