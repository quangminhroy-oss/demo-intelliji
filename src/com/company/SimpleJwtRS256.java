import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

public class JWT {

    public static void main(String[] args) throws Exception {

        BigInteger p = BigInteger.valueOf(61);
        BigInteger q = BigInteger.valueOf(53);

        BigInteger n = p.multiply(q);

        BigInteger one = BigInteger.ONE;
        BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));

        BigInteger e = BigInteger.valueOf(17);
        BigInteger d = e.modInverse(phi);

        System.out.println("PUBLIC KEY (e, n) = (" + e + ", " + n + ")");
        System.out.println("PRIVATE KEY (d, n) = (" + d + ", " + n + ")");

        String headerJson = "{\"alg\":\"RS256\",\"typ\":\"JWT\"}";
        String payloadJson = "{\"sub\":\"user123\",\"role\":\"admin\"}";

        String header = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));

        String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

        String unsignedToken = header + "." + payload;




        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(unsignedToken.getBytes(StandardCharsets.UTF_8));




        BigInteger hashInt = new BigInteger(1, hash);



        BigInteger signatureInt = hashInt.modPow(d, n);



        String signature = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(signatureInt.toByteArray());




        //String jwt = unsignedToken + "." + signature;

        System.out.println("\nHeader: " + header);
        System.out.println("Payload: " + payload);
        //System.out.println("Signature: " + signature);

        System.out.println("\nFULL JWT:");
        //System.out.println(jwt);
    }
}
