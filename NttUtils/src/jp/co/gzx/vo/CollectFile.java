package jp.co.gzx.vo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

/**
 * 批量把文件编码由GBK转为UTF8，可以继续完善做成在命令行中执行的程序， 可以添加文件名过滤等功能，暂时未实现。
 * 
 */
public class CollectFile {

  public static String basePath;

  /**
   * 把一个目录中的文件都放到一个文件中
   * 
   * @param fromPath
   *          -- 来源文件目录
   * @param toPath
   *          -- 目标文件目录
   * @return
   */
  public static boolean collectFile(String fromPath, String toPath, FileOutputStream out) {
    File ftmp = new File(fromPath);
    if (!ftmp.exists()) {
      info("转换文件路径错误！");
      return false;
    }

    // 如果是文件，则转换，结束
    if (ftmp.isFile()) {
      byte[] value = fileToBytes(fromPath);
      String content = convEncoding(value, "shift-jis", "utf-8");

      try {
        out.write(content.getBytes("UTF-8"));
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    } else {
      // 一个文件夹下面的文件出力到一个文件
      // 处理文件路径
      File file = new File(basePath + "/" + getFileNameWithPath(toPath) + ".txt");

      if (file.exists()) {
        file.delete();
      }
      try {
        System.out.println(file.getName());
        file.createNewFile();
        out = new FileOutputStream(file);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      // 查找目录下面的所有文件与文件夹
      File[] childFiles = ftmp.listFiles();
      for (int i = 0, n = childFiles.length; i < n; i++) {
        File child = childFiles[i];
        String childFrom = fromPath + "/" + child.getName();
        String childTo = toPath + "/" + child.getName();

        collectFile(childFrom, childTo, out);
      }
    }

    try {
      out.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return true;
  }

  /**
   * 把文件内容保存到指定的文件中，如果指定的文件已存在，则先删除这个文件， 如果没有则创建一个新文件，文件内容采用UTF-8编码方式保存。
   * 如果指定的文件路径不存在，则先创建文件路径，文件路径从根目录开始创建。
   * 
   * @param fileName
   *          -- 文件路径
   * @param content
   *          -- 文件内容
   * @return
   */
  public static boolean saveFileUtf8(String fileName, String content) {
    if (fileName == null || fileName.length() == 0)
      return false;
    if (content == null)
      return false;

    // 路径中的\转换为/
    fileName = fileName.replace('\\', '/');
    // 处理文件路径
    createPath(fileName.substring(0, fileName.lastIndexOf('/')));

    File file = null;
    FileOutputStream out = null;
    try {
      // 创建或修改文件
      file = new File(fileName);

      if (file.exists()) {
        file.delete();
      } else {
        file.createNewFile();
      }

      out = new FileOutputStream(file);
      // 添加三个字节标识为UTF-8格式，也是BOM码
      // out.write(new byte[]{(byte)0xEF,(byte)0xBB,(byte)0xBF});
      out.write(content.getBytes("UTF-8"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return false;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    } finally {
      if (out != null) {
        try {
          out.flush();
          out.close();
        } catch (IOException e) {
          e.printStackTrace();
          return false;
        }
      }
    }

    return true;
  }

  /**
   * 把文件内容转换为字节数组输出。
   * 
   * @param fileName
   *          -- 文件名
   * @return
   */
  public static byte[] fileToBytes(String fileName) {
    FileInputStream ins = null;
    ByteArrayOutputStream bos = null;
    try {
      // 创建文件读入流
      ins = new FileInputStream(new File(fileName));
      // 创建目标输出流
      bos = new ByteArrayOutputStream();

      // 取流中的数据
      int len = 0;
      byte[] buf = new byte[65536];
      while ((len = ins.read(buf, 0, 65536)) > -1) {
        bos.write(buf, 0, len);
      }

      // 目标流转为字节数组返回到前台
      return bos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (ins != null) {
          ins.close();
          ins = null;
        }
        if (bos != null) {
          bos.close();
          bos = null;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return null;
  }

  /**
   * 检查指定的文件路径，如果文件路径不存在，则创建新的路径， 文件路径从根目录开始创建。
   * 
   * @param filePath
   * @return
   */
  public static boolean createPath(String filePath) {
    if (filePath == null || filePath.length() == 0)
      return false;

    // 路径中的\转换为/
    filePath = filePath.replace('\\', '/');
    // 处理文件路径
    String[] paths = filePath.split("/");

    // 处理文件名中没有的路径
    StringBuilder sbpath = new StringBuilder();
    for (int i = 0, n = paths.length; i < n; i++) {
      sbpath.append(paths[i]);
      // 检查文件路径如果没有则创建
      File ftmp = new File(sbpath.toString());
      if (!ftmp.exists()) {
        ftmp.mkdir();
      }

      sbpath.append("/");
    }

    return true;
  }

  /**
   * 取路径中的文件名
   * 
   * @param path
   *          -- 文件路径，含文件名
   * @return
   */
  public static String getFileNameWithPath(String path) {
    if (path == null || path.length() == 0)
      return "";

    basePath = basePath.replaceAll("\\\\", "_");
    path = path.replaceAll("\\\\", "_");
    
    path = path.replaceAll(basePath, "");

    return path;
  }

  /**
   * 取路径中的文件名
   * 
   * @param path
   *          -- 文件路径，含文件名
   * @return
   */
  public static String getFileName(String path) {
    if (path == null || path.length() == 0)
      return "";

    path = path.replaceAll("\\\\", "/");
    int last = path.lastIndexOf("/");

    if (last >= 0) {
      return path.substring(last + 1);
    } else {
      return path;
    }
  }

  /**
   * 字符串的编码格式转换
   * 
   * @param value
   *          -- 要转换的字符串
   * @param oldCharset
   *          -- 原编码格式
   * @param newCharset
   *          -- 新编码格式
   * @return
   */
  public static String convEncoding(byte[] value, String oldCharset, String newCharset) {
    OutputStreamWriter outWriter = null;
    ByteArrayInputStream byteIns = null;
    ByteArrayOutputStream byteOuts = new ByteArrayOutputStream();
    InputStreamReader inReader = null;

    char cbuf[] = new char[1024];
    int retVal = 0;
    try {
      byteIns = new ByteArrayInputStream(value);
      inReader = new InputStreamReader(byteIns);
      outWriter = new OutputStreamWriter(byteOuts, newCharset);
      while ((retVal = inReader.read(cbuf)) != -1) {
        outWriter.write(cbuf, 0, retVal);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (inReader != null)
          inReader.close();
        if (outWriter != null)
          outWriter.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    String temp = null;
    try {
      temp = new String(byteOuts.toByteArray(), newCharset);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    // System.out.println("temp" + temp);
    return temp;
  }

  /**
   * 显示提示信息
   * 
   * @param message
   *          -- 信息内容
   * @param params
   *          -- 参数
   */
  private static void info(String message, Object... params) {
    message = MessageFormat.format(message, params);

    System.out.println(message);
  }
}