import javax.swing.*;
import net.sf.jasperreports.engine.*;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ReportParameterForm extends JDialog {
    private JTextField idAwalField;
    private JTextField idAkhirField;
    private JButton cetakButton;

    public ReportParameterForm(JFrame parent) {
        super(parent, "Cetak Laporan by Range ID", true);
        setSize(300, 150);
        setLayout(new GridLayout(3, 2, 10, 10));
        setLocationRelativeTo(parent);

        add(new JLabel("ID Awal:"));
        idAwalField = new JTextField();
        add(idAwalField);

        add(new JLabel("ID Akhir:"));
        idAkhirField = new JTextField();
        add(idAkhirField);

        add(new JLabel()); // Placeholder
        cetakButton = new JButton("Cetak ke PDF");
        add(cetakButton);

        cetakButton.addActionListener(e -> generateReport());
    }

    private void generateReport() {
        try {
            int idAwal = Integer.parseInt(idAwalField.getText());
            int idAkhir = Integer.parseInt(idAkhirField.getText());

            Connection conn = DatabaseConnector.getConnection();
            Map<String, Object> params = new HashMap<>();
            params.put("ID_AWAL", idAwal);
            params.put("ID_AKHIR", idAkhir);

            File reportFile = new File("src/reports/student_report.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportFile.getAbsolutePath());

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Simpan Laporan PDF");
            fileChooser.setSelectedFile(new File("Laporan_Mahasiswa.pdf"));
            
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                
                JasperExportManager.exportReportToPdfFile(jasperPrint, fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Laporan PDF berhasil disimpan di:\n" + fileToSave.getAbsolutePath(), "Sukses", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }
            conn.close(); 
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID Awal dan Akhir harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal membuat laporan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}