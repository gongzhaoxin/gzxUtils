package jp.co.gzx.vo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
  public FileUtils() {
  }

  // 根据文件路径返回编码格式
  public static String getFileEncode(String path) {
    java.io.RandomAccessFile raf = null;
    String encode = "";
    try {
      raf = new java.io.RandomAccessFile(path, "r");
      raf.seek(0);
      int flag1 = 0;
      int flag2 = 0;
      int flag3 = 0;
      if (raf.length() >= 2) {
        flag1 = raf.readUnsignedByte();
        flag2 = raf.readUnsignedByte();
      }
      if (raf.length() >= 3) {
        flag3 = raf.readUnsignedByte();
      }
      encode = getEncode(flag1, flag2, flag3);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      if (raf != null)
        try {
          raf.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
    }
    return encode;
  }

  private static String getEncode(int flag1, int flag2, int flag3) {
    String encode = (char) (flag1) + "/" + (char) (flag2) + "/" + (char) (flag3);
    // txt文件的开头会多出几个字节，分别是FF、FE（Unicode）,
    // FE、FF（Unicode big endian）,EF、BB、BF（UTF-8）

    if (flag1 == 255 && flag2 == 254) {
      encode = "Unicode";
    } else if (flag1 == 254 && flag2 == 255) {
      encode = "UTF-16";
    } else if (flag1 == 239 && flag2 == 187 && flag3 == 191) {
      encode = "UTF-8";
    }

    return encode;
  }

  // /根据文件路径直接返回文件流
  public static InputStreamReader getInputStreamReader(String path) throws java.io.IOException {
    java.io.InputStreamReader isr = null;
    String encode = getFileEncode(path);
    if (encode.equals("")) {
      isr = new java.io.InputStreamReader(

      new java.io.FileInputStream(path));
    } else {
      isr = new java.io.InputStreamReader(

      new java.io.FileInputStream(path), encode);
    }

    return isr;
  }

  // 根据文件路径返回编码格式
  public static boolean isUTF8(String path) {
    java.io.FileInputStream fis = null;
    try {
      fis = new java.io.FileInputStream(path);
    } catch (FileNotFoundException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    BufferedInputStream bds = new BufferedInputStream(fis);
    byte[] b1 = new byte[1000000];
    try {
      bds.read(b1);
      return isUTF8(b1);

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }

  /**
   * UTF-8编码格式判断
   * UTF-8编码规范 及如何判断文本是UTF-8编码的
   * UTF-8的编码规则很简单，只有二条：
   * 1）对于单字节的符号，字节的第一位设为0，后面7位为这个符号的unicode码。因此对于英语字母，UTF-8编码和ASCII码是相同的。
   * 2）对于n字节的符号（n>1），第一个字节的前n位都设为1，第n+1位设为0，后面字节的前两位一律设为10。剩下的没有提及的二进制位，全部为这个符号的unicode码。
   * 根据以上说明 下面给出一段java代码判断UTF-8格式
   * 
   * @param rawtext
   *          需要分析的数据
   * @return 是否为UTF-8编码格式
   */
  public static boolean isUTF8(byte[] rawtext) {
    int score = 0;
    int i, rawtextlen = 0;
    int goodbytes = 0, asciibytes = 0;
    // Maybe also use UTF8 Byte Order Mark: EF BB BF
    // Check to see if characters fit into acceptable ranges
    rawtextlen = rawtext.length;
    for (i = 0; i < rawtextlen; i++) {
      if ((rawtext[i] & (byte) 0x7F) == rawtext[i]) {
        // 最高位是0的ASCII字符
        asciibytes++;
        // Ignore ASCII, can throw off count
      } else if (-64 <= rawtext[i] && rawtext[i] <= -33
      // -0x40~-0x21
          && // Two bytes
          i + 1 < rawtextlen && -128 <= rawtext[i + 1] && rawtext[i + 1] <= -65) {
        goodbytes += 2;
        i++;
      } else if (-32 <= rawtext[i] && rawtext[i] <= -17 && // Three bytes
          i + 2 < rawtextlen && -128 <= rawtext[i + 1] && rawtext[i + 1] <= -65 && -128 <= rawtext[i + 2] && rawtext[i + 2] <= -65) {
        goodbytes += 3;
        i += 2;
      }
    }
    if (asciibytes == rawtextlen) {
      return false;
    }
    score = 100 * goodbytes / (rawtextlen - asciibytes);
    // If not above 98, reduce to zero to prevent coincidental matches
    // Allows for some (few) bad formed sequences
    if (score > 98) {
      return true;
    } else if (score > 95 && goodbytes > 30) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 判断文件的编码格式
   * 
   * @param fileName
   *          :file
   * @return 文件编码格式
   * @throws Exception
   */
  public static String codeString(String fileName) {
    BufferedInputStream bin = null;
    try {
      bin = new BufferedInputStream(new FileInputStream(fileName));
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    int p = 0;
    try {
      p = (bin.read() << 8) + bin.read();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String code = null;

    switch (p) {
      case 0xefbb:
        code = "UTF-8";
        break;
      case 0xfffe:
        code = "Unicode";
        break;
      case 0xfeff:
        code = "UTF-16BE";
        break;
      default:
        code = "GBK";
    }

    return code;
  }

}
