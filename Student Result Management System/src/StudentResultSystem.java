import java.sql.*;
import java.util.Scanner;

public class StudentResultSystem {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/result_management?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "Sairaj@4777";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Scanner scanner = new Scanner(System.in)) {

            // Input student details
            System.out.print("Enter student name: ");
            String name = scanner.nextLine();

            System.out.print("Enter student email: ");
            String email = scanner.nextLine();

            System.out.print("Enter date of birth (YYYY-MM-DD): ");
            String dobInput = scanner.nextLine();

            // Insert student
            String studentSql = "INSERT INTO students (name, email, dob) VALUES (?, ?, ?)";
            PreparedStatement studentStmt = conn.prepareStatement(studentSql, Statement.RETURN_GENERATED_KEYS);
            studentStmt.setString(1, name);
            studentStmt.setString(2, email);
            studentStmt.setDate(3, java.sql.Date.valueOf(dobInput));
            studentStmt.executeUpdate();

            // Get the generated student_id
            ResultSet rs = studentStmt.getGeneratedKeys();
            int studentId = -1;
            if (rs.next()) {
                studentId = rs.getInt(1);
            }

            // Input exam results (e.g. 3 subjects)
            for (int i = 1; i <= 3; i++) {
                System.out.print("Enter subject " + i + " name: ");
                String subject = scanner.nextLine();

                System.out.print("Enter marks for " + subject + ": ");
                int marks = Integer.parseInt(scanner.nextLine());

                String resultSql = "INSERT INTO results (student_id, subject, marks) VALUES (?, ?, ?)";
                PreparedStatement resultStmt = conn.prepareStatement(resultSql);
                resultStmt.setInt(1, studentId);
                resultStmt.setString(2, subject);
                resultStmt.setInt(3, marks);
                resultStmt.executeUpdate();
            }

            System.out.println("Student and results inserted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
