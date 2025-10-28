import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IStudentDAO studentDAO = new StudentDAO();
            
            MainFrame frame = new MainFrame(studentDAO);
            frame.setVisible(true);
        });
    }
}