package com.company;
import java.security.MessageDigest;
//import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Hashing {


    public static byte[] generate(String msg) throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] message = msg.getBytes();
        md.update(message);
        byte[] mdbytes = md.digest();
        return(mdbytes);
    }

    public static Boolean verify(byte[] hashValue, String msg) throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] msgBytes = msg.getBytes();
        md.update(msgBytes);
        byte[] mdBytes = md.digest();

        if (MessageDigest.isEqual(hashValue, mdBytes))
            return true;
        else
            return false;
    }
}
