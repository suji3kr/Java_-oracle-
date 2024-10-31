package ch20.oracle.sec06;

import java.sql.*; // JDBC 관련 클래스 임포트

public class BoardWithFileInsertExam {

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

            // 매개변수화된 SQL문 작성
            String sql = " " +
                    "INSERT INTO boards (bno, btitle, bcontent, bwriter, bdate, bfilename, bfiledata) " +
                    "VALUES (SEQ_BNO.NEXTVAL, ?, ?, ?, SYSDATE, ?, ?)"; // SQL INSERT 문

//            // PreparedStatement 얻기 및 값 지정
//            PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{"bno"}); // bno를 자동 생성키로 설정
//            pstmt.setString(1, "눈 오는 날"); // btitle
//            pstmt.setString(2, "함박눈이 내려요"); // bcontent
//            pstmt.setString(3, "winter"); // bwriter
//            pstmt.setString(4, "snow.jpg"); // bfilename
//            pstmt.setBlob(5, BoardWithFileInsertExam.class.getResourceAsStream("snow.jpg")); // bfiledata


            PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{"bno"}); // bno를 자동 생성키로 설정
            pstmt.setString(1, "크리스마스"); // btitle
            pstmt.setString(2, "메리 크리스마스~"); // bcontent
            pstmt.setString(3, "winter"); // bwriter
            pstmt.setString(4, "christmas.jpg"); // bfilename
            pstmt.setBlob(5, BoardWithFileInsertExam.class.getResourceAsStream("christmas.jpg")); // bfiledata

            // SQL문 실행
            int rows = pstmt.executeUpdate(); // 데이터베이스에 SQL문 실행
            System.out.println("저장된 행 수: " + rows); // 저장된 행 수 출력

            // bno 값 얻기
            if (rows == 1) {
                ResultSet rs = pstmt.getGeneratedKeys(); // 자동 생성된 키 가져오기
                if (rs.next()) {
                    int bno = rs.getInt(1); // 첫 번째 열에서 bno 값 가져오기
                    System.out.println("저장된 bno: " + bno); // 저장된 bno 출력
                }
                rs.close(); // ResultSet 닫기
            }

            // PreparedStatement 닫기
            pstmt.close(); // 자원 해제를 위해 PreparedStatement 닫기
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력

        } finally {
            // 연결 해제
            if (conn != null) {
                try {
                    conn.close(); // 데이터베이스 연결 닫기
                } catch (SQLException e) {
                    e.printStackTrace(); // 연결 닫기 중 예외 발생 시 스택 트레이스 출력
                }
            }
        }
    }
}

