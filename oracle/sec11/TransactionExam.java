package ch20.oracle.sec11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionExam {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            //JDBC Driver 등록
            Class.forName("oracle.jdbc.OracleDriver");

            //연결하기
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521/xe",
                    "java",
                    "oracle"
            );
            //트랜젝션 시작 -------------------------------------------
            //자동커밋 기능 끄기
            conn.setAutoCommit(false);

            //출금작업
            String sql1 = "UPDATE accounts SET balance= balance-? WHERE ano=?";
            PreparedStatement pstmt1 = conn. prepareStatement(sql1);
            pstmt1.setInt();







        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
