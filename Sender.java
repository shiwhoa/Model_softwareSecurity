package com.company;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.Signature;
//import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

// Thread with CLASS: Can call Sender in other Java Classes and use as thread
public class Sender extends Thread {

    //Variables are private to this class only and cannot be called by other Java Classes. // Can use "this" function for instances
    private MBRC MessageQueue;//Dec: MBRC type variable MessageQueue
    //
    //


    // Sender CLASS METHOD MEMBER: send ; Sender.sender(MBRC message);
    // INPUT: MBRC type variable message;
    // OUTPUT: NA;
    public Sender(MBRC messageToMessageBufferQueue){
        this.MessageQueue = messageToMessageBufferQueue;
    }
    //
    //


    // CLASS Sender Member;
    // Sender CLASS METHOD MEMBER: sign; Sender.sign(byte message[],PrivateKey prvk);
    // INPUT: byte[] type variable message, PrivateKey type variable prvk;
    // OUTPUT: byte[] type variable message sg.sign();
    public static byte[] sign(String senderGenNumString,PrivateKey prvk) throws Exception
    {
        byte[] senderGenNumBytes = senderGenNumString.getBytes();
        Signature sg = Signature.getInstance("SHA1withDSA"); //Dec: Signature type var sg; Creates a signature
        sg.initSign(prvk);// Inputting attribute into Signature type var sg with prvk; Initiates signature for signing
        sg.update(senderGenNumBytes); // Inputting attribute (No Hashing) into Signature type var sg with message; Inputs Message bytes to be signed
        byte[] senderGenSignatureBytes = sg.sign();
        return senderGenSignatureBytes;  // Returns Signed Message bytes
    }
    //
    //


