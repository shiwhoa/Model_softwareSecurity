package com.company;


import java.util.LinkedList;
import java.util.Queue;

// CLASS: Can call MBRC in other Java Classes and declare MBRC type variables
public class MBRC {

    //Dec: Variables are private to this class only and cannot be called by other Java Classes.//Can use this function for instances
    private int maxMessageBuffer = 1;
    private int messageCount = 0;
    private int maxReplyBuffer = 1;
    private int replyCount = 0;
    private static boolean messageBufferFull = false;
    private static boolean replyBufferFull = false;

    Queue<Message> messageBuffer = new LinkedList<Message>();
    Queue<Message> replyBuffer = new LinkedList<Message>();
    //
    //

    public void MBRC() {


    }

/*

    // MBRC CLASS METHOD MEMBER: send; MBRC.send(String op, int num);
    // SYNCHRONIZED: Multiple threads cannot access MBRC.send concurrently changing the value of the output concurrently
    // INPUT: String type variable op; int type variable num;
    // OUTPUT: String type variable msgBackToSender;
    public synchronized String send(String op, int num) throws InterruptedException {
        String message = op + "" + num; //Dec: String type variable message;
        System.out.println(message);
        this.messageBuffer.add(message);// Adds to messageBuffer queue
        this.messageCount++;
        System.out.println("(\"" + op + "\"," + num + ") is placed in queue buffer.");

        if(this.messageCount == this.maxMessageBuffer) {

            this.messageBufferFull = true;
            this.notifyAll();

        }

        while(!this.responseBufferFull) {

            this.wait();
        }

        String msgBackToSender = this.replyBuffer.remove();
        this.replyCount--;
        this.responseBufferFull = false;

        return msgBackToSender;
    }
    //
    //
*/


    // MBRC CLASS METHOD MEMBER: send; MBRC.send(String op, int num);
    // SYNCHRONIZED: Multiple threads cannot access MBRC.send concurrently changing the value of the output concurrently
    // INPUT: String type variable op; int type variable num;
    // OUTPUT: String type variable msgBackToSender;

    public synchronized Message sendEn(String senderGenOpString, String senderGenNumString, byte[] senderGenNumSignatureBytes) throws InterruptedException {
        Message senderGenFullMsgString = new Message();
        senderGenFullMsgString.message_content = senderGenOpString + " " + senderGenNumString; //Dec: String type variable message;
        senderGenFullMsgString.message_signature = senderGenNumSignatureBytes;
        System.out.println("Full message in MBRC : " + senderGenFullMsgString.message_content + " with Signature " + senderGenFullMsgString.message_signature);
        this.messageBuffer.add(senderGenFullMsgString);// Adds to messageBuffer queue
        ++this.messageCount;
        System.out.println("(\"" + senderGenOpString + "\"," + senderGenNumString + ") is placed in queue buffer.");

        if(this.messageCount == this.maxMessageBuffer) {

            this.messageBufferFull = true;
            this.notifyAll();

        }
        //
        //

        while(!this.replyBufferFull) {

            this.wait();
        }

        Message msgBackToSender = this.replyBuffer.remove();
        --this.replyCount;
        this.replyBufferFull = false;

        return msgBackToSender;
    }
    //
    //


    // MBRC CLASS METHOD MEMBER: receive; MBRC.receive();
    // SYNCHRONIZED: Multiple threads cannot access MBRC.receive concurrently changing the value of the output concurrently
    // INPUT: None
    // OUTPUT: String type variable msgBackToReceiver;
    public synchronized Message receive() throws InterruptedException {
        while(!this.messageBufferFull) {

            this.wait();

        }

        Message msgToReceiver = this.messageBuffer.remove();
        --this.messageCount;
        this.messageBufferFull = false;
        return msgToReceiver;
    }
    //
    //



    // MBRC CLASS METHOD MEMBER: receive; MBRC.reply(String op, int num);
    // SYNCHRONIZED: Multiple threads cannot access MBRC.receive concurrently changing the value of the output concurrently
    // INPUT: None
    // OUTPUT: int type variable num;
    public synchronized Message reply(String resultFromReceiverNumString, byte[] receiverGenNumMDBytes) {
        //String response = op + " " + num;
        Message receiverGenFullResult = new Message();
        receiverGenFullResult.message_content = resultFromReceiverNumString; //Dec: String type variable message;
        receiverGenFullResult.message_signature = receiverGenNumMDBytes;
        this.replyBuffer.add(receiverGenFullResult);
        ++this.replyCount;
        if (this.replyCount == this.maxReplyBuffer) {
            this.replyBufferFull = true;
            this.notifyAll();
        }
        return receiverGenFullResult;
    }



/*    public static void send(Message message4, byte[] signedmessage) {
        Message messageBuffer = message4;
        messageBufferFull = true;
        while (responseBufferFull == false) {


        }

    }*/


/*    public void receive(Message message4) throws InterruptedException {
        *//*while(messageBufferFull == false) {
            wait(500);
        }
        //messageBuffer = null;
        messageBufferFull = false;*//*


        }*/
    }

