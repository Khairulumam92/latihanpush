// hospital/gui/DoctorPanel.java
package hospital.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import hospital.model.Doctor;
import hospital.service.DoctorService;
import hospital.exception.ValidationException;

public class DoctorPanel extends JPanel {
    private JTextField nameField;
    private JTextField specializationField;
    private JTextField phoneField;
    private JTable doctorTable;
    private DoctorService doctorService;
    private JButton addButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JTextField searchField;
    
    // Components for schedule
    private JComboBox<String> startDayCombo;
    private JComboBox<String> endDayCombo;
    private JComboBox<String> startHourCombo;
    private JComboBox<String> endHourCombo;
    private JPanel schedulePanel;
    public String searchText;
    
    public DoctorPanel() {
        doctorService = new DoctorService();
        initComponents();
        setupLayout();
        addListeners();
    }
    
    private void initComponents() {
        // Initialize form fields
        nameField = createStyledTextField(20);
        specializationField = createStyledTextField(20);
        phoneField = createStyledTextField(15);
        searchField = createStyledTextField(20);
        
        // Initialize schedule components
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[] hours = getHours();
        
        startDayCombo = new JComboBox<>(days);
        endDayCombo = new JComboBox<>(days);
        startHourCombo = new JComboBox<>(hours);
        endHourCombo = new JComboBox<>(hours);
        
        // Style combo boxes
        styleComboBox(startDayCombo);
        styleComboBox(endDayCombo);
        styleComboBox(startHourCombo);
        styleComboBox(endHourCombo);
        
        // Create schedule panel
        schedulePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        schedulePanel.add(startDayCombo);
        schedulePanel.add(new JLabel("-"));
        schedulePanel.add(endDayCombo);
        schedulePanel.add(new JLabel(":"));
        schedulePanel.add(startHourCombo);
        schedulePanel.add(new JLabel("-"));
        schedulePanel.add(endHourCombo);
        
        // Initialize buttons
        addButton = createStyledButton("Add Doctor", new Color(46, 204, 113));
        deleteButton = createStyledButton("Delete", new Color(231, 76, 60));
        clearButton = createStyledButton("Clear", new Color(52, 73, 94));
        
        // Initialize table
        doctorTable = new JTable(doctorService.getTableModel());
        doctorTable.setRowHeight(25);
        doctorTable.setShowGrid(true);
        doctorTable.setGridColor(Color.LIGHT_GRAY);
        doctorTable.getTableHeader().setBackground(new Color(52, 73, 94));
        doctorTable.getTableHeader().setForeground(Color.BLACK);
        doctorTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    private String[] getHours() {
        String[] hours = new String[24];
        for (int i = 0; i < 24; i++) {
            hours[i] = String.format("%02d:00", i);
        }
        return hours;
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBackground(Color.BLACK);
        // Adjust width based on content
        comboBox.setPreferredSize(new Dimension(
            comboBox == startDayCombo || comboBox == endDayCombo ? 100 : 70,
            30));
    }
    
    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        return field;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchPanel.add(new JLabel("Search Doctor: "));
        searchPanel.add(searchField);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(createStyledTitledBorder("Doctor Information"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Add form components
        addFormField(formPanel, "Name:", nameField, gbc, 0);
        addFormField(formPanel, "Specialization:", specializationField, gbc, 1);
        addFormField(formPanel, "Schedule:", schedulePanel, gbc, 2);
        addFormField(formPanel, "Phone:", phoneField, gbc, 3);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(clearButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(addButton);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(buttonPanel, gbc);
        
        // Table Panel
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        scrollPane.setBorder(createStyledTitledBorder("Doctor List"));
        
        // Main layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private TitledBorder createStyledTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 73, 94)), 
            title,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(52, 73, 94));
    }
    
    private void addFormField(JPanel panel, String label, JComponent field, 
                            GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }
    
    private void addListeners() {
        addButton.addActionListener(e -> addDoctor());
        clearButton.addActionListener(e -> clearForm());
        deleteButton.addActionListener(e -> deleteDoctor());
        
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { search(); }
        });
    }
    
    private void addDoctor() {
        try {
            validateInput();
            String schedule = String.format("%s-%s: %s-%s",
                startDayCombo.getSelectedItem().toString().substring(0, 3),
                endDayCombo.getSelectedItem().toString().substring(0, 3),
                startHourCombo.getSelectedItem(),
                endHourCombo.getSelectedItem());
                
            Doctor doctor = new Doctor(
                null,
                nameField.getText().trim(),
                specializationField.getText().trim(),
                schedule,
                phoneField.getText().trim()
            );
            doctorService.addDoctor(doctor);
            clearForm();
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this doctor?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                doctorService.deleteDoctor(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a doctor to delete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void search() {
        searchText = searchField.getText().toLowerCase();
    }
    
    private void validateInput() throws ValidationException {
        if (nameField.getText().trim().isEmpty()) {
            throw new ValidationException("Name is required");
        }
        if (specializationField.getText().trim().isEmpty()) {
            throw new ValidationException("Specialization is required");
        }
        if (phoneField.getText().trim().isEmpty()) {
            throw new ValidationException("Phone number is required");
        }
        if (!phoneField.getText().trim().matches("\\d{10,12}")) {
            throw new ValidationException("Phone number must be between 10-12 digits");
        }
        // Validasi jadwal
        if (startDayCombo.getSelectedIndex() > endDayCombo.getSelectedIndex()) {
            throw new ValidationException("End day cannot be before start day");
        }
        String startTime = startHourCombo.getSelectedItem().toString();
        String endTime = endHourCombo.getSelectedItem().toString();
        if (startTime.compareTo(endTime) >= 0) {
            throw new ValidationException("End time must be after start time");
        }
    }
    
    private void clearForm() {
        nameField.setText("");
        specializationField.setText("");
        startDayCombo.setSelectedIndex(0);
        endDayCombo.setSelectedIndex(0);
        startHourCombo.setSelectedIndex(8); // Default to 08:00
        endHourCombo.setSelectedIndex(16);  // Default to 16:00
        phoneField.setText("");
        nameField.requestFocus();
    }
}