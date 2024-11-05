package ch20.oracle.sec09.exam02;

import java.io.FileOutputStream; // 파일 시스템에 파일을 쓰기 위한 클래스
import java.io.InputStream; // 바이너리 스트림에서 데이터를 읽기 위한 클래스
import java.io.OutputStream; // 바이너리 스트림에 데이터를 쓰기 위한 클래스
import java.sql.*; // JDBC 및 SQL 기능을 위한 클래스

public class BoardSelectExam {
    public static void main(String[] args) {
        Connection conn = null; // 연결 변수를 초기화
        try {
            // JDBC Driver 등록: Oracle JDBC 드라이버를 메모리에 로드
            Class.forName("oracle.jdbc.OracleDriver");

            // 연결하기: 데이터베이스에 연결하기 위한 정보 (URL, 사용자명, 비밀번호)
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521/xe", // 데이터베이스 URL
                    "java", // 사용자 이름
                    "oracle" // 비밀번호
            );

            // 매개변수화된 SQL 문 작성: 사용자가 작성한 게시글을 가져오기 위한 SQL 쿼리
            String sql = "" +
                    "SELECT bno, btitle, bcontent, bwriter, bdate, bfilename, bfiledata " +
                    "FROM boards " +
                    "WHERE bwriter=?"; // 게시글 작성자를 기준으로 선택

            // PreparedStatement 얻기 및 값 지정: SQL 쿼리를 준비하고 매개변수 설정
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "winter"); // 작성자 이름을 "winter"로 설정

            // SQL문 실행 후, ResultSet을 통해 데이터 읽기: 쿼리 실행 및 결과 집합 반환
            ResultSet rs = pstmt.executeQuery();

            // 결과 집합을 반복하여 데이터 처리
            while (rs.next()) {
                // 데이터 행을 읽고 Board 객체 생성
                Board board = new Board();
                board.setBno(rs.getInt("bno")); // 게시글 번호
                board.setBtitle(rs.getString("btitle")); // 게시글 제목
                board.setBcontent(rs.getString("bcontent")); // 게시글 내용
                board.setBwriter(rs.getString("bwriter")); // 게시글 작성자
                board.setBdate(rs.getDate("bdate")); // 게시일
                board.setBfilename(rs.getString("bfilename")); // 파일 이름
                board.setBfiledata(rs.getBlob("bfiledata")); // 파일 데이터 (BLOB)

                // 콘솔에 게시글 정보 출력
                System.out.println(board);

                // 파일로 저장: BLOB 데이터를 파일로 저장
                Blob blob = board.getBfiledata(); // BLOB 객체 가져오기
                if (blob != null) {
                    InputStream is = blob.getBinaryStream(); // BLOB 데이터를 읽기 위한 InputStream
                    OutputStream os = new FileOutputStream("C:/Temp/" + board.getBfilename()); // 파일 출력 스트림 생성
                    is.transferTo(os); // BLOB 데이터를 파일로 전송
                    os.flush(); // 출력 스트림 플러시
                    os.close(); // 출력 스트림 닫기
                    is.close(); // 입력 스트림 닫기
                }
            }
            rs.close(); // ResultSet 닫기

            // PreparedStatement 닫기: 자원 해제를 위해 Statement를 닫음
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
