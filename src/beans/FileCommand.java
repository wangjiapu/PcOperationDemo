package beans;

/**
 * Created by LZL on 2017/9/15.
 */

public class FileCommand<T>{
    private String path;
    private FileDescribe[] describe;


    public FileCommand(){}

    public FileCommand(String type, FileDescribe[] describe, boolean isBack) {
        this.path = type;
        this.describe = describe;
    }

    public String getType() {
        return path;
    }

    public void setType(String type) {
        this.path = type;
    }

    public FileDescribe[] getDescribe() {
        return describe;
    }

    public void setDescribe(FileDescribe[] describe) {
        this.describe = describe;
    }

}
