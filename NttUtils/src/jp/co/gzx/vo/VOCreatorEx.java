/*
 * 開発システム：社内作業効率化用Utils
 * 作成会社名：GZX
 * プログラムID：$URL:  $
 * 最終更新者：$Author: gzx $
 * リビジョン：$Rev:  $
 * 履歴 * 2016/08/23 gzx NNN用のバージョンを元に改修
 * 履歴 * 
 */
package jp.co.gzx.vo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * 論理名物理名変換辞書を元に、VOのgetter,setterメソッドを自動的に生成するクラス。
 * 
 * @author gzx
 * @version 1.0 2016/08/23
 */
public final class VOCreatorEx {

	// VOName
	private String voName;
	
	// 論理名物理名変換辞書のproperties
	private String voDctionary;
	
	// VO定義のproperties
	private String pVO;

	private FileWriter fileWriter;

	private StringBuffer headBuffer;
	
	private StringBuffer fieldBuffer;

	private StringBuffer methodBuffer;

	private StringBuffer fileEndBuffer;

	/**
	 * コンストラクタ
	 */
	public VOCreatorEx() throws IOException {
		this.voName = "Vo";
		this.voDctionary = "voDctionary.properties";
		this.pVO = "VOField.properties";
		fileWriter = new FileWriter(voName + ".java");
		
		headBuffer = new StringBuffer();
		fieldBuffer = new StringBuffer();
		methodBuffer = new StringBuffer();
		fileEndBuffer = new StringBuffer();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param voName
	 * @param 論理名物理名変換辞書のproperties
	 * @param VO定義のproperties
	 */
	public VOCreatorEx(String voName, String voDctionary, String pVO) throws IOException {
		this();
		this.voName = voName;
		this.voDctionary = voDctionary;
		this.pVO = pVO;

		fileWriter = new FileWriter(voName + ".java");

	}

	/**
	 * クラスファイルを作成する。
	 */
	public void create() throws IOException {

		// VONameのpropertiesファイルを取得
		InputStream isVOName = getClass().getResourceAsStream(voDctionary);
		Properties pVOName = new Properties();
		pVOName.load(isVOName);

		// VO定義のpropertiesファイルを取得
		InputStream isVO = getClass().getResourceAsStream(pVO);
		Properties pVO = new Properties();
		pVO.load(isVO);

		// VO定義ファイルからReadしたフィールド分Loopし、VOを生成
		Iterator<Entry<Object, Object>> iter = pVO.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry) iter.next();

			/*
			 * EclipseでpropertiesファイルEncodingをUTF-8に設定しておくと、文字化けしない案1
			 * http://d.hatena.ne.jp/torazuka/20130225/eclipse
			 * 1.ツールバーの［Window］→［Preferences］を開く
			 * 2. 左のツリーメニューの［General］→［ContentTypes］を開く
			 * 3. ［Content types:］の「Text」のツリーから「Java Properties File」を選ぶ
			 * 4. 「Default enconding」を「UTF-8」に書き換える
			 * 5.［Update］ボタン→［OK］ボタン
			 */
			/*
			 * propertiesファイル取得した後に文字コード変換しておくと、文字化けしない案２
			 *  String(entry.getKey().getBytes("ISO-8859-1"),"UTF-8");
			 */
			String name = entry.getKey();
			String type = entry.getValue();

			if (name.startsWith("vo.")){
				continue;
			}
			// フィールドの物理名をVONameのpropertiesファイル（論理名物理名変換辞書）を元に変換
			String fieldName;
			if (pVOName.containsKey(name)) {
				// 辞書に存在する場合、変換する。
				fieldName = (String) pVOName.get(name);
			} else {
				// 辞書に存在しない場合、生成したファイルに文字列"物理名が見つかりません"を出力
				fieldName = "物理名が見つかりません";

				// 存在しない論理名をVONameのpropertiesファイル（論理名物理名変換辞書）に追加
				// TODO 今後実現
			}

			// getter,setterのメソッド名変換
			String firstWord = fieldName.substring(0, 1);
			String methodName = fieldName.replaceFirst(firstWord, firstWord.toUpperCase());

			// フィールド定義を追加
			appendField(name, type, fieldName);
			// getter,setterのメソッドを追加
			appendMethod(name, type, fieldName, methodName);
		}

		//
		outputClassHeader(pVO.getProperty("vo.論理名"), pVO.getProperty("vo.物理名"), pVO.getProperty("vo.author"));
		// フィールド定義をファイルに出力
		fileWriter.write(headBuffer.toString());
		// フィールド定義をファイルに出力
		fileWriter.write(fieldBuffer.toString());
		// getter,setterのメソッドをファイルに出力
		fileWriter.write(methodBuffer.toString());

