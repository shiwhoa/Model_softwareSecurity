package com.company;

public class Main {     // CLASS: can call Main in other Java Classes

    public static void main(String[] args) throws Exception { // MEMBER:

        System.out.println("Program: To perform addition and multiplication operation on integer 10.");
        System.out.println("------------------------------------------------------------------------");
        MBRC var1 = new MBRC();
        Sender var2 = new Sender(var1);
        Receiver var3 = new Receiver(var1);
        var2.start();
        //var2.printMessage();
        var3.printResult();
        System.out.println("Program: An example about using confidentiality security from sender to the receiver.");
        System.out.println("------------------------------------------------------------------------");
    }

        //Sender.printMessage(message);
        //byte[] signedMessage = Sender.sign(message);
        //MBRC.send(message, signedMessage);

    }
