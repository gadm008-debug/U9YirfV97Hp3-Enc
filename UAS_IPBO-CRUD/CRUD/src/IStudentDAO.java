import java.util.List;

public interface IStudentDAO {
    void addStudent(Student student); 
    List<Student> getAllStudents();   
    void updateStudent(Student student); 
    void deleteStudent(int studentId);   
}