package thread

import Utils.HandleUtil
import beans.FileCommand
import beans.FileDescribe
import beans.PackByteArray
import beans.ProtocolField
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.LinkedBlockingDeque



class FileOutPutThread:Thread() {

    private val fileoutQueue=LinkedBlockingDeque<PackByteArray>(1024)

    private val outthread=OutputThread

    private var FILE_SEND=false

    private var describes: Array<out FileDescribe> ?=null
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
        val data=fileoutQueue.pop()
        when(data.flag){
            ProtocolField.fileSend->{
                val content=data.body.toString()
                val command = HandleUtil.gson.fromJson(content, FileCommand::class.java)
                 describes = command.describe
                val pack=PackByteArray(ProtocolField.fileSendOK,data.len,data.body)

                outthread.addMessage(pack)
                FILE_SEND =true
            }
            ProtocolField.fileSendHead -> {
                if (FILE_SEND){
                    fileOperation(data)
                }
            }
            ProtocolField.cmdScreenBody->{

            }
            ProtocolField.fileSendEnd->{
                val pack=PackByteArray(ProtocolField.fileSendEnd,0,null)
                outthread.addMessage(pack)
                FILE_SEND=false
            }
        }
    }

    private fun fileOperation(data:PackByteArray) {
        for (describe in describes!!) {
            val fileName = describe.getFileName() + "." + describe.getFileType()
            val file = File(fileName)
            var outputStream: FileOutputStream? = null

            try {
                outputStream = FileOutputStream(file)
                outputStream.write(data.body)

            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    if (outputStream != null)
                        outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

     fun addMessage(b:PackByteArray){
        fileoutQueue.putLast(b)
    }

}