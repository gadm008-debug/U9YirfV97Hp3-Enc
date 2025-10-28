public class Student {
    private int studentId;
    private String name;
    private String email;

    public Student(int studentId, String name, String email) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
    }

    // Getters
    public int getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}