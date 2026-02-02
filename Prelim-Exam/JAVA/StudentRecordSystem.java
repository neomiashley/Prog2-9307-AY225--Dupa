/*
Programmer Identifier: Dupa, Neomi Ashley Sabien I. - 252460546
Student Record System - Java Swing Implementation
*/

package JAVA;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class StudentRecordSystem extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField idField, firstNameField, lastNameField;
    private JTextField lab1Field, lab2Field, lab3Field, prelimField, attendanceField;

    private JButton addButton, deleteButton;

    // Track selected row for UPDATE
    private int selectedRowIndex = -1;

    public StudentRecordSystem() {
        setTitle("Student Records - Dupa, Neomi Ashley Sabien I. - 252460546");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        initComponents();
        loadCSVData();

        setVisible(true);
    }

    private void initComponents() {

        String[] columns = {
            "Student ID", "First Name", "Last Name",
            "Lab 1", "Lab 2", "Lab 3", "Prelim", "Attendance"
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Click row → load data into fields
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRowIndex = table.getSelectedRow();
                if (selectedRowIndex != -1) {
                    idField.setText(tableModel.getValueAt(selectedRowIndex, 0).toString());
                    firstNameField.setText(tableModel.getValueAt(selectedRowIndex, 1).toString());
                    lastNameField.setText(tableModel.getValueAt(selectedRowIndex, 2).toString());
                    lab1Field.setText(tableModel.getValueAt(selectedRowIndex, 3).toString());
                    lab2Field.setText(tableModel.getValueAt(selectedRowIndex, 4).toString());
                    lab3Field.setText(tableModel.getValueAt(selectedRowIndex, 5).toString());
                    prelimField.setText(tableModel.getValueAt(selectedRowIndex, 6).toString());
                    attendanceField.setText(tableModel.getValueAt(selectedRowIndex, 7).toString());

                    addButton.setText("Update Record");
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Record"));

        idField = new JTextField(8);
        firstNameField = new JTextField(8);
        lastNameField = new JTextField(8);
        lab1Field = new JTextField(3);
        lab2Field = new JTextField(3);
        lab3Field = new JTextField(3);
        prelimField = new JTextField(3);
        attendanceField = new JTextField(3);

        inputPanel.add(new JLabel("Student ID:")); inputPanel.add(idField);
        inputPanel.add(new JLabel("First Name:")); inputPanel.add(firstNameField);
        inputPanel.add(new JLabel("Last Name:")); inputPanel.add(lastNameField);
        inputPanel.add(new JLabel("Lab 1:")); inputPanel.add(lab1Field);
        inputPanel.add(new JLabel("Lab 2:")); inputPanel.add(lab2Field);
        inputPanel.add(new JLabel("Lab 3:")); inputPanel.add(lab3Field);
        inputPanel.add(new JLabel("Prelim:")); inputPanel.add(prelimField);
        inputPanel.add(new JLabel("Attendance:")); inputPanel.add(attendanceField);

        addButton = new JButton("Add Record");
        addButton.addActionListener(e -> addOrUpdateRecord());
        inputPanel.add(addButton);

        deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteRecord());
        inputPanel.add(deleteButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }

    private void loadCSVData() {
        try (BufferedReader br = new BufferedReader(new FileReader("class_records.csv"))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data.length >= 8) {
                    tableModel.addRow(data);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "CSV file not found. Program will still run.",
                "Notice",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addOrUpdateRecord() {
        String id = idField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();

        if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Student ID, First Name, and Last Name are required.",
                "Input Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String lab1 = lab1Field.getText().trim().isEmpty() ? "0" : lab1Field.getText().trim();
        String lab2 = lab2Field.getText().trim().isEmpty() ? "0" : lab2Field.getText().trim();
        String lab3 = lab3Field.getText().trim().isEmpty() ? "0" : lab3Field.getText().trim();
        String prelim = prelimField.getText().trim().isEmpty() ? "0" : prelimField.getText().trim();
        String attendance = attendanceField.getText().trim().isEmpty() ? "0" : attendanceField.getText().trim();

        if (selectedRowIndex == -1) {
            // ADD
            tableModel.addRow(new String[]{
                id, firstName, lastName,
                lab1, lab2, lab3, prelim, attendance
            });
            JOptionPane.showMessageDialog(this, "Record added successfully!");
        } else {
            // UPDATE
            tableModel.setValueAt(id, selectedRowIndex, 0);
            tableModel.setValueAt(firstName, selectedRowIndex, 1);
            tableModel.setValueAt(lastName, selectedRowIndex, 2);
            tableModel.setValueAt(lab1, selectedRowIndex, 3);
            tableModel.setValueAt(lab2, selectedRowIndex, 4);
            tableModel.setValueAt(lab3, selectedRowIndex, 5);
            tableModel.setValueAt(prelim, selectedRowIndex, 6);
            tableModel.setValueAt(attendance, selectedRowIndex, 7);

            JOptionPane.showMessageDialog(this, "Record updated successfully!");
        }

        clearFields();
    }

    private void deleteRecord() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a record to delete.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this record?",
            "Confirm",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
            clearFields();
        }
    }

    private void clearFields() {
        idField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        lab1Field.setText("");
        lab2Field.setText("");
        lab3Field.setText("");
        prelimField.setText("");
        attendanceField.setText("");

        selectedRowIndex = -1;
        table.clearSelection();
        addButton.setText("Add Record");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(StudentRecordSystem::new);
    }
}
