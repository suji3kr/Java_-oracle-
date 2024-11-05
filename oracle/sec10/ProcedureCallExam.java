package ch20.oracle.sec10;

import java.sql.*;

public class ProcedureCallExam {
    public static void main(String[] args) {
        Connection conn = null; // Initialize the connection variable
        try {
            // JDBC Driver 등록: Oracle JDBC 드라이버를 메모리에 로드
            Class.forName("oracle.jdbc.OracleDriver");

            // 연결하기: 데이터베이스에 연결하기 위한 정보 (URL, 사용자명, 비밀번호)
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521/xe", // 데이터베이스 URL
                    "java", // 사용자 이름
                    "oracle" // 비밀번호
            );

            // 매개변수화된 호출문 작성: 사용자 정의 프로시저(user_create)를 호출하기 위한 SQL 문 생성
            String sql = "{call user_create(?, ?, ?, ?, ?, ?)}"; // 프로시저에 전달할 매개변수 6개
            CallableStatement cstmt = conn.prepareCall(sql); // CallableStatement 객체 생성

            // ?값 지정 및 리턴 타입 지정: 프로시저의 입력 매개변수 및 출력 매개변수를 설정
            cstmt.setString(1, "summer"); // 사용자 아이디
            cstmt.setString(2, "한여름"); // 사용자 이름
            cstmt.setString(3, "12345"); // 비밀번호
            cstmt.setInt(4, 26); // 사용자 나이
            cstmt.setString(5, "summer@mycompany.com"); // 사용자 이메일
            cstmt.registerOutParameter(6, Types.INTEGER); // 출력 매개변수로 저장된 행 수를 정수형으로 지정

            // 프로시저 실행 및 리턴값 얻기: 프로시저를 호출하고 실행 결과를 얻음
            cstmt.execute(); // 프로시저 실행
            int rows = cstmt.getInt(6); // 출력 매개변수에서 저장된 행 수를 가져옴
            System.out.println("저장된 행 수: " + rows); // 저장된 행 수를 출력

            // CallableStatement 닫기: 자원 해제를 위해 Statement를 닫음
            cstmt.close();
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
        } finally {
            // 연결끊기: 데이터베이스 연결 해제
            if (conn != null) {
                try {
                    conn.close(); // 연결을 닫음
                } catch (SQLException e) {
                    e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
                }
            }
        }
    }
}