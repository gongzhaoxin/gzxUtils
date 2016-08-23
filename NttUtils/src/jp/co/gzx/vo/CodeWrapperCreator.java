/*
 * @(#)CodeWrapperCreator.java
 *
 * Copyright (c) 2007 NTT DATA Corporation.
 */
package jp.or.ktdk.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * 
 * @author huzl
 * @version 1.0 2007/10/17
 */
public class CodeWrapperCreator {

    /**
     * 設定ファイル
     */
    private String propFile;
    
    /**
     * 生成クラスファイル
     */
    private String clsFile;
    
    private FileWriter fileWriter;
    
    private StringBuffer fieldBuffer;
    
    private StringBuffer methodBuffer;
    
    /**
     * コンストラクタ
     */
    public CodeWrapperCreator(String propFile, String clsFile) throws IOException {
        this.propFile = propFile;
        fileWriter = new FileWriter(clsFile);

        fieldBuffer = new StringBuffer();
        methodBuffer = new StringBuffer();
    }

    /**
     * クラスファイルを作成する。
     */
    public void createClass() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(propFile));
        String dataLine = reader.readLine();
        while (dataLine != null) {
            
            if (dataLine.trim().length() > 0) {
                String[] items = dataLine.split(",");
                
                String name = items[0];
                String codeName = items[1];
                String methodName = items[2];
                String methodParam = items[3];
                String codeValue = items[4];
                
                appendField(name, codeName, codeValue);
                appendMethod(name, codeName, methodName, methodParam, codeValue);
            }
            dataLine = reader.readLine();
            
        }
        
        fileWriter.write(fieldBuffer.toString());
        
        appendConstructor();
        
        fileWriter.write(methodBuffer.toString());
        
        appendOthers();
        
        
        fileWriter.close();
        
    }
    
    /**
     * 
     * @param name
     * @param codeName
     * @param codeValue
     */
    private void appendField(String name, String codeName, String codeValue) {
       fieldBuffer.append("    /**").append("\r\n"); 
       fieldBuffer.append("     * コード名称：" + name + "\r\n");
       fieldBuffer.append("     */").append("\r\n");
       fieldBuffer.append("    private static final String CODE_NM_");
       fieldBuffer.append(codeName).append(" = \"").append(codeValue).append("\";\r\n\r\n");
    }
    
    /**
     * 
     * @param name
     * @param codeName
     * @param methodName
     * @param methodParam
     * @param codeValue
     */
    private void appendMethod(String name, String codeName, String methodName, String methodParam, String codeValue) {
        methodBuffer.append("    /**").append("\r\n");
        methodBuffer.append("     * コードテーブルから").append(name).append("リストを取得する。").append("\r\n");
        methodBuffer.append("     *").append("\r\n");
        methodBuffer.append("     * @return ").append(name).append("リスト").append("\r\n");
        methodBuffer.append("     */").append("\r\n");
        methodBuffer.append("    public static List<CodeInfoVO> get").append(methodName).append("List() {").append("\r\n");
        methodBuffer.append("        return CodeManager.getCodeList(CODE_NM_").append(codeName).append(");\r\n");
        methodBuffer.append("    }\r\n\r\n");
        
        methodBuffer.append("    /**").append("\r\n");
        methodBuffer.append("     * 運用日付よりコードテーブルから").append(name).append("リストを取得する。").append("\r\n");
        methodBuffer.append("     *").append("\r\n");
        methodBuffer.append("     * @param date 運用日付").append("\r\n");
        methodBuffer.append("     * @return ").append(name).append("リスト").append("\r\n");
        methodBuffer.append("     */").append("\r\n");
        methodBuffer.append("    public static List<CodeInfoVO> get").append(methodName).append("ListDate(String date) {").append("\r\n");
        methodBuffer.append("        return CodeManager.getCodeListDate(CODE_NM_").append(codeName).append(", date);\r\n");
        methodBuffer.append("    }\r\n\r\n");
        
        methodBuffer.append("    /**").append("\r\n");
        methodBuffer.append("     * ").append(name).append("コードよりコードテーブルから").append(name).append("名称を取得する。").append("\r\n");
        methodBuffer.append("     *").append("\r\n");
        methodBuffer.append("     * @param ").append(methodParam).append("Code ").append(name).append("コード\r\n");
        methodBuffer.append("     * @return ").append(name).append("取得した").append(name).append("名称\r\n");
        methodBuffer.append("     */").append("\r\n");
        methodBuffer.append("    public static String get").append(methodName).append("Name(String ").append(methodParam).append("Code) {\r\n");
        methodBuffer.append("        return getCodeName(CODE_NM_").append(codeName).append(", ").append(methodParam).append("Code);\r\n");
        methodBuffer.append("    }\r\n\r\n");
        
        methodBuffer.append("    /**").append("\r\n");
        methodBuffer.append("     * ").append(name).append("コードよりコードテーブルから").append(name).append("名称を取得する。").append("\r\n");
        methodBuffer.append("     *").append("\r\n");
        methodBuffer.append("     * @param ").append(methodParam).append("Code ").append(name).append("コード\r\n");
        methodBuffer.append("     * @param date 運用日付").append("\r\n");
        methodBuffer.append("     * @return ").append(name).append("取得した").append(name).append("名称\r\n");
        methodBuffer.append("     */").append("\r\n");
        methodBuffer.append("    public static String get").append(methodName).append("NameDate(String ").append(methodParam).append("Code, String date) {\r\n");
        methodBuffer.append("        return getCodeNameDate(CODE_NM_").append(codeName).append(", ").append(methodParam).append("Code, date);\r\n");
        methodBuffer.append("    }\r\n\r\n");

        methodBuffer.append("    /**").append("\r\n");
        methodBuffer.append("     * ").append(name).append("コードよりコードテーブルから").append(name).append("情報を取得する。").append("\r\n");
        methodBuffer.append("     *").append("\r\n");
        methodBuffer.append("     * @param ").append(methodParam).append("Code ").append(name).append("コード\r\n");
        methodBuffer.append("     * @return ").append(name).append("取得した").append(name).append("情報\r\n");
        methodBuffer.append("     */").append("\r\n");
        methodBuffer.append("    public static CodeInfoVO get").append(methodName).append("Info(String ").append(methodParam).append("Code) {\r\n");
        methodBuffer.append("        return CodeManager.getCodeInfo(CODE_NM_").append(codeName).append(", ").append(methodParam).append("Code);\r\n");
        methodBuffer.append("    }\r\n\r\n");
        
        methodBuffer.append("    /**").append("\r\n");
        methodBuffer.append("     * ").append(name).append("コードよりコードテーブルから").append(name).append("情報を取得する。").append("\r\n");
        methodBuffer.append("     *").append("\r\n");
        methodBuffer.append("     * @param ").append(methodParam).append("Code ").append(name).append("コード\r\n");
        methodBuffer.append("     * @param date 運用日付").append("\r\n");
        methodBuffer.append("     * @return ").append(name).append("取得した").append(name).append("情報\r\n");
        methodBuffer.append("     */").append("\r\n");
        methodBuffer.append("    public static CodeInfoVO get").append(methodName).append("InfoDate(String ").append(methodParam).append("Code, String date) {\r\n");
        methodBuffer.append("        return CodeManager.getCodeInfoDate(CODE_NM_").append(codeName).append(", ").append(methodParam).append("Code, date);\r\n");
        methodBuffer.append("    }\r\n\r\n");

    }

    private void appendConstructor() throws IOException {
        fileWriter.write("    /**" + "\r\n");
        fileWriter.write("     * コンストラクタ" + "\r\n");
        fileWriter.write("     */" + "\r\n");
        fileWriter.write("    private CodeWrapper() {" + "\r\n");
        fileWriter.write("        super();" + "\r\n");
        fileWriter.write("    }" + "\r\n\r\n");
    }
    
    private void appendOthers() throws IOException {
        fileWriter.write("    /**" + "\r\n");
        fileWriter.write("     * コードよりコードテーブルからコード名称を取得する。" + "\r\n");
        fileWriter.write("     * " + "\r\n");
        fileWriter.write("     * @param codeId コードID" + "\r\n");
        fileWriter.write("     * @param code コード" + "\r\n");
        fileWriter.write("     * @return 取得したコード名称" + "\r\n");
        fileWriter.write("     */" + "\r\n");
        fileWriter.write("    private static String getCodeName(String codeId, String code) {" + "\r\n");
        fileWriter.write("        CodeInfoVO codeInfo = CodeManager.getCodeInfo(codeId, code);" + "\r\n");
        fileWriter.write("        if (codeInfo == null) {" + "\r\n");
        fileWriter.write("            return CommonSystemNames.EMPTY_STR;" + "\r\n");
        fileWriter.write("        } else {" + "\r\n");
        fileWriter.write("            return codeInfo.getCodeName();" + "\r\n");
        fileWriter.write("        }" + "\r\n");
        fileWriter.write("    }" + "\r\n\r\n");
        
        fileWriter.write("    /**" + "\r\n");
        fileWriter.write("     * コードよりコードテーブルからコード名称を取得する。" + "\r\n");
        fileWriter.write("     * " + "\r\n");
        fileWriter.write("     * @param codeId コードID" + "\r\n");
        fileWriter.write("     * @param code コード" + "\r\n");
        fileWriter.write("     * @param date 運用日付" + "\r\n");
        fileWriter.write("     * @return 取得したコード名称" + "\r\n");
        fileWriter.write("     */" + "\r\n");
        fileWriter.write("    private static String getCodeNameDate(String codeId, String code, String date) {" + "\r\n");
        fileWriter.write("        CodeInfoVO codeInfo = CodeManager.getCodeInfoDate(codeId, code, date);" + "\r\n");
        fileWriter.write("        if (codeInfo == null) {" + "\r\n");
        fileWriter.write("            return CommonSystemNames.EMPTY_STR;" + "\r\n");
        fileWriter.write("        } else {" + "\r\n");
        fileWriter.write("            return codeInfo.getCodeName();" + "\r\n");
        fileWriter.write("        }" + "\r\n");
        fileWriter.write("    }" + "\r\n");
        fileWriter.write("}" + "\r\n\r\n");
        
    }
    
    /**
     * 実行メソッド
     * 
     * @param args パラメータ
     */
    public static void main(String[] args) {
    	try {
    	    String propFile = "C:\\codeid.properties";
    	    String clsFile = "C:\\test.java";
    	    CodeWrapperCreator creator = new CodeWrapperCreator(propFile, clsFile);
    	    creator.createClass();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

}
