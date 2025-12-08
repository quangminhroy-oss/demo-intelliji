import java.security.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class SimpleJwtRS256 {

    public static void main(String[] args) throws Exception {


        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
         keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();


        String headerJson = "{\"alg\":\"RS256\",\"typ\":\"JWT\"}";


        String payloadJson = "{\"sub\":\"user123\",\"role\":\"admin\"}";


        String header = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));

        String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

        String unsignedToken = header + "." + payload;


        Signature rsa = Signature.getInstance("SHA256withRSA")å;
        rsa.initSign(privateKey);
        rsa.update(unsignedToken.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = rsa.sign();

        String signature = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(signatureBytes);


        String jwt = unsignedToken + "." + signature;


        System.out.println("Header (Base64URL): " + header);
        System.out.println("Payload (Base64URL): " + payload);
        System.out.println("Signature: " + signature);

        System.out.println("\nFULL JWT:");
        System.out.println(jwt);
    }
}
