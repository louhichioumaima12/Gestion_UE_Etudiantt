package utilities;

import java.security.Key;
import javax.crypto.spec.SecretKeySpec;

public class JwtKeyProvider {

    // Secret d'au moins 64 caractères (512 bits) pour HS512.
    // En vrai projet : le lire depuis une variable d'environnement.
    private static final String SECRET =
            "ChangezMoiParUnSecretTresLongEtAleatoireDAuMoins64CaracteresPourHS512!!";

    private JwtKeyProvider() {
    }

    public static Key getKey() {
        byte[] bytes = SECRET.getBytes();
        return new SecretKeySpec(bytes, 0, bytes.length, "HmacSHA512");
    }
}