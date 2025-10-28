import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO implements IStudentDAO {

    @Override
    public void addStudent(Student student) {
        String sql = "INSERT INTO students (student_id, name, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getEmail());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("student_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                students.add(new Student(id, name, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public void updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, email = ? WHERE student_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getEmail());
            pstmt.setInt(3, student.getStudentId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}