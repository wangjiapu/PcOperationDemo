package thread

import Utils.HandleUtil
import Utils.IntConvertUtils
import beans.FileDescribe
import beans.PackByteArray

import beans.ProtocolField
import java.io.*
import java.util.*
import java.util.concurrent.LinkedBlockingDeque
import kotlin.collections.ArrayList

class FileInputThread:Thread(){

    private var FILE_GET=false  //接受文件标志位

    private val mFileQueue = LinkedList<File>()
    private val inputQueue = LinkedBlockingDeque<PackByteArray>(1024)

    private val out = OutputThread

    override fun run() {


        try {
            while (!Thread.interrupted()){
                handle()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun handle(){
        val pba=inputQueue.pop()
        val type = pba.flag
        when (type) {
            ProtocolField.fileGet -> {
                val filedescibe=pba.body.toString()
                val list = ArrayList<FileDescribe>()
                val fileInfos = HandleUtil.gson.fromJson(filedescibe, Array<beans.FileInfo>::class.java)
                for (fileInfo in fileInfos) {
                    val file = File(fileInfo.getPath())
                    val describe = FileDescribe()
                    describe.fileSize = file.length()
                    val name = file.getName().substring(0, file.getName().lastIndexOf("."))
                    val type = file.getName().substring(file.getName().lastIndexOf(".") + 1,
                            file.getName().length)
                    describe.fileName = name
                    describe.fileType = type
                    list.add(describe)
                    mFileQueue.add(File(fileInfo.path))
                }

                val bytes=HandleUtil.gson.toJson(list).toByteArray()
                val len=IntConvertUtils.getShortByByteArray(bytes)
                val pack=PackByteArray(ProtocolField.fileGetOK, len,bytes)
                out.addMessage(pack)
                FILE_GET=true
            }

            ProtocolField.fileGetHead->{
                if (FILE_GET){
                   sendFile()
                }
            }
            ProtocolField.fileGetEnd ->{
                FILE_GET=false
            }

        }

    }


    private fun sendFile() {

        while (mFileQueue.isEmpty()) {
            var count = 0
            val inputStream = FileInputStream(mFileQueue.poll())
            while (true) {
                val bytes = ByteArray(1020)
                count = inputStream.read(bytes)
                if (count == -1)
                    break
                if (count == 1020){
                    val pack=PackByteArray(ProtocolField.fileGetBody,
                            IntConvertUtils.getShortByByteArray(bytes), bytes)
                    out.addMessage(pack)
                }
                else {
                    val newBytes = ByteArray(count)
                    var i = 0
                    i = 0
                    while (i < count) {
                        newBytes[i] = bytes[i]
                        i++
                    }
                    val pack=PackByteArray(ProtocolField.fileGetBody,
                            IntConvertUtils.getShortByByteArray(newBytes), newBytes)
                    out.addMessage(pack)
                }
            }
            inputStream.close()
        }
        val pack=PackByteArray(ProtocolField.fileGetEnd,0,null)
        out.addMessage(pack)
    }


    public fun addMessage(byteArray: PackByteArray) {
        inputQueue.putLast(byteArray)
    }

}