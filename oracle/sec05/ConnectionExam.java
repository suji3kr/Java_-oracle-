package ch20.oracle.sec05;

import java.sql.Connection; // JDBC의 Connection 클래스 임포트
import java.sql.DriverManager; // JDBC의 DriverManager 클래스 임포트
import java.sql.SQLException; // SQL 예외 처리 클래스 임포트

public class ConnectionExam {
    public static void main(String[] args) {

        Connection conn = null; // Connection 객체 초기화

        try {
            // Oracle JDBC 드라이버 로드
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // 데이터베이스에 연결
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521/xe", // JDBC URL
                    "java", // 데이터베이스 사용자 이름
                    "oracle" // 데이터베이스 비밀번호
            );

            // 연결 성공 메시지 출력
            System.out.println("연결 성공");

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
                    conn.close(); // 연결 닫기
                    System.out.println("연결 끊기");
                } catch (SQLException e) {
                    // 연결 닫기 중 예외 처리
                    e.printStackTrace();
                }
            }
        }
    }
}
