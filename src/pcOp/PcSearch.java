package pcOp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/*public class PcSearch {
    public static void main(String[] args){
        ps p=new ps();
        p.search("C:\\android_pg\\FlashRemote","test");
       // p.search("for /r C:\\android_pg\\FlashRemote %i in (*test*) do @echo %i");
    }
}*/

/**
 * 参数：path:路径；something:查找的东西;
 * 返回值：一个list集合用来存储返回查询到的结果，结果为空则为未找到；
 *
 */
public class PcSearch{
    private static final String ROOT="cmd /c for /r ";
    private static final String TERM1=" %i in (*";
    private static final String TERM2="*) do @echo %i";
    private static final String defaultPath="C：";
    private static final String CMD="cmd /c  start www.baidu.com/s?wd=";

    public List<String> search(String something){
       return search(defaultPath,something);
    }

    public List<String> search(String ptah,String something){
        List<String> result=null;
        try {
            result=new ArrayList<>();
           Process process= Runtime.getRuntime()
                   .exec(ROOT+ptah+TERM1+something+TERM2);
            BufferedReader bufferedReader1   =   new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "gbk"));
            String ls_1=null;
            StringBuilder stringBuilder=new StringBuilder();
            /**
             * 构造下更好
             */
            while ((ls_1=bufferedReader1.readLine()) != null)
                result.add(ls_1);

            bufferedReader1.close();
           process.destroy();
           System.out.println(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }


    public void voiceSearch(String s){
        Runtime runtime=Runtime.getRuntime();
        try {
            runtime.exec(CMD+s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