		fileWriter.close();

	}

	/**
	 * ヘッダ情報を作成
	 * 
	 * @param DTO論理名
	 * @param DTO物理名（Class名）
	 * @param 作成者
	 */
	private void outputClassHeader(String DtoName, String className, String author) throws IOException {

		headBuffer.append("package jp.co.jip.nnn.subif.im.dto;").append(System.lineSeparator());
		headBuffer.append(System.lineSeparator());
		headBuffer.append("import java.io.Serializable;").append(System.lineSeparator());
		headBuffer.append(System.lineSeparator());
		headBuffer.append("/**").append(System.lineSeparator());
		headBuffer.append(" * ").append(DtoName).append("。").append(System.lineSeparator());
		headBuffer.append(" * ").append(System.lineSeparator());
		headBuffer.append(" * @author ").append(author).append(System.lineSeparator());
		headBuffer.append(" */").append(System.lineSeparator());
		headBuffer.append("public class ").append(className).append(" implements Serializable {").append(System.lineSeparator());
		headBuffer.append(System.lineSeparator());
		headBuffer.append("    /** serialVersionUID */").append(System.lineSeparator());
		headBuffer.append("    private static final long serialVersionUID = 1L;").append("").append(System.lineSeparator()).append(System.lineSeparator());
	}

	/**
	 * 
	 * @param name
	 * @param codeName
	 * @param codeValue
	 */
	private void appendField(String name, String type, String fieldName) {
		fieldBuffer.append("    /**");
		fieldBuffer.append(" ").append(name);
		fieldBuffer.append(" */").append(System.lineSeparator());
		fieldBuffer.append("    private ").append(type).append(" ").append(fieldName);
		if ("int".equals(type)) {
			fieldBuffer.append(" = 0;").append(System.lineSeparator()).append(System.lineSeparator());
		} else if ("float".equals(type)) {
			fieldBuffer.append(" = 0;").append(System.lineSeparator()).append(System.lineSeparator());
		} else if ("double".equals(type)) {
			fieldBuffer.append(" = 0;").append(System.lineSeparator()).append(System.lineSeparator());
		} else if ("long".equals(type)) {
			fieldBuffer.append(" = 0;").append(System.lineSeparator()).append(System.lineSeparator());
		} else if ("char".equals(type)) {
			fieldBuffer.append(" = 0;").append(System.lineSeparator()).append(System.lineSeparator());
		} else {
			fieldBuffer.append(" = null;").append(System.lineSeparator()).append(System.lineSeparator());
		}
	}

	/**
	 * 
	 * @param name
	 * @param codeName
	 * @param methodName
	 * @param methodParam
	 * @param codeValue
	 */
	private void appendMethod(String name, String type, String fieldName, String methodName) {
		methodBuffer.append("    /**").append(System.lineSeparator());
		methodBuffer.append("     * ").append(name).append("を取得します。").append(System.lineSeparator());
		methodBuffer.append("     *").append(System.lineSeparator());
		methodBuffer.append("     * @return ").append(name).append(System.lineSeparator());
		methodBuffer.append("     */").append(System.lineSeparator());
		methodBuffer.append("    public final ").append(type).append(" get").append(methodName).append("() {")
				.append(System.lineSeparator());
		methodBuffer.append("        return this.").append(fieldName).append(";").append(System.lineSeparator());
		methodBuffer.append("    }").append(System.lineSeparator()).append(System.lineSeparator());

		methodBuffer.append("    /**").append(System.lineSeparator());
		methodBuffer.append("     * ").append(name).append("を設定します。").append(System.lineSeparator());
		methodBuffer.append("     *").append(System.lineSeparator());
		methodBuffer.append("     * @param ").append(fieldName).append(" ").append(name).append(System.lineSeparator());
		methodBuffer.append("     */").append(System.lineSeparator());
		methodBuffer.append("    public final void set").append(methodName).append("(").append(type).append(" ")
				.append(fieldName).append(") ").append("{").append(System.lineSeparator());
		methodBuffer.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";").append(System.lineSeparator());
		methodBuffer.append("    }").append(System.lineSeparator()).append(System.lineSeparator());
	}

	/**
	 * 実行メソッド
	 * 
	 * @param args
	 *            パラメータ
	 */
	public static void main(String[] args) {
		try {
			VOCreatorEx creator = new VOCreatorEx();
			creator.create();
			System.out.println("good!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
