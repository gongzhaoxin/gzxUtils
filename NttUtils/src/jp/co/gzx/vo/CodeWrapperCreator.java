/*
 * @(#)CodeWrapperCreator.java
 *
 * Copyright (c) 2007 NTT DATA Corporation.
 */
package jp.co.gzx.vo;

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
     * �ݒ�t�@�C��
     */
    private String propFile;
    
    /**
     * �����N���X�t�@�C��
     */
    private String clsFile;
    
    private FileWriter fileWriter;
    
    private StringBuffer fieldBuffer;
    
    private StringBuffer methodBuffer;
    
    /**
     * �R���X�g���N�^
     */
    public CodeWrapperCreator(String propFile, String clsFile) throws IOException {
        this.propFile = propFile;
        fileWriter = new FileWriter(clsFile);

        fieldBuffer = new StringBuffer();
        methodBuffer = new StringBuffer();
    }

    /**
     * �N���X�t�@�C�����쐬����B
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
       fieldBuffer.append("     * �R�[�h���́F" + name + "\r\n");
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
        methodBuffer.append("     * �R�[�h�e�[�u������").append(name).append("���X�g���擾����B").append("\r\n");
        methodBuffer.append("     *").append("\r\n");
        methodBuffer.append("     * @return ").append(name).append("���X�g").append("\r\n");
        methodBuffer.append("     */").append("\r\n");
        methodBuffer.append("    public static List<CodeInfoVO> get").append(methodName).append("List() {").append("\r\n");
        methodBuffer.append("        return CodeManager.getCodeList(CODE_NM_").append(codeName).append(");\r\n");
        methodBuffer.append("    }\r\n\r\n");
        
        methodBuffer.append("    /**").append("\r\n");
        methodBuffer.append("     * �^�p���t���R�[�h�e�[�u������").append(name).append("���X�g���擾����B").append("\r\n");
        methodBuffer.append("     *").append("\r\n");
        methodBuffer.append("     * @param date �^�p���t").append("\r\n");
        methodBuffer.append("     * @return ").append(name).append("���X�g").append("\r\n");
        methodBuffer.append("     */").append("\r\n");
        methodBuffer.append("    public static List<CodeInfoVO> get").append(methodName).append("ListDate(String date) {").append("\r\n");
        methodBuffer.append("        return CodeManager.getCodeListDate(CODE_NM_").append(codeName).append(", date);\r\n");
        methodBuffer.append("    }\r\n\r\n");
        
        methodBuffer.append("    /**").append("\r\n");
        methodBuffer.append("     * ").append(name).append("�R�[�h���R�[�h�e�[�u������").append(name).append("���̂��擾����B").append("\r\n");
        methodBuffer.append("     *").append("\r\n");
        methodBuffer.append("     * @param ").append(methodParam).append("Code ").append(name).append("�R�[�h\r\n");
        methodBuffer.append("     * @return ").append(name).append("�擾����").append(name).append("����\r\n");
        methodBuffer.append("     */").append("\r\n");
        methodBuffer.append("    public static String get").append(methodName).append("Name(String ").append(methodParam).append("Code) {\r\n");
        methodBuffer.append("        return getCodeName(CODE_NM_").append(codeName).append(", ").append(methodParam).append("Code);\r\n");
        methodBuffer.append("    }\r\n\r\n");
        
        methodBuffer.append("    /**").append("\r\n");
        methodBuffer.append("     * ").append(name).append("�R�[�h���R�[�h�e�[�u������").append(name).append("���̂��擾����B").append("\r\n");
        methodBuffer.append("     *").append("\r\n");
        methodBuffer.append("     * @param ").append(methodParam).append("Code ").append(name).append("�R�[�h\r\n");
        methodBuffer.append("     * @param date �^�p���t").append("\r\n");
        methodBuffer.append("     * @return ").append(name).append("�擾����").append(name).append("����\r\n");
        methodBuffer.append("     */").append("\r\n");
        methodBuffer.append("    public static String get").append(methodName).append("NameDate(String ").append(methodParam).append("Code, String date) {\r\n");
        methodBuffer.append("        return getCodeNameDate(CODE_NM_").append(codeName).append(", ").append(methodParam).append("Code, date);\r\n");
        methodBuffer.append("    }\r\n\r\n");

        methodBuffer.append("    /**").append("\r\n");
        methodBuffer.append("     * ").append(name).append("�R�[�h���R�[�h�e�[�u������").append(name).append("�����擾����B").append("\r\n");
        methodBuffer.append("     *").append("\r\n");
        methodBuffer.append("     * @param ").append(methodParam).append("Code ").append(name).append("�R�[�h\r\n");
        methodBuffer.append("     * @return ").append(name).append("�擾����").append(name).append("���\r\n");
        methodBuffer.append("     */").append("\r\n");
        methodBuffer.append("    public static CodeInfoVO get").append(methodName).append("Info(String ").append(methodParam).append("Code) {\r\n");
        methodBuffer.append("        return CodeManager.getCodeInfo(CODE_NM_").append(codeName).append(", ").append(methodParam).append("Code);\r\n");
        methodBuffer.append("    }\r\n\r\n");
        
        methodBuffer.append("    /**").append("\r\n");
        methodBuffer.append("     * ").append(name).append("�R�[�h���R�[�h�e�[�u������").append(name).append("�����擾����B").append("\r\n");
        methodBuffer.append("     *").append("\r\n");
        methodBuffer.append("     * @param ").append(methodParam).append("Code ").append(name).append("�R�[�h\r\n");
        methodBuffer.append("     * @param date �^�p���t").append("\r\n");
        methodBuffer.append("     * @return ").append(name).append("�擾����").append(name).append("���\r\n");
        methodBuffer.append("     */").append("\r\n");
        methodBuffer.append("    public static CodeInfoVO get").append(methodName).append("InfoDate(String ").append(methodParam).append("Code, String date) {\r\n");
        methodBuffer.append("        return CodeManager.getCodeInfoDate(CODE_NM_").append(codeName).append(", ").append(methodParam).append("Code, date);\r\n");
        methodBuffer.append("    }\r\n\r\n");

    }

    private void appendConstructor() throws IOException {
        fileWriter.write("    /**" + "\r\n");
        fileWriter.write("     * �R���X�g���N�^" + "\r\n");
        fileWriter.write("     */" + "\r\n");
        fileWriter.write("    private CodeWrapper() {" + "\r\n");
        fileWriter.write("        super();" + "\r\n");
        fileWriter.write("    }" + "\r\n\r\n");
    }
    
    private void appendOthers() throws IOException {
        fileWriter.write("    /**" + "\r\n");
        fileWriter.write("     * �R�[�h���R�[�h�e�[�u������R�[�h���̂��擾����B" + "\r\n");
        fileWriter.write("     * " + "\r\n");
        fileWriter.write("     * @param codeId �R�[�hID" + "\r\n");
        fileWriter.write("     * @param code �R�[�h" + "\r\n");
        fileWriter.write("     * @return �擾�����R�[�h����" + "\r\n");
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
        fileWriter.write("     * �R�[�h���R�[�h�e�[�u������R�[�h���̂��擾����B" + "\r\n");
        fileWriter.write("     * " + "\r\n");
        fileWriter.write("     * @param codeId �R�[�hID" + "\r\n");
        fileWriter.write("     * @param code �R�[�h" + "\r\n");
        fileWriter.write("     * @param date �^�p���t" + "\r\n");
        fileWriter.write("     * @return �擾�����R�[�h����" + "\r\n");
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
     * ���s���\�b�h
     * 
     * @param args �p�����[�^
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
