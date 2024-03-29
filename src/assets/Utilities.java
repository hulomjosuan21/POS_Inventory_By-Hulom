package assets;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.toedter.calendar.JDateChooser;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.DefaultFormatter;

@Author("Josuan Leonardo Hulom")
public class Utilities {
    public static boolean isEmptyOrSpacesOnly(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    public static void switchPanel(JLayeredPane layered, JPanel panel){
        layered.removeAll();
        layered.add(panel);
        layered.repaint();
        layered.revalidate();         
    }  
    
    public static void backLabelActions(JLabel label,String[] iconName){
        label.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                label.setIcon(new ImageIcon("src/icons/"+iconName[2]));
                label.repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e){
                label.setIcon(new ImageIcon("src/icons/"+iconName[0]));
                label.repaint();                       
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setIcon(new ImageIcon("src/icons/"+iconName[1]));
                label.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setIcon(new ImageIcon("src/icons/"+iconName[0]));
                label.repaint();       
            }
        });
    }
    
    public static void backLabelActions(JLabel label,Color c1, Color c2, Color c3, Color c4){
        label.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                label.setForeground(c1);
                label.repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e){
                label.setForeground(c2);  
                label.repaint();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(c3);
                label.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(c4);     
                label.repaint();
            }
        });
    }
    
    public static boolean validateAge(JDateChooser dateChooser) {
        Date selectedDate = dateChooser.getDate();

        if (selectedDate == null) {
            return false;
        }
        Calendar currentDate = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(selectedDate);

        int age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (age < 18) {
            return false;
        }
        return true;
    } 
   
    public static boolean validateUsername(String _username){
        final String pattern = "^[a-zA-Z]+@[0-9]+$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(_username);
        return matcher.matches();
    }    
    
    public static boolean validatePassword(String _password, Component parentComponent) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(_password);
        boolean isPhoneNumber = _password.matches("\\d{10}"); 
        
        if (isPhoneNumber) {
            JOptionPane.showMessageDialog(parentComponent, "You've used a phone number as a password. Please choose a stronger password.","",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!matcher.matches()) {
            if (!_password.matches(".*[0-9].*")) {
                JOptionPane.showMessageDialog(parentComponent, "Password should contain at least one digit.","",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!_password.matches(".*[a-z].*")) {
                JOptionPane.showMessageDialog(parentComponent, "Password should contain at least one lowercase letter.","",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!_password.matches(".*[A-Z].*")) {
                JOptionPane.showMessageDialog(parentComponent, "Password should contain at least one uppercase letter.","",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!_password.matches(".*[@#$%^&+=].*")) {
                JOptionPane.showMessageDialog(parentComponent, "Password should contain at least one special character (@#$%^&+=).","",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (_password.length() < 8) {
                JOptionPane.showMessageDialog(parentComponent, "Password should be at least 8 characters long.","",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (_password.contains(" ")) {
                JOptionPane.showMessageDialog(parentComponent, "Password should not contain spaces.","",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return false;
        }
        return true;
    }
    
    public static boolean containsNumbers(String str) {
        Pattern pattern = Pattern.compile(".*\\d.*");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    
    public static String capitalizeEachWord(String fullName) {
        String[] words = fullName.split("\\s+"); 

        StringBuilder capitalizedFullName = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                capitalizedFullName.append(capitalizedWord).append(" ");
            }
        }
        return capitalizedFullName.toString().trim(); 
    }
    
    public static String formatNumber(double number) {
        String[] suffix = new String[]{"", "k", "M", "B", "T"};
        int index = 0;
        while (number >= 1000 && index < suffix.length - 1) {
            number /= 1000;
            index++;
        }
        return String.format("%.1f%s", number, suffix[index]);
    }
    public static String formatNumber(int number) {
        String[] suffix = new String[]{"", "k", "M", "B", "T"};
        int index = 0;
        while (number >= 1000 && index < suffix.length - 1) {
            number /= 1000;
            index++;
        }
        return String.format("%d%s", number, suffix[index]);        
    }
    
    public static String getCurrentDate(JDateChooser g_date) {
        if (g_date == null || g_date.getDate() == null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
            return dateFormat.format(new Date());
        } else {
            Date selectedDate = g_date.getDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
            return dateFormat.format(selectedDate);
        }
    }
    
    public static String get_AddedDate(JDateChooser g_date){
        SimpleDateFormat dateFormat = new SimpleDateFormat(Helper.dateFormat);
        if(g_date.getDate() != null){
            return dateFormat.format(g_date.getDate());
        }
        return java.sql.Date.valueOf(LocalDate.now()).toString();
    }
    
    public static String getCurrentDate(String format) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return currentDate.format(formatter);
    }

    public static String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return now.format(formatter);
    }
    
    private static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
        BufferedImage roundedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = roundedImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, image.getWidth(), image.getHeight(), cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return roundedImage;
    }
    
    public static void generateQRCodeImage(String text, String fileName, int width, int height, Component parent_component) {
        try {
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hintMap);

            String userHome = System.getProperty("user.home");
            String downloadFolder = userHome + "\\Downloads";
            File directory = new File(downloadFolder);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String filePath = downloadFolder + "\\" + fileName;

            int onColor = 0xFFC04D4D;
            
            int offColor = 0xFFF5CDCD;
            
            //White Background
            //int offColor = 0xFFFFFFFF;
            MatrixToImageConfig config = new MatrixToImageConfig(onColor, offColor);

            Path path = FileSystems.getDefault().getPath(filePath);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix, config);

            image = makeRoundedCorner(image, 25);

            ImageIO.write(image, "PNG", path.toFile());

            String message = "QR Code successfully generated!\nView in download folder now.";
            JOptionPane.showMessageDialog(parent_component, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (WriterException | IOException e) {
            String errorMessage = "Unable to generate QR Code:\n" + e.getMessage();
            JOptionPane.showMessageDialog(parent_component, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static String getSpinnerFromTable(javax.swing.JTable table){
        int editedRow = table.getEditingRow();
        int editedColumn = table.getEditingColumn();
        
        table.editCellAt(table.getSelectedRow(), table.getSelectedColumn());
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        
        String editedValue = null;
        
        if (editedRow != -1 && editedColumn != -1) {
            TableCellEditor editor = table.getCellEditor(editedRow, editedColumn);
            if (editor != null) {
                editor.stopCellEditing();
            }
            editedValue = model.getValueAt(editedRow, editedColumn).toString();
        }
        
        return editedValue;
    }
    
    public static String getComboxFromTable(JTable table, int colIdx) {
        int row = table.getSelectedRow();
        TableCellEditor editor = table.getCellEditor(row, colIdx);    
        if (editor instanceof DefaultCellEditor) {
            Component editorComponent = ((DefaultCellEditor) editor).getComponent();
            if (editorComponent instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox<?>) editorComponent;
                Object selectedItem = comboBox.getSelectedItem();
                if (selectedItem != null) {
                    return selectedItem.toString();
                }
            }
        }
        return ""; 
    }
    
    public static class IntCustomJSpinner extends DefaultCellEditor {

        private JSpinner input;

        public IntCustomJSpinner() {
            super(new JCheckBox());
            input = new JSpinner();
            SpinnerNumberModel numberModel = (SpinnerNumberModel) input.getModel();
            numberModel.setMinimum(0);
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor) input.getEditor();
            DefaultFormatter formatter = (DefaultFormatter) editor.getTextField().getFormatter();
            formatter.setCommitsOnValidEdit(true);
            editor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);

            editor.getTextField().addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                        e.consume();
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            super.getTableCellEditorComponent(table, value, isSelected, row, column);
            int qty = Integer.parseInt(value.toString());
            input.setValue(qty);
            return input;
        }

        @Override
        public Object getCellEditorValue() {
            return input.getValue();
        }
    }
    
    public static class DoubleCustomJSpinner extends DefaultCellEditor {

        private JSpinner input;

        public DoubleCustomJSpinner() {
            super(new JCheckBox());
            input = new JSpinner();
            SpinnerNumberModel numberModel = new SpinnerNumberModel(1.0, 0.0, Double.MAX_VALUE, 0.1); // Allow float values
            input.setModel(numberModel);
            JSpinner.NumberEditor editor = (JSpinner.NumberEditor) input.getEditor();
            DefaultFormatter formatter = (DefaultFormatter) editor.getTextField().getFormatter();
            formatter.setCommitsOnValidEdit(true);
            editor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);

            editor.getTextField().addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != '.' && c != '-') {
                        e.consume();
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            super.getTableCellEditorComponent(table, value, isSelected, row, column);
            float qty = Float.parseFloat(value.toString());
            input.setValue(qty);
            return input;
        }

        @Override
        public Object getCellEditorValue() {
            return input.getValue();
        }
    } 
}
