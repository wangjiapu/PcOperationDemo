package pcMain;

import beans.Content;

import java.io.BufferedReader;
import java.io.IOException;

public class StringUtils {
    public static boolean startAndEnd(String string){
        return  (string.startsWith(Parameter.ONLINE_SUCCESS) && string.endsWith(Parameter.END_FLAG));
    }

    public static Content getContent(String s){
        int head=s.indexOf("_");
        int tail=s.lastIndexOf("_");
        Content content=new Content();
        content.setHead(s.substring(0,head));
        content.setContent(s.substring(head+1,tail));
        content.setTail(s.substring(tail+1,s.length()));
        return content;
    }
}
