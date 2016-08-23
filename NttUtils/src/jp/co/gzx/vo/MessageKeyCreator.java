package jp.co.gzx.vo;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class MessageKeyCreator {
      /**
       * 
       */
      private static final String MESSAGE_FILE_PATH = "application-message.properties";
    
      /**
       * 
       */
      private static final String OUTPUT_FILE_PATH = "messageKeys.java";
      
      /**
       * 
       */
      private static final String MESSAGE_HEAD = "    public static final String ";
      
      /**
       * 
       */
      private static final String BACK_SLASH = "/";
      
      /**
       * 
       */
      private static final String STAR = "*";
      
      /**
       * 
       */
      private static final String CHANGE_LINE = "\n";
      
      /**
       * 
       */
      private static final String EQUALS = " = ";
      
      /**
       * 
       */
      private static final String QUOTE = "\"";
      
      /**
       * 
       */
      private static final String SEMICOLON = ";";
      
      /**
       * 
       */
      private static final String SPACE = " ";
      
      /**
       * ��؂���B
       * 
       * @throws FileNotFoundException
       */
      public void CreateSeagements() {
          FileWriter fw = null;
        try {
            
            Properties p = readMessageFile();
            fw = new FileWriter(OUTPUT_FILE_PATH);
            for (Map.Entry m : p.entrySet()) {
                if (m.getKey()!= null && m.getValue() != null) {
                    String content = createSeagement(m.getKey().toString(), m.getValue().toString());
                    fw.write(content);
                } else {
                    fw.write("ERROR HAPPENED");
                    fw.write(m.getKey().toString());
                    fw.write(CHANGE_LINE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fw = null;
        }
      }
      
      /**
       * �o�͂�Name�ǂނ���B
       * 
       * @return �o�͂�Names
�@      * @throws FileNotFoundException s
       */
      private Properties readMessageFile() throws FileNotFoundException{
          InputStream is =  MessageKeyCreator.class.getResourceAsStream("/" + MESSAGE_FILE_PATH);
          if (is == null) {
              return null;
          }
          Properties p = new Properties();
          try {
              p.load(is);
          } catch (IOException e) {
              return null;
          } finally {
              try {
                  if (is != null) {
                      is.close();
                  }
              } catch (IOException e) {
                  // ���ł��s��Ȃ��B
              }
          }
          return p;
      }
      
      /**
       * �s�����B
       */
      private String createSeagement(String key, String value){
          StringBuffer sb = new StringBuffer();
          
          //��s�ǉ����� �R�����g�s
          sb.append(SPACE).append(SPACE).append(SPACE).append(SPACE);
          sb.append(BACK_SLASH).append(STAR).append(STAR);
          sb.append(CHANGE_LINE);
          
          //����
          sb.append(SPACE).append(SPACE).append(SPACE).append(SPACE);
          sb.append(SPACE).append(STAR).append(SPACE);
          sb.append(value);
          sb.append(CHANGE_LINE);
          
          //���ߏI��
          sb.append(SPACE).append(SPACE).append(SPACE).append(SPACE);
          sb.append(SPACE).append(STAR).append(BACK_SLASH);
          sb.append(CHANGE_LINE);
          
          //�@���e�ǉ��B
          sb.append(MESSAGE_HEAD);
          sb.append(key.toUpperCase());
          sb.append(EQUALS);
          sb.append(QUOTE);
          sb.append(key);
          sb.append(QUOTE);
          sb.append(SEMICOLON);
          sb.append(CHANGE_LINE);
          sb.append(CHANGE_LINE);
          return sb.toString();
      }
      
      public static void main(String[] args) {
          MessageKeyCreator mk = new MessageKeyCreator();
          mk.CreateSeagements();
      }
}
