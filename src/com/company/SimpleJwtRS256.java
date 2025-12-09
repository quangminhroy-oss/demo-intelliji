import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

public class SimpleJwtRS256 {

    public static void main(String[] args) throws Exception {




        BigInteger p = BigInteger.valueOf(61);
        BigInteger q = BigInteger.valueOf(53);


        BigInteger n = p.multiply(q);

        // Tính phi(n) = (p - 1)(q - 1)
        BigInteger one = BigInteger.ONE;

        BigInteger phi = (p.subtract(one))
                .multiply(q.subtract(one));


        // Chọn số mũ e
        BigInteger e = BigInteger.valueOf(17);

        // Tính d = e^-1 mod phi
        BigInteger d = e.modInverse(phi);

        // In khoá
        System.out.println("PUBLIC KEY (e, n) = (" + e + ", " + n + ")");
        System.out.println("PRIVATE KEY (d, n) = (" + d + ", " + n + ")");


        KeyFactory factory = KeyFactory.getInstance("RSA");

        RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(n, e);
        PublicKey publicKey = factory.generatePublic(pubSpec);

        RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(n, d);
        PrivateKey privateKey = factory.generatePrivate(privSpec);


        String headerJson = "{\"alg\":\"RS256\",\"typ\":\"JWT\"}";
        String payloadJson = "{\"sub\":\"user123\",\"role\":\"admin\"}";

        String header = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));

        String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

        String unsignedToken = header + "." + payload;


        Signature rsa = Signature.getInstance("SHA256withRSA");
        rsa.initSign(privateKey);
        rsa.update(unsignedToken.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = rsa.sign();

        String signature = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(signatureBytes);

        String jwt = unsignedToken + "." + signature;


        System.out.println("\nHeader: " + header);
        System.out.println("Payload: " + payload);
        System.out.println("Signature: " + signature);

        System.out.println("\nFULL JWT:");
        System.out.println(jwt);
    }
}
