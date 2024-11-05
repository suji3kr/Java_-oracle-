package ch20.oracle.sec10;

import java.sql.*;

public class FunctionCallExam {

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

            // 매개변수화된 호출문 작성: 사용자 정의 함수(user_login)를 호출하기 위한 SQL 문 생성
            String sql = "{? = call user_login(?, ?)}"; // 함수 반환값과 두 개의 매개변수를 가진 호출문
            CallableStatement cstmt = conn.prepareCall(sql); // CallableStatement 객체 생성

            // ?값 지정 및 리턴 타입 지정: 첫 번째 ?는 반환값으로 사용, 정수형으로 지정
            cstmt.registerOutParameter(1, Types.INTEGER);
            // 두 번째 및 세 번째 매개변수에 사용자 정보를 설정
            cstmt.setString(2, "winter"); // 아이디
            cstmt.setString(3, "12345"); // 비밀번호

            // 함수 실행 및 리턴 값 얻기: 실제로 함수 호출 실행
            cstmt.execute();
            int result = cstmt.getInt(1); // 첫 번째 매개변수로부터 반환된 값을 가져옴

            // CallableStatement 닫기: 자원 해제를 위해 Statement를 닫음
            cstmt.close();

            // 로그인 결과 (Switch Expressions 이용): 반환된 결과에 따라 로그인 메시지 결정
            String message = switch(result) {
                case 0 -> "로그인 성공"; // 로그인 성공
                case 1 -> "비밀 번호가 틀림"; // 비밀번호가 틀림
                default -> "아이디가 존재하지 않음"; // 아이디가 존재하지 않음
            };
            System.out.println(message); // 결정된 메시지 출력
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
        } finally {
            // 연결끊기: 데이터베이스 연결 해제
            if(conn != null) {
                try {
                    conn.close(); // 연결을 닫음
                } catch (SQLException e) {
                    e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
                }
            }
        }
    }
}
