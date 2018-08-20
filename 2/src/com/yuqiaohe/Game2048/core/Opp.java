package com.yuqiaohe.Game2048.core;

import java.io.*;
import java.net.*;
import java.net.InetAddress;

public class Opp {
    static private Socket socket;
    static private DataInputStream is;
    static private DataOutputStream os;

    static private final int PORT = 3333;


    private static int[] data = new int[52];
    public Opp()
    {
        init();
    }
    public Opp(String s)
    {
        try {
            socket = new Socket(s,PORT);
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {}
    }
    private static void init(){
        try {
            socket = new Socket(InetAddress.getLocalHost().getHostAddress(),PORT);
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {}
    }
    public static void send(int[] data){
        try{
            for(int i=0;i<53;++i)
            os.writeInt(data[i]);
        }catch(Exception e){e.printStackTrace();}
    }
    public static int[] receive(){
        try {
            for(int i=0;i<52;++i)data[i]=is.readInt();
            return data;
        } catch (IOException e){e.printStackTrace();return null;}
    }

    public static void close(){
        try{
            is.close();
            os.close();
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static Socket getSocket()
    {
        return socket;
    }
    public static void sendOne(int d)
    {
        try{
            os.writeInt( d );
        }catch (IOException e){e.printStackTrace();}
    }
    public static int readOne()
    {
        int s =0;
        try{
             s=is.readInt();
        }catch (IOException e){e.printStackTrace();}
        //System.out.println( s );
        return s;
    }
}