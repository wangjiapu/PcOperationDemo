package pcMain;

import beans.Command;
import beans.DiskInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import pcOp.*;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class OpenService {
    private static Socket connSocket;
    private static PrintWriter writer;
    private static OutputStream w;
    private static BufferedReader reader;
    private static Gson gson=new Gson();

    public static void main(String[] args) {

        try {
            connSocket = new Socket(Parameter.SERVER_IP, 10087);
            OutputStream outputStream = connSocket.getOutputStream();
            InputStream inputStream = connSocket.getInputStream();

            w=connSocket.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream));
            reader = new BufferedReader(new InputStreamReader(inputStream));
            //向服务端发送消息，我要上线
            String username = System.getProperty("user.name");
            System.out.println(username);
            writer.println("|ONLINE|_" + "lzl471954654" + "_Test_" + Parameter.END_FLAG);
            writer.flush();
            String result = StringUtils.readLine(reader);
            System.out.println(result);
            if (StringUtils.startAndEnd(result)) {

                /**
                 * 下线
                 * 时间限制
                 * 离线
                 */

                loop();
            } else {  //上线失败
                System.out.println("上线失败，请重新上线。。。");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void loop() {
        while (true) {
            //持续不断的读取服务端消息
            String op = StringUtils.readLine(reader);
            System.out.println(op);
            String[] index=op.split("_");
            Command command=gson.fromJson(index[1], Command.class);
            operation(command);
        }
    }

    /**
     * 对电脑的操作
     */
    private static void operation(Command command) {
        int type=Integer.valueOf(command.getType());
        switch (type) {
            case -1:
                send2service(writer);
                break;
            case 0://关机
                PcTools pcTools = new PcTools();
                pcTools.opencmd(21);
               // send2service(writer);
                break;
            case 1://取消关机
                PcTools pcTools1=new PcTools();
                pcTools1.opencmd(23);
                break;

            case 2:
                PcScreen pcScreen=new PcScreen(command.getDescribe());
                if (command.getIsBack()){
                    pcScreen.sendScreen(command.getDescribe(), w);
                }else {
                    pcScreen.shot();
                }
                break;

            case 4://获取磁盘详细信息
                PcDisk pcDisk=new PcDisk();
                String diskString=gson.toJson(pcDisk.getDisk());
                send2service(writer,diskString);
                break;
            case 7:
                PcSearch pcSearch=new PcSearch();
                pcSearch.voiceSearch(command.getDescribe());
                break;
            default:
                PcTools tool=new PcTools();
                if (type<127){
                    tool.opencmd(type%100);
                }else{
                    tool.closecmd(type%200);
                }

        }
    }


    private static void send2service(PrintWriter writer) {
       send2service(writer,Parameter.OK);
    }

    private static void send2service(PrintWriter writer,String s) {
        writer.println(StringUtils.addEnd_flag2Str(s));
        writer.flush();
    }
}
