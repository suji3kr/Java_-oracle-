package ch20.oracle.sec07;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BoardUpdateExam {
    public static void main(String[] args) {
        Connection conn = null; // 데이터베이스 연결 변수를 초기화
        try {
            // JDBC Driver 등록: Oracle JDBC 드라이버를 메모리에 로드
            Class.forName("oracle.jdbc.OracleDriver");

            // 연결하기: 데이터베이스에 연결하기 위한 정보 (URL, 사용자명, 비밀번호)
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521/xe",
                    "java",
                    "oracle"
            );

            // 매개변수화된 SQL문 작성: 게시글을 업데이트하기 위한 SQL 쿼리
            String sql = new StringBuilder()
                    .append("UPDATE boards SET ") // boards 테이블의 데이터 업데이트
                    .append("btitle=?, ") // 제목 업데이트
                    .append("bcontent=?, ") // 내용 업데이트
                    .append("bfilename=?, ") // 파일 이름 업데이트
                    .append("bfiledata=? ")  // 여기서 쉼표 제거,  파일 데이터 업데이트
                    .append("WHERE bno=?") // 여기에도 불필요한 공백을 제거 //특정 게시글 번호를 기준으로 업데이트
                    .toString();

            // PreparedStatement 얻기 및 값 지정: SQL 쿼리를 준비하고 매개변수를 설정
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "눈사람"); // 첫 번째 매개변수: 게시글 제목
            pstmt.setString(2, "눈으로 만든 사람"); // 두 번째 매개변수: 게시글 내용
            pstmt.setString(3, "snowman.jpg"); // 세 번째 매개변수: 파일 이름
            pstmt.setBlob(4, BoardUpdateExam.class.getResourceAsStream("snowman.jpg")); // 네 번째 매개변수: 파일 데이터 (BLOB)
            pstmt.setInt(5, 3); // 다섯 번째 매개변수: 업데이트할 게시글 번호 (3번)

            // SQL문 실행: 업데이트 쿼리를 실행하고 수정된 행 수 반환
            int rows = pstmt.executeUpdate();
            System.out.println("수정 된 행 수: " + rows); // 수정된 행 수 출력

            // PreparedStatement 닫기: 자원 해제를 위해 PreparedStatement를 닫음
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력
        } finally {
            // 연결 끊기: 데이터베이스 연결 해제
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

