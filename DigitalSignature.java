package com.company;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.*;

public class DigitalSignature {

    public static byte[] sign(String senderGenNumString,PrivateKey prvk) throws Exception //CLASS MEMBER METHOD; INPUT:
    {
        byte[] senderGenNumBytes = senderGenNumString.getBytes();
        Signature sg = Signature.getInstance("SHA1withDSA"); //Signature type var sg; Creates a signature
        sg.initSign(prvk);// Inputting attribute into Signature type var sg with prvk; Initiates signature for signing
        sg.update(senderGenNumBytes); // Inputting attribute (No Hashing) into Signature type var sg with message; Inputs Message bytes to be signed
        // sg = f(prvk, message)
        return sg.sign();  // Returns Signed Message bytes
    }


    public static boolean verify (byte[] numFromSenderBytes, PublicKey pubk, byte[] signature) throws Exception
    {
        Signature sg = Signature.getInstance("SHA1withDSA");//Signature type var sg; Creates a signature
        sg.initVerify(pubk);// Inputting attribute into Signature type var sg with pubk; Initiates signature to be verified
        sg.update(numFromSenderBytes);// Inputting attribute (No Hashing) into Signature type var sg with message; Inputs Message bytes to be verified
        // sg = f(pubk, message)
        return sg.verify(signature);// Returns comparison result of comparing created signature (using pubk + msg) with received signature (using prvk + message)
    }

    /*public static void dsa(Message message3) throws Exception {

        //String concMessage = message3.message_name.concat(Integer.toString(message3.message_content));

        // Generate a key-pair
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA"); // KeyPairGenerator type variable kpg using Digital Signature Algorithm
        kpg.initialize(512); // KeyPairGenerator is 512 is the key size.

        KeyPair kp = kpg.generateKeyPair();  //KeyPair type variable kp attained from KeyPairGenerator attribute
        PublicKey pubk = kp.getPublic();  // PublicKey type var pubk attained from KeyPair attribute
        PrivateKey prvk = kp.getPrivate();  //PrivateKey type var prvk attained from KeyPair attribute

        byte[] signature = DigitalSignature.sign(message3, prvk);


        String str1 = new String(signature);  //Convert byte array type var signature to String and store in String type var str1
        System.out.println("The signature of input data : " + str1);

        boolean result = DigitalSignature.verify(message4, pubk, signature);
        System.out.println("Signature Verification Result = " + result);


    }*/

}
