package thread

import Utils.HandleUtil
import Utils.IntConvertUtils
import beans.FileCommand
import beans.FileDescribe
import beans.PackByteArray
import beans.ProtocolField
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.concurrent.LinkedBlockingDeque



class FileOutPutThread:Thread() {

    private val fileOutQueue=LinkedBlockingDeque<PackByteArray>(1024)

    private var key:Short=0
    private val fileStreamMap = HashMap<Short,OutputStream>()//file writer map

    private val outThread=OutputThread


    fun addMessage(b:PackByteArray){
        fileOutQueue.putLast(b)
    }

    override fun run() {
        try {
            while (!Thread.interrupted()){
                handle()
            }
        }catch (e:InterruptedException){
            println("FileOutputThread is dead!!!")
        }

    }


    private fun handle() {
        val data=fileOutQueue.pop()
        when(data.flag){
            ProtocolField.fileSend->{
                val content=data.body.toString()
                val command = HandleUtil.gson.fromJson(content, FileCommand::class.java)
                val result=register2Map(command.describe)
                if (result != null){
                    val resultBytes=result.toByteArray(Charset.forName("UTF-8"))
                    val pack=PackByteArray(ProtocolField.fileSendOK,
                            IntConvertUtils.getShortByByteArray(resultBytes),resultBytes)
                    outThread.addMessage(pack)
                }
            }

            ProtocolField.cmdScreenBody->{
                fileOperation(data)
            }
            ProtocolField.fileSendEnd->{
                try{
                    val fileFlag=IntConvertUtils
                            .getShortByByteArray(byteArrayOf(data.body!![0],data.body[1]))
                    val output=fileStreamMap[fileFlag]
                    output!!.close()
                    fileStreamMap.remove(fileFlag)
                }catch (e:Exception){
                    println("filesendEnd:${e.message}")
                }
                if (fileStreamMap.isEmpty()){
                    key=0
                }
            }
        }
    }

    //register map
    private fun register2Map(describes: Array<out FileDescribe>?):String? {
        val map=HashMap<String,Short>()
        if (describes!=null){
            for (describe in describes){
                val fileName=describe.fileName+"."+describe.fileType
                val file=File(fileName)
                val outputStream=FileOutputStream(file)
                fileStreamMap.put(key,outputStream)
                map.put(fileName,key++)
            }
            return HandleUtil.gson.toJson(map)
        }
       return null
    }

    /**
     * storage file content to corresponding fileItem
     */
    private fun fileOperation(data:PackByteArray) {
        val fileFlag= IntConvertUtils
                .getShortByByteArray(byteArrayOf(data.body!![0],data.body[1]))

        val output=fileStreamMap[fileFlag]
        try {
            output!!.write(data.body)
        }catch (e:Exception){
            println("fileOperation outputException:${e.message}")
        }
    }
}