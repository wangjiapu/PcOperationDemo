package thread

import Utils.HandleUtil
import Utils.IntConvertUtils
import beans.FileCommand
import beans.FileDescribe
import beans.ProtocolField
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.LinkedBlockingDeque

class FileOutPutThread:Thread() {

    private val fileoutQueue=LinkedBlockingDeque<ByteArray>(1024)

    override fun run() {
        try {
            while (!Thread.interrupted()){
                handle()
            }
        }catch (e:InterruptedException){
            println("FileOutputThread is dead!!!1")
        }


    }


    private fun handle() {
        val data=fileoutQueue.pop()
        val bytesize= byteArrayOf(data[1],data[2])
        when(data[0]){
            ProtocolField.fileSend->{
                val content=String(data,2,IntConvertUtils.getShortByByteArray(bytesize).toInt())
                val command = HandleUtil.gson.fromJson(content, FileCommand::class.java)

                fileOperation(command.describe, content)

            }
            ProtocolField.fileSendOK-> {

            }
        }
    }

    private fun fileOperation(describes: Array<out FileDescribe>?, content: String) {
        pos.write(HandleUtil.splicing(ProtocolField.fileSendOK,content))
        pos.flush()
        // sendMsg(Parameter.FILE_READY + "_" + jsonSrc + "_" + Parameter.END_FLAG)
        for (describe in describes!!) {
            val fileName = describe.getFileName() + "." + describe.getFileType()
            val fileSize = describe.getFileSize()
            var count = 0
            var size: Long = 0
            val file = File(fileName)
            var outputStream: FileOutputStream? = null
            var inputStream: BufferedInputStream? = null

            try {
                val bytes = ByteArray(4096)
                outputStream = FileOutputStream(file)
                inputStream = BufferedInputStream(connSocket.getInputStream())
                while ((count = inputStream.read(bytes)) != -1) {
                    println("count is " + count)
                    outputStream!!.write(bytes, 0, count)
                    outputStream.flush()
                    size += count.toLong()
                    if (size >= fileSize)
                        break
                }
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

    public fun addMessage(b:ByteArray){
        fileoutQueue.putLast(b)
    }

}