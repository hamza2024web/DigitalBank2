import util.JDBCUtil;

import java.sql.Connection;

public class Main {
    public static void main(String[] args){
        try{
            Connection conn = JDBCUtil.getInstance().getConnection();

            if (conn != null && !conn.isClosed()){
                System.out.println("Connection to PostgreSQL is working!");
            }else {
                System.out.println("Connection failed!");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
