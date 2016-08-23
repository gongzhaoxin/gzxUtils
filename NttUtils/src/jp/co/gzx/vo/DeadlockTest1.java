package jp.co.gzx.vo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DeadlockTest1 {

  private String URL = "jdbc:hitachi:hirdb://DBID=22200,DBHOST=172.30.248.150,ENCODELANG=UTF-8";

  public static void main(String[] args) {
    new DeadlockTest1().test();
  }

  public void test() {

    Connection con1 = null;
    Connection con2 = null;

    try {

      Class.forName("JP.co.Hitachi.soft.HiRDB.JDBC.HiRDBDriver");
      PreparedStatement pstmt1 = null;
      PreparedStatement pstmt2 = null;
      String sql = null;
      int res = -1;

      // コネクション１を取得
      con1 = DriverManager.getConnection(URL, "jsfown", "jsfown");
      con1.setAutoCommit(false);

      // コネクション２を取得
      con2 = DriverManager.getConnection(URL, "jsfown", "jsfown");
      con2.setAutoCommit(false);

      // コネクション１でレコード１をUPDATE
      sql = "update imt42330 set ikjj_ido_kbn='5' where ikjj_kesai_no = '123'";
      pstmt1 = con1.prepareStatement(sql);
      res = pstmt1.executeUpdate();

      // コネクション２でレコード２をUPDATE
      sql = "SELECT * From Imt42330 for update";
      pstmt2 = con2.prepareStatement(sql);
      pstmt2.execute();

      sql = "update imt42330 set ikjj_ido_kbn='5' where ikjj_kesai_no = '123'";
      pstmt2 = con2.prepareStatement(sql);
      res = pstmt2.executeUpdate();
      
      // コネクション１でレコード２をUPDATE【ここでデッドロック（実際はタイムアウト）】
      sql = "SELECT * From Imt42330 for update";
      pstmt1 = con1.prepareStatement(sql);
      pstmt1.execute();

      // コネクション１をコミット
      con1.commit();
      con1.close();

      // コネクション２でレコード１をUPDATE
      sql = "SELECT * From Imt42330 for update";
      pstmt2 = con2.prepareStatement(sql);
      pstmt2.execute();

      // コネクション２をコミット
      con2.commit();
      con2.close();

    } catch (Throwable t) {
      t.printStackTrace();
    } finally {
      try {
        if ((con1 != null) && (con1.isClosed() == false)) {
          con1.close();
        }
        if ((con2 != null) && (con2.isClosed() == false)) {
          con2.close();
        }
      } catch (Throwable t) {
        t.printStackTrace();
      }
    }

  }

}
