package com.fitzgerald.simulator.processor;

import java.nio.ByteBuffer;

public class Util {
    
    public static Integer bytesToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(bytes);
        return buffer.getInt(0);
    }
    
    public static byte[] intToBytes(Integer integer) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(integer);
        buffer.position(0);
        
        byte[] bytes = new byte[4];
        buffer.get(bytes);
        
        return bytes;
    }
    
    public static void main(String[] args) {
        byte[] bytes = intToBytes(243634);
        Integer test1 = bytesToInt(bytes);
        System.out.println(test1);
    }
    
}