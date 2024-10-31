package ch20.oracle.sec09.exam02;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;

public class BoardSelectExam {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            //JDBC Driver등록
            Class.forName("oracle.jdbc.OracleDriver");

            //연결하기

            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521/xe",
                    "java",
                    "oracle"
            );
            //매개변수화된 SQL 문 작성
            String sql = "" +
                    "SELECT bno, btitle, bcontent, bwriter, bdate, bfilename, bfiledata " +
                    "FROM boards " +
                    "WHERE bwriter=?";

            //PreparedStatement 얻기 및 값 지정
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "winter");

            //SQL문 실행 후, Resultset을 통해 데이터 읽기
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                //데이터 행을 읽고 board객체 생성
                Board board = new Board();
                board.setBno(rs.getInt("bno"));
                board.setBtitle(rs.getString("btitle"));
                board.setBcontent(rs.getString("bcontent"));
                board.setBwriter(rs.getString("bwriter"));
                board.setBdate(rs.getDate("bdate"));
                board.setBfilename(rs.getString("bfilename"));
                board.setBfiledata(rs.getBlob("bfiledata"));

                //콘솔에 출력
                System.out.println(board);

                //파일로 저장

                Blob blob = board.getBfiledata();
                if(blob != null) {
                    InputStream is = blob.getBinaryStream();
                    OutputStream os = new FileOutputStream("C:/Temp/" +board.getBfilename());
                    is.transferTo(os);
                    os.flush();
                    os.close();
                    is.close();
                }
            }
            rs.close();

            //PreparedStatement 닫기
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null){
                try {
                    //연결끊기
                    conn.close();
                }catch (SQLException e){}
            }
        }
    }
}
