package pcMain;

import beans.Content;

import java.io.BufferedReader;
import java.io.IOException;

public class StringUtils {
    public static boolean startAndEnd(String string){
        return  (string.startsWith(Parameter.ONLINE_SUCCESS) && string.endsWith(Parameter.END_FLAG));
    }

    public static boolean isBind(String s){
        return s.startsWith(Parameter.CONNECTED_SUCCESS)&&s.endsWith(Parameter.END_FLAG);
    }

    /**
     *  读取流中的字符
     * @param reader
     * @return
     */

    public static String readLine(BufferedReader reader) {
        StringBuilder sb = new StringBuilder();
        String temp = "";
        try {
            while (!(temp = reader.readLine()).endsWith(Parameter.END_FLAG)) {
                sb.append(temp);
            }
            sb.append(temp);
        } catch (IOException e) {
            System.out.println("读取数据失败。。。");
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 字符串结尾加 end_flag结尾标志
     * @param s
     * @return
     */
    public static String addEnd_flag2Str(String s){
        return s+"_"+Parameter.END_FLAG;
    }

/*public static String rmEnd_flagstr(String s){
    int endflagindex=s.lastIndexOf("_");
    return s.substring(0,endflagindex);
}*/

    public static Content getContent(String s){
        int head=s.indexOf("_");
        int tail=s.lastIndexOf("_");
        Content content=new Content();
        content.setHead(s.substring(0,head));
        System.out.println(content.getHead());
        content.setContent(s.substring(head+1,tail));
        System.out.println(content.getContent());
        content.setTail(s.substring(tail+1,s.length()));
        System.out.println(content.getTail());
        return content;
    }
}
