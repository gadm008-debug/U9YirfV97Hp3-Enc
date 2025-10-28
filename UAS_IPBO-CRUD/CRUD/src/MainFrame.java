import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MainFrame extends JFrame {
    private IStudentDAO studentDAO;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton reportButton; 

    public MainFrame(IStudentDAO dao) {
        this.studentDAO = dao;
        
        setTitle("Manajemen Data Mahasiswa (UAS)");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        getContentPane().setBackground(new Color(230, 240, 255)); // Warna biru muda

        setupInputPanel();
        setupTablePanel();
        
        refreshTable();
        setLocationRelativeTo(null);
    }

    private void setupInputPanel() {
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(new Color(255, 255, 255, 150)); // Latar semi-transparan
        
        idField = new JTextField(5);
        nameField = new JTextField(15);
        emailField = new JTextField(15);
        
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        reportButton = new JButton("Cetak Laporan PDF"); 

        // --- Atur Style Tombol (Syarat UI) ---
        addButton.setBackground(new Color(0, 150, 0));
        addButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(200, 0, 0));
        deleteButton.setForeground(Color.WHITE);
        reportButton.setBackground(new Color(0, 100, 200));
        reportButton.setForeground(Color.WHITE);

        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(addButton);
        inputPanel.add(updateButton);
        inputPanel.add(deleteButton);
        inputPanel.add(clearButton);
        inputPanel.add(reportButton);

        add(inputPanel, BorderLayout.NORTH);

        // --- Action Listeners ---
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent()); 
        clearButton.addActionListener(e -> clearFields());
        
        reportButton.addActionListener(e -> openReportForm());
    }

    private void setupTablePanel() {
        String[] columnNames = {"ID", "Name", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat tabel tidak bisa diedit
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        studentTable.setRowHeight(25);

        studentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    emailField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    
                    idField.setEditable(false);
                    addButton.setEnabled(false);
                }
            }
        });

        add(new JScrollPane(studentTable), BorderLayout.CENTER);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Student> students = studentDAO.getAllStudents(); 
        for (Student student : students) {
            Object[] row = {
                student.getStudentId(),
                student.getName(),
                student.getEmail()
            };
            tableModel.addRow(row); 
        }
    }

    private void addStudent() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String email = emailField.getText();
            
            studentDAO.addStudent(new Student(id, name, email));
            
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Mahasiswa berhasil ditambahkan!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String email = emailField.getText();
            
            studentDAO.updateStudent(new Student(id, name, email));
            
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Data berhasil di-update!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, 
                "Anda yakin ingin menghapus data ini?", "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            int studentId = (int) tableModel.getValueAt(selectedRow, 0);
            studentDAO.deleteStudent(studentId);
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
        }
    }
    
    private void openReportForm() {
        ReportParameterForm form = new ReportParameterForm(this);
        form.setVisible(true);
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        idField.setEditable(true);
        addButton.setEnabled(true);
        studentTable.clearSelection();
    }
}