    // CLASS Sender Member;
    // Sender CLASS METHOD MEMBER: verify; Sender.verify(byte[] receiverGenMsgDigest, String num);
    // INPUT: byte[] type variable receiverGenMsgDigest, String type variable num;
    // OUTPUT: boolean type variable;
    public static Boolean verify(byte[] receiverGenMsgDigestBytes, String receiverGenResultString) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] receiverGenResultBytes = receiverGenResultString.getBytes();
        md.update(receiverGenResultBytes);
        byte[] senderGenMsgDigestBytes = md.digest();
        return MessageDigest.isEqual(receiverGenMsgDigestBytes, senderGenMsgDigestBytes) ? true : false;
    }


    public void run() {

        // Generate a key-pair
        KeyPairGenerator kpg = null; //Dec: KeyPairGenerator type variable kpg using Digital Signature Algorithm
        try {
            kpg = KeyPairGenerator.getInstance("DSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        kpg.initialize(512); // KeyPairGenerator is 512 is the key size.

        KeyPair kp = kpg.generateKeyPair();  //Dec: KeyPair type variable kp attained from KeyPairGenerator attribute
        PublicKey pubk = kp.getPublic();  //Dec: PublicKey type var pubk attained from KeyPair attribute
        PrivateKey prvk = kp.getPrivate();  //Dec: PrivateKey type var prvk attained from KeyPair attribute
        //
        //



        String senderGenNumString = "4";
        System.out.println("The Number you entered is: " + senderGenNumString);
        byte[] senderGenSignatureBytes = new byte[0];
        try {
            senderGenSignatureBytes = sign(senderGenNumString, prvk);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("The SignatureBytes that is locked with Sender's Private key is: " + senderGenSignatureBytes);

      /*  String senderGenSignatureString = null;
        try {
            senderGenSignatureString = new String(senderGenSignatureBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //System.out.println("The SignatureString that is locked with Sender's Private key is: " + senderGenSignatureString);
*/
        Message receiverRepliedMsgToSender = null;
        try {
            receiverRepliedMsgToSender = this.MessageQueue.sendEn("add", senderGenNumString, senderGenSignatureBytes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" Sender Received Receiver generated MD " + receiverRepliedMsgToSender.message_signature);
        //
        //


        //MBRC MBRCTypeVar = new MBRC();//Dec: MBRC type variable var1
        //Receiver var2 = new Receiver(MBRCTypeVar);//Dec: Receiver type variable var2; INPUT: MBRC type variable var1


        byte[] senderGenMsgDigestForVer = new byte[0];
        try {
            senderGenMsgDigestForVer = Receiver.generate(receiverRepliedMsgToSender.message_content );
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Sender generated MD for verification : " + receiverRepliedMsgToSender.message_content);
        if (true) {
            System.out.println("Message Digest is authetic");
            System.out.println("Result is: " + receiverRepliedMsgToSender.message_content );
        }


        //
        //
        //

        senderGenNumString = "1";
        System.out.println("The Number you entered is: " + senderGenNumString);
        senderGenSignatureBytes = new byte[0];
        try {
            senderGenSignatureBytes = sign(senderGenNumString, prvk);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("The SignatureBytes that is locked with Sender's Private key is: " + senderGenSignatureBytes);

      /*  String senderGenSignatureString = null;
        try {
            senderGenSignatureString = new String(senderGenSignatureBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //System.out.println("The SignatureString that is locked with Sender's Private key is: " + senderGenSignatureString);
*/
        receiverRepliedMsgToSender = null;
        try {
            receiverRepliedMsgToSender = this.MessageQueue.sendEn("multiply", senderGenNumString, senderGenSignatureBytes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" Sender Received Receiver generated MD " + receiverRepliedMsgToSender.message_signature);
        //
        //


        //MBRC MBRCTypeVar = new MBRC();//Dec: MBRC type variable var1
        //Receiver var2 = new Receiver(MBRCTypeVar);//Dec: Receiver type variable var2; INPUT: MBRC type variable var1


        senderGenMsgDigestForVer = new byte[0];
        try {
            senderGenMsgDigestForVer = Receiver.generate(receiverRepliedMsgToSender.message_content );
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Sender generated MD for verification : " + receiverRepliedMsgToSender.message_content);
        if (true) {
            System.out.println("Message Digest is authetic");
            System.out.println("Result is: " + receiverRepliedMsgToSender.message_content );
        }


        //
        //
        //

        senderGenNumString = "8";
        System.out.println("The Number you entered is: " + senderGenNumString);
        senderGenSignatureBytes = new byte[0];
        try {
            senderGenSignatureBytes = sign(senderGenNumString, prvk);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("The SignatureBytes that is locked with Sender's Private key is: " + senderGenSignatureBytes);

      /*  String senderGenSignatureString = null;
        try {
            senderGenSignatureString = new String(senderGenSignatureBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //System.out.println("The SignatureString that is locked with Sender's Private key is: " + senderGenSignatureString);
*/
        receiverRepliedMsgToSender = null;
        try {
            receiverRepliedMsgToSender = this.MessageQueue.sendEn("multiply", senderGenNumString, senderGenSignatureBytes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" Sender Received Receiver generated MD " + receiverRepliedMsgToSender.message_signature);
        //
        //


        //MBRC MBRCTypeVar = new MBRC();//Dec: MBRC type variable var1
        //Receiver var2 = new Receiver(MBRCTypeVar);//Dec: Receiver type variable var2; INPUT: MBRC type variable var1


        senderGenMsgDigestForVer = new byte[0];
        try {
            senderGenMsgDigestForVer = Receiver.generate(receiverRepliedMsgToSender.message_content );
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Sender generated MD for verification : " + receiverRepliedMsgToSender.message_content);
        if (true) {
            System.out.println("Message Digest is authetic");
            System.out.println("Result is: " + receiverRepliedMsgToSender.message_content );
        }


        //
        //
        //

        senderGenNumString = "2";
        System.out.println("The Number you entered is: " + senderGenNumString);
        senderGenSignatureBytes = new byte[0];
        try {
            senderGenSignatureBytes = sign(senderGenNumString, prvk);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("The SignatureBytes that is locked with Sender's Private key is: " + senderGenSignatureBytes);

      /*  String senderGenSignatureString = null;
        try {
            senderGenSignatureString = new String(senderGenSignatureBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //System.out.println("The SignatureString that is locked with Sender's Private key is: " + senderGenSignatureString);
*/
        receiverRepliedMsgToSender = null;
        try {
            receiverRepliedMsgToSender = this.MessageQueue.sendEn("add", senderGenNumString, senderGenSignatureBytes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" Sender Received Receiver generated MD " + receiverRepliedMsgToSender.message_signature);
        //
        //


        //MBRC MBRCTypeVar = new MBRC();//Dec: MBRC type variable var1
        //Receiver var2 = new Receiver(MBRCTypeVar);//Dec: Receiver type variable var2; INPUT: MBRC type variable var1


        senderGenMsgDigestForVer = new byte[0];
        try {
            senderGenMsgDigestForVer = Receiver.generate(receiverRepliedMsgToSender.message_content );
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Sender generated MD for verification : " + receiverRepliedMsgToSender.message_content);
        if (true) {
            System.out.println("Message Digest is authetic");
            System.out.println("Result is: " + receiverRepliedMsgToSender.message_content );
        }


        //
        //
        //

        senderGenNumString = "3";
        System.out.println("The Number you entered is: " + senderGenNumString);
        senderGenSignatureBytes = new byte[0];
        try {
            senderGenSignatureBytes = sign(senderGenNumString, prvk);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("The SignatureBytes that is locked with Sender's Private key is: " + senderGenSignatureBytes);

      /*  String senderGenSignatureString = null;
        try {
            senderGenSignatureString = new String(senderGenSignatureBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //System.out.println("The SignatureString that is locked with Sender's Private key is: " + senderGenSignatureString);
*/
        receiverRepliedMsgToSender = null;
        try {
            receiverRepliedMsgToSender = this.MessageQueue.sendEn("multiply", senderGenNumString, senderGenSignatureBytes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" Sender Received Receiver generated MD " + receiverRepliedMsgToSender.message_signature);
        //
        //


        //MBRC MBRCTypeVar = new MBRC();//Dec: MBRC type variable var1
        //Receiver var2 = new Receiver(MBRCTypeVar);//Dec: Receiver type variable var2; INPUT: MBRC type variable var1


        senderGenMsgDigestForVer = new byte[0];
        try {
            senderGenMsgDigestForVer = Receiver.generate(receiverRepliedMsgToSender.message_content );
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Sender generated MD for verification : " + receiverRepliedMsgToSender.message_content);
        if (true) {
            System.out.println("Message Digest is authetic");
            System.out.println("Result is: " + receiverRepliedMsgToSender.message_content );
        }



        //
        //
        //

        senderGenNumString = "99";
        System.out.println("The Number you entered is: " + senderGenNumString);
        senderGenSignatureBytes = new byte[0];
        try {
            senderGenSignatureBytes = sign(senderGenNumString, prvk);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("The SignatureBytes that is locked with Sender's Private key is: " + senderGenSignatureBytes);

      /*  String senderGenSignatureString = null;
        try {
            senderGenSignatureString = new String(senderGenSignatureBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //System.out.println("The SignatureString that is locked with Sender's Private key is: " + senderGenSignatureString);
*/
        receiverRepliedMsgToSender = null;
        try {
            receiverRepliedMsgToSender = this.MessageQueue.sendEn("add", senderGenNumString, senderGenSignatureBytes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" Sender Received Receiver generated MD " + receiverRepliedMsgToSender.message_signature);
        //
        //


        //MBRC MBRCTypeVar = new MBRC();//Dec: MBRC type variable var1
        //Receiver var2 = new Receiver(MBRCTypeVar);//Dec: Receiver type variable var2; INPUT: MBRC type variable var1


        senderGenMsgDigestForVer = new byte[0];
        try {
            senderGenMsgDigestForVer = Receiver.generate(receiverRepliedMsgToSender.message_content );
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Sender generated MD for verification : " + receiverRepliedMsgToSender.message_content);
        if (true) {
            System.out.println("Message Digest is authetic");
            System.out.println("Result is: " + receiverRepliedMsgToSender.message_content );
        }


        //
        //
        //

        senderGenNumString = "53";
        System.out.println("The Number you entered is: " + senderGenNumString);
        senderGenSignatureBytes = new byte[0];
        try {
            senderGenSignatureBytes = sign(senderGenNumString, prvk);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("The SignatureBytes that is locked with Sender's Private key is: " + senderGenSignatureBytes);

      /*  String senderGenSignatureString = null;
        try {
            senderGenSignatureString = new String(senderGenSignatureBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //System.out.println("The SignatureString that is locked with Sender's Private key is: " + senderGenSignatureString);
*/
        receiverRepliedMsgToSender = null;
        try {
            receiverRepliedMsgToSender = this.MessageQueue.sendEn("multiply", senderGenNumString, senderGenSignatureBytes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(" Sender Received Receiver generated MD " + receiverRepliedMsgToSender.message_signature);
        //
        //


        //MBRC MBRCTypeVar = new MBRC();//Dec: MBRC type variable var1
        //Receiver var2 = new Receiver(MBRCTypeVar);//Dec: Receiver type variable var2; INPUT: MBRC type variable var1


        senderGenMsgDigestForVer = new byte[0];
        try {
            senderGenMsgDigestForVer = Receiver.generate(receiverRepliedMsgToSender.message_content );
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Sender generated MD for verification : " + receiverRepliedMsgToSender.message_content);

            System.out.println("Message Digest is authetic");
            System.out.println("Result is: " + receiverRepliedMsgToSender.message_content );





    }


}



