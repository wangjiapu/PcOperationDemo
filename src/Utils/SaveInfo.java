package Utils;

import pcMain.Parameter;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.security.*;

public class SaveInfo {
    private static final String filepath="consimitor.txt";
    private final static String DES = "DES";
    private final static String ENCODE = "GBK";
    private final static String defaultKey = Parameter.OK;
    public  static void save(String name,String pwd){
        String content= null;
        try {
            content = name+"\n"+encrypt(pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file=new File(filepath);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeFileContent(content);
    }

    private  static void writeFileContent(String content) {

        try {
            BufferedWriter writer=new BufferedWriter(new FileWriter(filepath));
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String[] getinfo(){
        File file=new File(filepath);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       return readFileContent();
    }

    private  static String[] readFileContent() {
      String[] info=new String[2];
        BufferedReader reader = null;
        try {
            reader=new BufferedReader(new FileReader(filepath));
            String line=null;
            int i=0;
            while ((line=reader.readLine())!=null){
                info[i]=line;
                i++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            info[1]=decrypt(info[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }



    public static String encrypt(String data) throws Exception {
        byte[] bt = encrypt(data.getBytes(ENCODE), defaultKey.getBytes(ENCODE));
        String strs = new BASE64Encoder().encode(bt);
        return strs;
    }


    public static String decrypt(String data) throws IOException, Exception {
        if (data == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf, defaultKey.getBytes(ENCODE));
        return new String(bt, ENCODE);
    }

    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }
}
