package com.example.util.base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import android.util.Base64;

public class Base64Util {
	public static String fileToBase64(File file) {
	    String base64 = null;
	    InputStream in = null;
	    try {
	        in = new FileInputStream(file);
	        byte[] bytes = new byte[in.available()];
	        int length = in.read(bytes);
	        base64 = safeUrlBase64Encode(bytes);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            in.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return base64;
	}
	
	public static String safeUrlBase64Encode(byte[] data){
		 String encodeBase64 = new BASE64Encoder().encode(data);
		 String safeBase64Str = encodeBase64.replace('+', '-');
		 safeBase64Str = safeBase64Str.replace('/', '_');
		 safeBase64Str = safeBase64Str.replaceAll("=", "");
		 return safeBase64Str;
		}
	
	public static byte[] safeUrlBase64Decode(final String safeBase64Str) throws IOException{
		 String base64Str = safeBase64Str.replace('-', '+');
		 base64Str = base64Str.replace('_', '/');
		 int mod4 = base64Str.length()%4;
		 if(mod4 > 0){
		 base64Str = base64Str + "====".substring(0, mod4);
		 }
		 return new BASE64Decoder().decodeBuffer(base64Str);
		}
}
