package com.jenkov.nioserver.tansmit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by jjenkov on 19-10-2015.
 */
public class TansmitUtil {

    private static final byte[] GET    = new byte[]{'G','E','T'};
    private static final byte[] POST   = new byte[]{'P','O','S','T'};
    private static final byte[] PUT    = new byte[]{'P','U','T'};
    private static final byte[] HEAD   = new byte[]{'H','E','A','D'};
    private static final byte[] DELETE = new byte[]{'D','E','L','E','T','E'};

    private static final byte[] HOST           = new byte[]{'H','o','s','t'};
    private static final byte[] CONTENT_LENGTH = new byte[]{'C','o','n','t','e','n','t','-','L','e','n','g','t','h'};

    public static int parseTransmitRequest(byte[] src, int startIndex, int endIndex, TransmitHeaders httpHeaders){

    	//System.out.println("rcvByte = " + (src != null ? DatatypeConverter.printHexBinary(src, startIndex, endIndex) : "empty"));

    	//byte[] des = new byte[endIndex - startIndex];
    	//System.arraycopy(src, startIndex, des, 0, endIndex - startIndex);
		//System.out.println("src size = " + (src != null ? src.length : -1) + "--> startIndex = " + startIndex + ", endIndex = " + endIndex/* + "\r\n" + new String(des, "UTF-8")*/);
    	int endOfFirstLine = findNextLineBreak(src, startIndex, endIndex);
    	if (endOfFirstLine != -1) {
    		return endOfFirstLine;
    	}

       return -1;
    }

    private static void findContentLength(byte[] src, int startIndex, int endIndex, TransmitHeaders httpHeaders) throws UnsupportedEncodingException {
        int indexOfColon = findNext(src, startIndex, endIndex, (byte) ':');

        //skip spaces after colon
        int index = indexOfColon +1;
        while(src[index] == ' '){
            index++;
        }

        int valueStartIndex = index;
        int valueEndIndex   = index;
        boolean endOfValueFound = false;

        while(index < endIndex && !endOfValueFound){
            switch(src[index]){
                case '0' : ;
                case '1' : ;
                case '2' : ;
                case '3' : ;
                case '4' : ;
                case '5' : ;
                case '6' : ;
                case '7' : ;
                case '8' : ;
                case '9' : { index++;  break; }

                default: {
                    endOfValueFound = true;
                    valueEndIndex = index;
                }
            }
        }

        httpHeaders.contentLength = Integer.parseInt(new String(src, valueStartIndex, valueEndIndex - valueStartIndex, "UTF-8"));

    }


    public static int findNext(byte[] src, int startIndex, int endIndex, byte value){
        for(int index = startIndex; index < endIndex; index++){
            if(src[index] == value) return index;
        }
        return -1;
    }

    public static int findNextLineBreak(byte[] src, int startIndex, int endIndex) {
        for(int index = startIndex; index < endIndex; index++){
            if(src[index] == '\n'){
            	System.out.println("src n index = " + index);
                if(src[index - 1] == '\r'){
                	System.out.println("src r index = " + (index - 1));
                    return index;
                }
                return index;
            };
        }
        System.out.println("src index = -1");
        return -1;
    }

    public static void resolveHttpMethod(byte[] src, int startIndex, TransmitHeaders httpHeaders){
        if(matches(src, startIndex, GET)) {
            httpHeaders.httpMethod = TransmitHeaders.HTTP_METHOD_GET;
            return;
        }
        if(matches(src, startIndex, POST)){
            httpHeaders.httpMethod = TransmitHeaders.HTTP_METHOD_POST;
            return;
        }
        if(matches(src, startIndex, PUT)){
            httpHeaders.httpMethod = TransmitHeaders.HTTP_METHOD_PUT;
            return;
        }
        if(matches(src, startIndex, HEAD)){
            httpHeaders.httpMethod = TransmitHeaders.HTTP_METHOD_HEAD;
            return;
        }
        if(matches(src, startIndex, DELETE)){
            httpHeaders.httpMethod = TransmitHeaders.HTTP_METHOD_DELETE;
            return;
        }
    }

    public static boolean matches(byte[] src, int offset, byte[] value){
        for(int i=offset, n=0; n < value.length; i++, n++){
            if(src[i] != value[n]) return false;
        }
        return true;
    }
}
