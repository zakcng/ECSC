package main;


import java.security.Security;
import java.security.Provider;
import java.security.MessageDigest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;



public class sha3_256 {
    Security.addProvider(new BouncyCastleProvider());



}
