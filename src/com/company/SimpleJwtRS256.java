import java.security.MessageDigest;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

public class SimpleJwtRS256 {

    public static void main(String[] args) throws Exception {

        BigInteger p = BigInteger.valueOf(61);
        BigInteger q = BigInteger.valueOf(53);

        BigInteger n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE)
                .multiply(q.subtract(BigInteger.ONE));

        BigInteger e = BigInteger.valueOf(17);
        BigInteger d = e.modInverse(phi);

        System.out.println("PUBLIC KEY  (e, n) = (" + e + ", " + n + ")");
        System.out.println("PRIVATE KEY (d, n) = (" + d + ", " + n + ")");

        String headerJson = "{\"alg\":\"RS256\",\"typ\":\"JWT\"}";
        String payloadJson = "{\"sub\":\"user123\",\"role\":\"admin\"}";

        String header = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));

        String payload = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

        String unsignedToken = header + "." + payload;

        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha256.digest(unsignedToken.getBytes(StandardCharsets.UTF_8));
        BigInteger hashInt = new BigInteger(1, hash);

        BigInteger signatureInt = hashInt.modPow(d, n);

        String signature = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(signatureInt.toByteArray());

        String jwt = unsignedToken + "." + signature;

        System.out.println("\nHeader  : " + header);
        System.out.println("Payload : " + payload);
        System.out.println("Signature: " + signature);

        System.out.println("\nFULL JWT:");
        System.out.println(jwt);


        boolean valid = verifyJWT(jwt, e, n);
        System.out.println("\nVERIFY RESULT: " + valid);
    }


    public static boolean verifyJWT(String jwt, BigInteger e, BigInteger n) throws Exception {

        String[] parts = jwt.split("\\.");
        if (parts.length != 3) return false;

        String header = parts[0];
        String payload = parts[1];
        String signatureB64 = parts[2];

        String unsignedToken = header + "." + payload;


        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha256.digest(unsignedToken.getBytes(StandardCharsets.UTF_8));
        BigInteger hashInt = new BigInteger(1, hash);


        byte[] sigBytes = Base64.getUrlDecoder().decode(signatureB64);
        BigInteger sigInt = new BigInteger(1, sigBytes);

        BigInteger decryptedHash = sigInt.modPow(e, n);


        return decryptedHash.equals(hashInt.mod(n));

    }
}
