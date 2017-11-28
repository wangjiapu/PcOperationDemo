package pcMain;


import pcOp.PcLuminance;
import pcOp.PcVolume;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> list=new ArrayList<>();
        for (int i=0;i<5;i++){
            list.add(""+i);
        }
        Iterator it = list.iterator();
        int index = 0;
        while (it.hasNext())
        {
            Object obj = it.next();
            if (needDelete(obj))  //needDelete返回boolean，决定是否要删除
            {
                //todo delete
                System.out.println("111");
                it.remove();
            }
            index ++;
        }

        for (int i=0;i<list.size()-1;i++){
            System.out.println(list.get(i));
        }
    }

    private static boolean needDelete(Object obj) {
        return true;
    }

    public static void changeStr(String str)
    {
        str = "welcome";
    }
}

interface A{
    static void getA(){}
}
class B implements A{
    public static void getA(){

    }
}
