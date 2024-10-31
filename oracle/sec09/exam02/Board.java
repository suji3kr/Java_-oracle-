package ch20.oracle.sec09.exam02;


import lombok.Data;
import java.sql.Blob;
import java.util.Date;

@Data               // Constructor, Getter, Setter, hashCode(), equals(), toString() 자동생성
public class Board {
    private int bno;
    private String btitle;
    private String bcontent;
    private String bwriter;
    private Date bdate;
    private String bfilename;
    private Blob bfiledata;
}

