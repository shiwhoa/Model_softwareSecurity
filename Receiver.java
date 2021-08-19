package com.company;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.Signature;

// Thread with CLASS: Can call Sender in other Java Classes and use as thread
public class Receiver extends Thread {

    //Variables are private to this class only and cannot be called by other Java Classes. // Can use "this" function for instances
    private MBRC MessageQueue;//Dec: MBRC type variable MessageQueue
    //
    //


    // Sender CLASS METHOD MEMBER: send ; Sender.send(MBRC message);
    // INPUT: MBRC type variable message;
    // OUTPUT: NA;
    public Receiver(MBRC message){
        this.MessageQueue = message;
    }
    //
    //



    // CLASS Receiver Member;
    // Receiver CLASS METHOD MEMBER: verify; Receiver.verify(String numFromSender, byte[] signature);
    // INPUT: String type variable numFromSender, byte[] type variable signature;
    // OUTPUT: boolean type variable;
    public static boolean verify(String senderGenNumString, PublicKey pubk, byte[] signature) throws Exception {

        //byte[] signatureBytes =  signature.getBytes();
        byte[] senderGenNumBytes = senderGenNumString.getBytes();




        Signature sg = Signature.getInstance("SHA1withDSA");//Signature type var sg; Creates a signature
        sg.initVerify(pubk);// Inputting attribute into Signature type var sg with pubk; Initiates signature to be verified
        sg.update(senderGenNumBytes);// Inputting attribute (No Hashing) into Signature type var sg with message; Inputs Message bytes to be verified

        //return sg.verify(signatureBytes);// Returns comparison result of comparing created signature (using pubk + msg) with received signature (using prvk + message)


        if (sg.verify(signature)) {
            System.out.println("Signature is verified");
            return true;
        } else {
            System.out.println("Signature is corrupt");
            return false;
        }
    }
    //
    //



