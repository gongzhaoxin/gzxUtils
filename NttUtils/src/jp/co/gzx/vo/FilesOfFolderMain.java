package jp.co.gzx.vo;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * 
 * 
 */
public class FilesOfFolderMain {

  public static void main(String[] args) {
    // 递归取到所有的文件进行转换
    //findAllFiles("C:\\jip\\NNN-Lota-src-20150323\\noncore-common");
    findAllFiles("C:\\jip\\NNN-Lota-src-20150323\\subif-cf");

  }

  /**
   * 遍历文件夹下的所有文件（递归）
   * 
   * @param path
   *          -- 文件目录
   * @return
   * @throws IOException
   */
  public static boolean findAllFiles(String path) {
    File ftmp = new File(path);
    if (!ftmp.exists()) {
      info("转换文件路径错误！");
      return false;
    }

    if (ftmp.isFile()) {
      // .svn目录下面的svn管理文件不作为处理对象
      if (path.contains(".svn") || path.contains(".class") || path.contains("/target/") || path.contains(".gitkeep")) {
        return false;
      }
      // 真正的处理方法
      if(!FileUtils.isUTF8(path)){
        info("{0}:{1}", path, "");
      }else{
        info("{0}:{1}", path, "UTF-8");
      }
      

    } else {
      // 查找目录下面的所有文件与文件夹
      File[] childFiles = ftmp.listFiles();
      for (int i = 0, n = childFiles.length; i < n; i++) {
        File child = childFiles[i];
        String childPath = path + "/" + child.getName();

        findAllFiles(childPath);
      }
    }
    return true;
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