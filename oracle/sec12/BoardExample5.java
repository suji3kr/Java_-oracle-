package ch20.oracle.sec12;

import ch20.oracle.sec09.exam02.Board;

import java.sql.*;
import java.util.Scanner;

public class BoardExample5 {
    // 필드 선언
    private Scanner scanner = new Scanner(System.in);  // 사용자 입력을 받을 Scanner 객체
    private Connection conn;  // 데이터베이스 연결을 위한 Connection 객체

    // 생성자: BoardExample3 객체가 생성될 때 실행됨
    public BoardExample5() {
        try {
            // JDBC 드라이버 등록: Oracle JDBC 드라이버를 동적으로 로드
            Class.forName("oracle.jdbc.OracleDriver");

            // 데이터베이스에 연결: 연결 URL, 사용자명, 비밀번호를 사용하여 연결을 생성
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521/xe",  // 데이터베이스 URL (호스트: localhost, 포트: 1521, SID: xe)
                    "java",  // 데이터베이스 사용자명
                    "oracle"  // 데이터베이스 비밀번호
            );
        } catch (Exception e) {
            e.printStackTrace();  // 예외가 발생하면 예외 정보 출력
            exit();  // 예외 발생 시 프로그램 종료
        }
    }

    // 게시물 목록을 출력하는 메소드
    public void list() {
        // 출력 포맷을 설정하여 타이틀 및 컬럼명을 출력
        System.out.println();
        System.out.println("[게시물 목록]");  // 게시물 목록 제목
        System.out.println("--------------------------------------------");
        // 컬럼명을 포맷에 맞춰 출력 (no, writer, date, title)
        System.out.printf("%-6s%-12s%-16s%-40s\n", "no", "writer", "date", "title");
        System.out.println("--------------------------------------------");

        // boards 테이블에서 게시물 정보를 조회하는 SQL 쿼리
        try {
            // SQL 쿼리문: 게시물 번호(bno), 제목(btitle), 내용(bcontent), 작성자(bwriter), 작성일(bdate) 조회
            String sql = "" +
                    "SELECT bno, btitle, bcontent, bwriter, bdate " +
                    "FROM boards " +  // boards 테이블에서 데이터를 조회
                    "ORDER BY bno DESC";  // 게시물 번호(bno) 내림차순으로 정렬

            // PreparedStatement 객체 생성하여 SQL 쿼리 실행 준비
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // 쿼리 실행 결과로 ResultSet 객체 받기
            ResultSet rs = pstmt.executeQuery();

            // ResultSet에서 각 행을 순차적으로 읽어들여 출력
            while (rs.next()) {
                Board board = new Board();  // Board 객체 생성
                board.setBno(rs.getInt("bno"));  // bno 컬럼 값 설정
                board.setBtitle(rs.getString("btitle"));  // btitle 컬럼 값 설정
                board.setBwriter(rs.getString("bwriter"));  // bwriter 컬럼 값 설정
                board.setBdate(rs.getDate("bdate"));  // bdate 컬럼 값 설정

                // 게시물 정보를 포맷에 맞게 출력
                System.out.printf("%-6s%-12s%-16s%-40s \n",
                        board.getBno(),  // 게시물 번호
                        board.getBwriter(),  // 작성자
                        board.getBdate(),  // 작성일
                        board.getBtitle());  // 게시물 제목
            }

            // 쿼리 실행 후, ResultSet과 PreparedStatement 닫기 (자원 해제)
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();  // SQL 예외가 발생하면 예외 정보 출력
            exit();  // 예외 발생 시 프로그램 종료
        }

        // 메인 메뉴 출력
        mainMenu();
    }

    // 메인 메뉴를 출력하고 사용자의 선택을 받아 처리하는 메소드
    public void mainMenu() {
        System.out.println();
        System.out.println("--------------------------------------------");
        // 메인 메뉴 출력: 사용자가 선택할 수 있는 옵션을 표시
        System.out.println("메인 메뉴: 1. Create | 2. Read | 3. Clear | 4. Exit");
        System.out.println("메뉴 선택: ");

        String menuNo = scanner.nextLine();  // 사용자가 입력한 메뉴 번호를 읽어옴
        System.out.println();

        // 사용자 선택에 따른 메소드 호출
        switch (menuNo) {
            case "1" -> create();  // '1'을 선택하면 create() 메소드 실행 (게시물 작성)
            case "2" -> read();  // '2'를 선택하면 read() 메소드 실행 (게시물 조회)
            case "3" -> clear();  // '3'을 선택하면 clear() 메소드 실행 (게시물 삭제)
            case "4" -> exit();  // '4'를 선택하면 exit() 메소드 실행 (프로그램 종료)
        }
    }

    // 게시물 작성 메소드
    public void create() {
        // 새로운 Board 객체를 생성하여 게시물 정보를 입력받기 위한 준비
        Board board = new Board();

        // 새 게시물 입력 안내 출력
        System.out.println("[ 새 게시물 입력]");

        // 사용자로부터 제목 입력 받기
        System.out.println("제목: ");
        board.setBtitle(scanner.nextLine());  // 입력받은 제목을 Board 객체에 설정

        // 사용자로부터 내용 입력 받기
        System.out.println("내용: ");
        board.setBcontent(scanner.nextLine());  // 입력받은 내용을 Board 객체에 설정

        // 사용자로부터 작성자 입력 받기
        System.out.println("작성자: ");
        board.setBwriter(scanner.nextLine());  // 입력받은 작성자를 Board 객체에 설정

        // 보조 메뉴 출력 (게시물 입력 완료 후 추가 메뉴 제공)
        System.out.println("---------------------------------------");
        System.out.println("메뉴 선택: ");
        String menuNo = scanner.nextLine();  // 사용자로부터 메뉴 번호 입력 받기

        // 사용자가 '1'을 선택하면 게시물 데이터를 데이터베이스에 저장
        if (menuNo.equals("1")) {
            // SQL 쿼리: boards 테이블에 새 게시물을 삽입
            try {
                String sql = "" +
                        "INSERT INTO boards (bno, btitle, bcontent, bwriter, bdate) " +  // 게시물 삽입 쿼리
                        "VALUES (SEQ_BNO.NEXTVAL, ?, ?, ?, SYSDATE)";  // SEQ_BNO.NEXTVAL로 게시물 번호 자동 생성, SYSDATE로 현재 시간 삽입

                // PreparedStatement 객체 생성
                PreparedStatement pstmt = conn.prepareStatement(sql);
                // ? 부분에 실제 값 설정 (게시물 제목, 내용, 작성자)
                pstmt.setString(1, board.getBtitle());  // 제목
                pstmt.setString(2, board.getBcontent());  // 내용
                pstmt.setString(3, board.getBwriter());  // 작성자

                // 쿼리 실행하여 데이터베이스에 게시물 정보 저장
                pstmt.executeUpdate();  // INSERT 실행

                // PreparedStatement 자원 해제
                pstmt.close();
            } catch (Exception e) {
                // 예외 발생 시 출력하고 프로그램 종료
                e.printStackTrace();
                exit();  // 예외 처리 후 프로그램 종료
            }
        }

        // 게시물 목록을 출력하여 새로 추가된 게시물이 목록에 반영되도록 함
        list();
    }


    // 게시물 조회 메소드 (현재는 구체적인 기능을 구현하지 않음)
    public void read() {

        //입력받기
        System.out.println("[게시믈 읽기]");
        System.out.print("bno: ");
        int bno = Integer.parseInt(scanner.nextLine());

        //boards 테이블에서 해당 게시물을 가져와 출력
        try {
            String sql = "" +
                    "SELECT bno, btitle, bcontent, bwriter, bdate) " +  // 게시물 삽입 쿼리
                    "FROM boards" +
                    "WHERE bno=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bno);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Board board = new Board();  // Board 객체 생성
                board.setBno(rs.getInt("bno"));  // bno 컬럼 값 설정
                board.setBtitle(rs.getString("btitle"));  // btitle 컬럼 값 설정
                board.setBwriter(rs.getString("bwriter"));  // bwriter 컬럼 값 설정
                board.setBdate(rs.getDate("bdate"));  // bdate 컬럼 값 설정
                System.out.println("##############");
                System.out.println("번호: " + board.getBno());
                System.out.println("제목: " + board.getBtitle());
                System.out.println("내용: " + board.getBcontent());
                System.out.println("작성자: " + board.getBwriter());
                System.out.println("날짜: " + board.getBdate());

                System.out.println("##############");
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }

     //게시물 목록 출력
        list();
    }

    // 게시물 삭제 메소드 (현재는 구체적인 기능을 구현하지 않음)
    public void clear() {
        System.out.println("*** clear() 메소드 실행됨");
        // 게시물 목록을 출력하기 위해 list() 메소드 호출
        list();
    }

    // 프로그램 종료 메소드
    public void exit() {
        System.exit(0);  // 프로그램을 종료 (0은 정상 종료 코드)
    }

    // main 메소드: 프로그램의 시작점
    public static void main(String[] args) {
        // BoardExample3 객체 생성하여 프로그램 시작
        BoardExample5 boardExample = new BoardExample5();
        // 게시물 목록을 출력하는 list() 메소드 호출
        boardExample.list();
    }
}