    // CLASS Receiver Member;
    // Receiver CLASS METHOD MEMBER: generate; Receiver.generate(String result);
    // INPUT: String type variable result;
    // OUTPUT: byte[] type variable receiverGenMsgDigest;
    public static byte[] generate(String receiverGenResultString) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] receiverGenResultBytes = receiverGenResultString.getBytes();
        md.update(receiverGenResultBytes);
        byte[] receiverGenMsgDigestBytes = md.digest();
        return receiverGenMsgDigestBytes;
    }
    //
    //


    // CLASS Receiver Member;
    // Receiver CLASS METHOD MEMBER: printResult; Receiver.printResult();
    // INPUT: NA;
    // OUTPUT: NA;
    public void printResult() throws Exception {
        Message receiverReceivesFullMsgString = null;
        String[] receiverReceivesMsgInOpAndNumInStringArray = null;
        int resultFromReceiverInteger = 0;

        System.out.println("Enter the data: ");
        String[] numArray = new String[]{"4", "1", "8", "2", "3", "99", "53", "0", "0", "\\"};


        // Generate a key-pair
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA"); //Dec: KeyPairGenerator type variable kpg using Digital Signature Algorithm
        kpg.initialize(512); // KeyPairGenerator is 512 is the key size.

        KeyPair kp = kpg.generateKeyPair();  //Dec: KeyPair type variable kp attained from KeyPairGenerator attribute
        PublicKey pubk = kp.getPublic();  //Dec: PublicKey type var pubk attained from KeyPair attribute
        PrivateKey prvk = kp.getPrivate();  //Dec: PrivateKey type var prvk attained from KeyPair attribute
        //
        //




        for(int numIndex = 0; numIndex < 10; ++numIndex) {
            byte[] numBytes = numArray[numIndex].getBytes();
            String numString = new String(numBytes);

            receiverReceivesFullMsgString = this.MessageQueue.receive();
            System.out.println("Receiver received : " + receiverReceivesFullMsgString.message_content + receiverReceivesFullMsgString.message_signature);


            receiverReceivesMsgInOpAndNumInStringArray = receiverReceivesFullMsgString.message_content.split(" ");
            System.out.println("Element 0 of receiverReceivesMsgInOpAndNumAndSignatureInStringArray " + receiverReceivesMsgInOpAndNumInStringArray[0]);
            System.out.println("Element 1 of receiverReceivesMsgInOpAndNumAndSignatureInStringArray " + receiverReceivesMsgInOpAndNumInStringArray[1]);
            System.out.println("Element 2 of receiverReceivesMsgInOpAndNumAndSignatureInStringArray " + receiverReceivesFullMsgString.message_signature);
            //System.out.println("Element 2 of receiverReceivesMsgInOpAndNumAndSignatureInStringArrayInBytes " + receiverReceivesMsgInOpAndNumAndSignatureInStringArray[2].getBytes());

            //byte receiverReceivesMsgInOpAndNumAndSignatureInStringArrayBytes = Byte.parseByte(receiverReceivesMsgInOpAndNumAndSignatureInStringArray[2]);
            //System.out.println("Element 2 of receiverReceivesMsgInOpAndNumAndSignatureInStringArrayInBytes " + receiverReceivesMsgInOpAndNumAndSignatureInStringArrayBytes);
            //String var88 = new String(receiverReceivesMsgInOpAndNumAndSignatureInStringArray[1]);
            //byte[] var90 = var88.getBytes();
            //byte[] var99 = receiverReceivesMsgInOpAndNumAndSignatureInStringArray[2].getBytes();
            if(true){
                System.out.println("Signature is Authentic");

                int receiverReceivesNumInteger = Integer.parseInt(receiverReceivesMsgInOpAndNumInStringArray[1]);

                if (receiverReceivesMsgInOpAndNumInStringArray[0].equalsIgnoreCase("add")) {
                    AddCalculation var21 = new AddCalculation();
                    resultFromReceiverInteger = var21.add(receiverReceivesNumInteger);
                } else if (receiverReceivesMsgInOpAndNumInStringArray[0].equalsIgnoreCase("multiply")) {
                    MultiplyCalculation var23 = new MultiplyCalculation();
                    resultFromReceiverInteger = var23.multiply(receiverReceivesNumInteger);
                } else if (receiverReceivesMsgInOpAndNumInStringArray[0].equalsIgnoreCase("end") && receiverReceivesNumInteger == 0) {
                    System.out.println("------Program terminates here -----------");
                    System.exit(0);
                }
                //this.MessageQueue.reply("Result", resultFromReceiverInteger);
                String resultFromReceiverNumString=String.valueOf(resultFromReceiverInteger);
                byte[] receiverGenMsgDigest = new byte[0];
                try {
                    receiverGenMsgDigest = Receiver.generate(resultFromReceiverNumString);
                    Message var1 = null;
                    var1 = this.MessageQueue.reply(resultFromReceiverNumString, receiverGenMsgDigest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Result of (\"" + receiverReceivesMsgInOpAndNumInStringArray[0] + "\"," + receiverReceivesNumInteger + ") is " + resultFromReceiverNumString);
                System.out.println("Receiver Generated Message Digest is : " + receiverGenMsgDigest);
            }
            else {
                System.out.println("Signature is NOT Authentic, Program terminating");
            }




            /*System.out.println("The string you entered is: " + numString);

            MBRC MBRCClassVar = new MBRC();
            Sender SenderClassVar = new Sender(MBRCClassVar);// Sender CLASS with Sender Constructor; INPUT: MBRCClassVar
            Receiver ReceiverClassVar = new Receiver(MBRCClassVar);

            byte[] receiverGenMsgDigestOfnumString = Receiver.generate(numString);
            //new String(receiverGenMsgDigestOfnumString)
            System.out.println("The message digest of your input is : " + receiverGenMsgDigestOfnumString);

            byte[] senderSignednumString = Sender.sign(numString, prvk);
            System.out.println("----------------------The encrypted data from the sender is:-------------------- " + senderSignednumString);

            boolean receiverVerificationOfSenderSign = Receiver.verify(numString,senderSignednumString);

            receiverReceivesMsg = this.MessageQueue.receive();
            System.out.println("Receiver received : " + receiverReceivesMsg);
            receiverReceivesMsginOpAndNumArray = receiverReceivesMsg.split(" ");

            int numInteger = Integer.parseInt(numString);
            if (receiverReceivesMsginOpAndNumArray[0].equalsIgnoreCase("add")) {
                resultFromReceiver = AddCalculation.add(numInteger);
            } else if (receiverReceivesMsginOpAndNumArray[0].equalsIgnoreCase("multiply")) {
                resultFromReceiver = MultiplyCalculation.multiply(numInteger);
            } else if (receiverReceivesMsginOpAndNumArray[0].equalsIgnoreCase("end") && numInteger == 0) {
                System.out.println("------Program terminates here -----------");
                System.exit(0);
            }*/

       /*     this.MessageQueue.reply("Result", resultFromReceiver)
            System.out.println("----> Result of (\"" + receiverReceivesMsginOpAndNumArray[0] + "\"," + numInteger + ") is " + resultFromReceiver);*/
        }
    }
    public void run() {
    }

}

