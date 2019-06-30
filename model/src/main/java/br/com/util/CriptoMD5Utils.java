package br.com.util;

import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class CriptoMD5Utils
{

    public CriptoMD5Utils()
    {
    }

    public static String getStringMD5(String str)
    {
        if(str == null || str.equals(""))
            str = "null";
        String strMD5 = "";
        try
        {
            byte b[] = digest(str.getBytes(), "MD5");
            strMD5 = byteArrayToHexString(b).toUpperCase();
        }
        catch(NoSuchAlgorithmException ex)
        {
            ex.printStackTrace();
        }
        return strMD5;
    }

    private static String byteArrayToHexString(byte b[])
    {
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < b.length; i++)
        {
            int j = b[i] & 0xff;
            buf.append("0123456789abcdef".charAt(j / 16));
            buf.append("0123456789abcdef".charAt(j % 16));
        }

        return buf.toString();
    }

    private static byte[] digest(byte input[], String algoritmo)
        throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance(algoritmo);
        md.reset();
        return md.digest(input);
    }

    private static byte[] hexStringToByteArray(String hexa)
        throws IllegalArgumentException
    {
        if(hexa.length() % 2 != 0)
            throw new IllegalArgumentException("String hexa inv\uFFFDlida");
        byte b[] = new byte[hexa.length() / 2];
        for(int i = 0; i < hexa.length(); i += 2)
            b[i / 2] = (byte)("0123456789abcdef".indexOf(hexa.charAt(i)) << 4 | "0123456789abcdef".indexOf(hexa.charAt(i + 1)));

        return b;
    }

    public static void main(String args[])
    {
        System.out.println("Criptografando \"2000\"");
        String b = getStringMD5("112233");
        System.out.println(b);
    }

    private static final String hexDigits = "0123456789abcdef";
}