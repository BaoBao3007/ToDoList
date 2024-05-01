package Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDataAccess {
    // Thay thế thông tin sau với thông tin kết nối của bạn
    static final String JDBC_URL = "jdbc:" + "mysql://gateway01.ap-southeast-1.prod.aws.tidbcloud.com:4000/TO_DO_LIST?sslMode=VERIFY_IDENTITY";
    static final String USER = "349U3FBRT6ZmZme.root";
    static final String PASSWORD = "oJEuK7kqGu6eEo9y";

    public static void main(String[] args) {
        try {
            // Đăng ký driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Mở kết nối
            Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);

            if (connection != null) {
                 System.out.println("Kết nối đến cơ sở dữ liệu thành công!");
                // Đóng kết nối sau khi sử dụng
                connection.close();
            } else {
                System.out.println("Không thể kết nối đến cơ sở dữ liệu!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Không thể tìm thấy driver JDBC!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Lỗi khi kết nối đến cơ sở dữ liệu!");
            e.printStackTrace();
        }
    }


    // Chức năng thêm: viết ngắn gọi lại các hàm query ( 2-3 hàm), hàm đóng , mở kết nối .
}