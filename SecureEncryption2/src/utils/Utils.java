package utils;

public class Utils {

    public static byte byteWrapAroundShift(byte value,int shift){

        int shiftedByte = value + 128 + shift;
        int remainder=Math.abs(shiftedByte)%255;
        byte b;
        if(remainder < 0)b = (byte)(remainder + 255);
        else b = (byte)(remainder-128);

        return b;
    }
}
