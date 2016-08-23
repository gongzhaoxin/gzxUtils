package jp.co.gzx.vo;



/**
 * 批量把文件编码由GBK转为UTF8，可以继续完善做成在命令行中执行的程序， 可以添加文件名过滤等功能，暂时未实现。
 * 
 */
public class SumFile {

  public static void main(String[] args) {

    String path ="D:\\NNN\\SVN\\09.製造（Code）\\本開発\\06.残高管理\\DB環境管理\\DDL";
    // 需要转换的文件目录
    String fromPath = path + "\\20150526";
    // 转换到指定的文件目录

    String toPathSum = path + "\\20150526_SUM1";

    CopyOfJIS2UTF.sumFile(fromPath, toPathSum, null);
  }

}