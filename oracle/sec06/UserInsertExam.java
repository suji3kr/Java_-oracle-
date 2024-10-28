package ch20.oracle.sec06;

import java.sql.Connection; // JDBC의 Connection 클래스 임포트
import java.sql.DriverManager; // JDBC의 DriverManager 클래스 임포트
import java.sql.PreparedStatement; // SQL 쿼리를 준비하는 PreparedStatement 클래스 임포트
import java.sql.SQLException; // SQL 예외 처리 클래스 임포트

public class UserInsertExam {
    public static void main(String[] args) {

        Connection conn = null; // 데이터베이스 연결을 위한 Connection 객체 초기화
        try {
            // JDBC 드라이버 등록
            Class.forName("oracle.jdbc.OracleDriver");

            // 데이터베이스에 연결
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521/xe", // JDBC URL
                    "java", // 데이터베이스 사용자 이름
                    "oracle" // 데이터베이스 비밀번호
            );

            // 매개변수화된 SQL 문 작성
            String sql = "" +
                    "INSERT INTO users(userid, username, userpassword, userage, useremail) " +
                    "VALUES(?, ?, ?, ?, ?)"; // SQL 쿼리 문

            // PreparedStatement 얻기 및 값 지정
            PreparedStatement pstmt = conn.prepareStatement(sql); // SQL 쿼리를 준비
            pstmt.setString(1, "summer"); // userid
            pstmt.setString(2, "한여름"); // username
            pstmt.setString(3, "12345"); // userpassword
            pstmt.setInt(4, 25); // userage
            pstmt.setString(5, "winter@mycompany.com"); // useremail

            // SQL문 실행
            int rows = pstmt.executeUpdate(); // SQL 쿼리 실행
            System.out.println("저장된 행 수: " + rows); // 저장된 행 수 출력

            // PreparedStatement 닫기
            pstmt.close(); // 자원 해제를 위해 PreparedStatement 닫기

        } catch (ClassNotFoundException e) {
            // JDBC 드라이버를 찾을 수 없는 경우 예외 처리
            e.printStackTrace();

        } catch (SQLException e) {
            // SQL 관련 예외 처리
            e.printStackTrace();

        } finally {
            // 연결 해제
            if (conn != null) {
                try {
                    conn.close(); // 데이터베이스 연결 닫기
                    System.out.println("연결 끊기");
                } catch (SQLException e) {
                    // 연결 닫기 중 예외 처리
                    e.printStackTrace();
                }
            }
        }
    }
}
