package main;


import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;



public class sha3_512 {

    public static String createHash(String stringToHash) {

        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
        byte[] digest = digestSHA3.digest(stringToHash.getBytes());

        return(Hex.toHexString(digest));
    }

    public static void main(String[] args) {

        System.out.println("SHA3-512 =" + createHash("Test String"));


    }
}
