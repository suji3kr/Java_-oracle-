package ch20.oracle.sec09.exam01;

import java.sql.*;

public class UserSelectExam {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            //jdbc Driver등록
            Class.forName("oracle.jdbc.driver.OracleDriver");

            //연결하기
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521/xe",
                    "java",
                    "oracle"
            );
            //매개변수화된 SQL 문 작성
            String sql = "" +
                    "SELECT userid, username, userpassword, userage, useremail " + "FROM users " + "WHERE userid=?";

            //PreparedStatement 얻기 및 값 지정
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "winter");

            //SQL문 실행 후, ResultSet을 통해 데이터 읽기
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {     //1개의 데이터 행을 가져왔을 경우
                User user = new User();
                user.setUserId(rs.getString("userid"));
                user.setUserName(rs.getString("username"));
                user.setUserPassword(rs.getString("userpassword"));
                user.setUserAge(rs.getInt(4));
                user.setUserEmail(rs.getString(5));
                System.out.println(user);
            } else {
                System.out.println("사용자 아이디가 존재하지 않음");
            }
            rs.close();
            //PreparedStatement 닫기
            } catch (Exception e) {
            e.printStackTrace();
            } finally {
            if (conn != null) {
                try {
                    //연결끊기
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }
}
