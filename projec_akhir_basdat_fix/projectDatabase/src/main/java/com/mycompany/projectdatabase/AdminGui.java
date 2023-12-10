/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.projectdatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author FX506
 */
public class AdminGui extends javax.swing.JFrame {

    /**
     * Creates new form AdminGui
     */
    public AdminGui() {
        initComponents();
    }

    
private void createTable(String tableName, String column1, String type1, String column2, String type2, String primaryKey) {
    
    String url = "jdbc:sqlserver://localhost:1433;databaseName=jdk;user=basdat;password=1234;encrypt=true;trustServerCertificate=true;";
    String query = "CREATE TABLE " + tableName + " (" + 
                   column1 + "  " + type1 + ", " +
                   column2 + "  " + type2 + ", " +
                   "PRIMARY KEY (" + primaryKey + "))" ;

    try (Connection connection = DriverManager.getConnection(url);
         Statement statement = connection.createStatement()) {
        statement.execute(query);

        JOptionPane.showMessageDialog(this, "Create Was Successful");

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error creating table. Error Message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void insertData(String tableName, String pkColumn, String columnName, Object pkValue, Object value) {
    String url = "jdbc:sqlserver://localhost:1433;databaseName=jdk;user=basdat;password=1234;encrypt=true;trustServerCertificate=true;";

    String query = "INSERT INTO " + tableName + " (" + pkColumn + ", " + columnName + ") VALUES (?, ?)";

    try (Connection connection = DriverManager.getConnection(url);
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        if (pkValue instanceof String) {
            preparedStatement.setString(1, (String) pkValue);
        } else if (pkValue instanceof Integer) {
            preparedStatement.setInt(1, (Integer) pkValue);
        } else {
            preparedStatement.setObject(1, pkValue);
        }
        
        if (value instanceof String) {
            preparedStatement.setString(2, (String) value);
        } else if (value instanceof Integer) {
            preparedStatement.setInt(2, (Integer) value);
        } else {
            preparedStatement.setObject(1, value);
        }

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Insert Was Successful");
        } else {
            JOptionPane.showMessageDialog(this, "Insert Failed");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error inserting data into the database. Error Message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


private void deleteData(String tableName, String columnName, Object value, boolean deleteAll) {
    String url = "jdbc:sqlserver://localhost:1433;databaseName=jdk;user=basdat;password=1234;encrypt=true;trustServerCertificate=true;";

    // Menggunakan PreparedStatement untuk mencegah SQL injection
    String query;

    if (deleteAll) {
        // Hapus semua baris dari tabel
        query = "DELETE FROM " + tableName;
    } else {
        // Hapus baris berdasarkan nilai tertentu pada kolom tertentu
        query = "DELETE FROM " + tableName + " WHERE " + columnName + " = ?";
    }

    try (Connection connection = DriverManager.getConnection(url);
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        // Set nilai parameter sesuai dengan tipe data kolom
        if (!deleteAll) {
            if (value instanceof String) {
                preparedStatement.setString(1, (String) value);
            } else if (value instanceof Integer) {
                preparedStatement.setInt(1, (Integer) value);
            } else {
                JOptionPane.showMessageDialog(this, "No rows deleted. Data not found or an error occurred.");
                return;
            }
        }
        
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Delete Was Successful");
        } else {
            JOptionPane.showMessageDialog(this, "No rows deleted. Data not found or an error occurred.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error deleting data from the database. Error Message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


private void alterData(String tableName, String columnName, String type){

    String url = "jdbc:sqlserver://localhost:1433;databaseName=jdk;user=basdat;password=1234;encrypt=true;trustServerCertificate=true;";
    String query = "ALTER TABLE "+ tableName + " Add "+columnName+" "+type;

    try (Connection connection = DriverManager.getConnection(url);
        Statement statement = connection.createStatement()) {
        statement.execute(query);

        JOptionPane.showMessageDialog(this, "Add column Table Was Successful");

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error creating table. Error Message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void dropData(String tableName){
    
    String url = "jdbc:sqlserver://localhost:1433;databaseName=jdk;user=basdat;password=1234;encrypt=true;trustServerCertificate=true;";
    String query = "DROP TABLE "+ tableName;

    try (Connection connection = DriverManager.getConnection(url);
        Statement statement = connection.createStatement()) {
        statement.execute(query);

        JOptionPane.showMessageDialog(this, "Drop Table Was Successful");

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error creating table. Error Message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

public void dropColumn(String tableName, String columnName) {
    
    String url = "jdbc:sqlserver://localhost:1433;databaseName=jdk;user=basdat;password=1234;encrypt=true;trustServerCertificate=true;";    
    String query = "ALTER TABLE " + tableName + " DROP COLUMN " + columnName;

    try (Connection connection = DriverManager.getConnection(url);
        Statement statement = connection.createStatement()) {

        statement.execute(query);

        JOptionPane.showMessageDialog(null, "Drop Column Was Successful");

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error dropping column. Error Message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
public void executeDropOperation(String tableName, String textFieldColumn) {
    if (textFieldColumn == null || textFieldColumn.isEmpty()) {
        dropData(tableName);
    } else {
        dropColumn(tableName, textFieldColumn);
    }
}



private void updateData(String tableName, String columnSet, Object valueSet, String columnCondition, Object valueCondition){
    
    String url = "jdbc:sqlserver://localhost:1433;databaseName=jdk;user=basdat;password=1234;encrypt=true;trustServerCertificate=true;";

        // Perhatikan bahwa query CREATE TABLE tidak memerlukan eksekusi ResultSet
    String query = "UPDATE " + tableName + " SET " + columnSet + " = ? WHERE " + columnCondition + " = ?";

    try (Connection connection = DriverManager.getConnection(url);
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            if (valueSet != null) {
                if (valueSet instanceof String) {
                    preparedStatement.setString(1, (String) valueSet);
                } else if (valueSet instanceof Integer) {
                    preparedStatement.setInt(1, (Integer) valueSet);
                } else {
                // Handle tipe data lain jika diperlukan
                    preparedStatement.setObject(1, valueSet);
            }
        } else {
            // Handle nilai null jika diperlukan
               preparedStatement.setObject(1, null);
        }

        if (valueCondition != null) {
                if (valueCondition instanceof String) {
                    preparedStatement.setString(2, (String) valueCondition);
                } else if (valueCondition instanceof Integer) {
                    preparedStatement.setInt(2, (Integer) valueCondition);
                } else {
                    // Handle tipe data lain jika diperlukan
                    preparedStatement.setObject(2, valueCondition);
                }
        } else {
            // Handle nilai null jika diperlukan
            preparedStatement.setObject(2, null);
        }

        // Eksekusi query UPDATE
        int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Update Was Successful");
            } else {
                JOptionPane.showMessageDialog(this, "Update Failed");
            }

            } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating data. SQL State: " + e.getSQLState() + ", Error Code: " + e.getErrorCode() + ", Error Message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

}

private void manipulationDatabase(String tabel){
        String url = "jdbc:sqlserver://localhost:1433;databaseName=jdk;user=basdat;password=1234;encrypt=true;trustServerCertificate=true;";

        String query = "SELECT * from "+tabel;
        try (Connection connection = DriverManager.getConnection(url); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

            DefaultTableModel model = (DefaultTableModel) tableDML.getModel();
            model.setRowCount(0); // Menghapus semua baris yang ada

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Menambahkan judul kolom (header) ke model tabel
            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            model.setColumnIdentifiers(columnNames);

            while (resultSet.next()) {
                // Menambahkan baris baru ke model tabel
                Vector<Object> rowData = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.add(resultSet.getObject(i));
                }
                model.addRow(rowData);
            }            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving and sorting data from the database. Error Message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    
private void informationSchema(String tabel){
        String url = "jdbc:sqlserver://localhost:1433;databaseName=jdk;user=basdat;password=1234;encrypt=true;trustServerCertificate=true;";

        String query = "SELECT * from "+tabel;
        try (Connection connection = DriverManager.getConnection(url); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

        DefaultTableModel model = (DefaultTableModel) tableDDL.getModel();
        model.setRowCount(0); // Menghapus semua baris yang ada

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Menambahkan judul kolom (header) ke model tabel
        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        model.setColumnIdentifiers(columnNames);

        while (resultSet.next()) {
            // Menambahkan baris baru ke model tabel
            Vector<Object> rowData = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                rowData.add(resultSet.getObject(i));
            }
            model.addRow(rowData);
        }

            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving and sorting data from the database. Error Message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
}
    
        
private void updateDataTable(String tabel){
    String url = "jdbc:sqlserver://localhost:1433;databaseName=jdk;user=basdat;password=1234;encrypt=true;trustServerCertificate=true;";

        String query = "SELECT * from "+tabel;
        try (Connection connection = DriverManager.getConnection(url); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

        DefaultTableModel model = (DefaultTableModel) updateTable.getModel();
        model.setRowCount(0); // Menghapus semua baris yang ada

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Menambahkan judul kolom (header) ke model tabel
        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        model.setColumnIdentifiers(columnNames);

        while (resultSet.next()) {
            // Menambahkan baris baru ke model tabel
            Vector<Object> rowData = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                rowData.add(resultSet.getObject(i));
            }
            model.addRow(rowData);
        }

            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving and sorting data from the database. Error Message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        cdPage = new javax.swing.JPanel();
        tableName = new javax.swing.JLabel();
        column1Name = new javax.swing.JLabel();
        column1Type = new javax.swing.JLabel();
        column2Name = new javax.swing.JLabel();
        column2Type = new javax.swing.JLabel();
        tableNameField = new javax.swing.JTextField();
        column1NameField = new javax.swing.JTextField();
        column1TypeField = new javax.swing.JTextField();
        column2NameField = new javax.swing.JTextField();
        column2TypeField = new javax.swing.JTextField();
        createBtn = new javax.swing.JToggleButton();
        primaryKey = new javax.swing.JLabel();
        keyField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableDDL = new javax.swing.JTable();
        informationSchema = new javax.swing.JToggleButton();
        addPage = new javax.swing.JPanel();
        tableName1 = new javax.swing.JLabel();
        columnName = new javax.swing.JLabel();
        columnValue = new javax.swing.JLabel();
        tableField = new javax.swing.JTextField();
        columnField = new javax.swing.JTextField();
        columnValueField = new javax.swing.JTextField();
        gta = new javax.swing.JScrollPane();
        tableDML = new javax.swing.JTable();
        insertBtn = new javax.swing.JToggleButton();
        viewBtn = new javax.swing.JToggleButton();
        deleteBtn = new javax.swing.JToggleButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        pkColumn = new javax.swing.JTextField();
        pkValue = new javax.swing.JTextField();
        updatePage = new javax.swing.JPanel();
        tableName2 = new javax.swing.JLabel();
        columnName1 = new javax.swing.JLabel();
        columnType1 = new javax.swing.JLabel();
        columnValue1 = new javax.swing.JLabel();
        tableField1 = new javax.swing.JTextField();
        columnField1 = new javax.swing.JTextField();
        setColumn = new javax.swing.JTextField();
        conditionValue = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        columnType2 = new javax.swing.JLabel();
        columnValue2 = new javax.swing.JLabel();
        conditionColumn = new javax.swing.JTextField();
        setValue = new javax.swing.JTextField();
        updateBtn = new javax.swing.JToggleButton();
        modifyBtn = new javax.swing.JToggleButton();
        dropBtn = new javax.swing.JToggleButton();
        gta1 = new javax.swing.JScrollPane();
        updateTable = new javax.swing.JTable();
        view2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        columnType = new javax.swing.JTextField();
        backBtn = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tableName.setText("Table_name           :");

        column1Name.setText("Column 1_name     :");

        column1Type.setText("Column 1_type       :");

        column2Name.setText("Column 2_name     :");

        column2Type.setText("Column 2_type       :");

        column1NameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                column1NameFieldActionPerformed(evt);
            }
        });

        column2TypeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                column2TypeFieldActionPerformed(evt);
            }
        });

        createBtn.setText("Create");
        createBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createBtnActionPerformed(evt);
            }
        });

        primaryKey.setText("Primary Key            :");

        keyField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keyFieldActionPerformed(evt);
            }
        });

        tableDDL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tableDDL);

        informationSchema.setText("View");
        informationSchema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                informationSchemaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cdPageLayout = new javax.swing.GroupLayout(cdPage);
        cdPage.setLayout(cdPageLayout);
        cdPageLayout.setHorizontalGroup(
            cdPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cdPageLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(cdPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(column1Name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tableName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(column2Name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(column2Type, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(column1Type, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(primaryKey, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(cdPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tableNameField, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                    .addComponent(column1NameField)
                    .addComponent(column1TypeField)
                    .addComponent(column2NameField)
                    .addComponent(column2TypeField)
                    .addComponent(keyField)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cdPageLayout.createSequentialGroup()
                        .addComponent(createBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(informationSchema, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        cdPageLayout.setVerticalGroup(
            cdPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cdPageLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(cdPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tableName)
                    .addComponent(tableNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cdPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(column1Name)
                    .addComponent(column1NameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cdPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(column1Type)
                    .addComponent(column1TypeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cdPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(column2Name)
                    .addComponent(column2NameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cdPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(column2Type)
                    .addComponent(column2TypeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(cdPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(primaryKey)
                    .addComponent(keyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(cdPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createBtn)
                    .addComponent(informationSchema))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cdPageLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Create", cdPage);

        tableName1.setText("Table_name               :");

        columnName.setText("Column_name            :");

        columnValue.setText("Column_value             : ");

        tableDML.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        gta.setViewportView(tableDML);

        insertBtn.setText("Insert");
        insertBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertBtnActionPerformed(evt);
            }
        });

        viewBtn.setText("View");
        viewBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewBtnActionPerformed(evt);
            }
        });

        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        jLabel3.setText("Primary Key Column  :");

        jLabel4.setText("Primary Key Value       :");

        pkColumn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pkColumnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addPageLayout = new javax.swing.GroupLayout(addPage);
        addPage.setLayout(addPageLayout);
        addPageLayout.setHorizontalGroup(
            addPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addPageLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(addPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(columnName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(columnValue, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                    .addComponent(tableName1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(addPageLayout.createSequentialGroup()
                        .addGroup(addPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addPageLayout.createSequentialGroup()
                        .addComponent(insertBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(viewBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(columnField)
                    .addComponent(columnValueField)
                    .addComponent(tableField, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                    .addComponent(pkColumn)
                    .addComponent(pkValue))
                .addGap(38, 38, 38)
                .addComponent(gta, javax.swing.GroupLayout.PREFERRED_SIZE, 632, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        addPageLayout.setVerticalGroup(
            addPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(addPageLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(addPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tableName1)
                    .addComponent(tableField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(addPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(columnName)
                    .addComponent(columnField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(columnValue)
                    .addComponent(columnValueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pkColumn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(pkValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(addPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(insertBtn)
                    .addComponent(viewBtn)
                    .addComponent(deleteBtn))
                .addContainerGap(107, Short.MAX_VALUE))
            .addGroup(addPageLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(gta, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Add", addPage);

        tableName2.setText("Table_name         :");

        columnName1.setText("Column_name      :");

        columnType1.setText("Condition_column  :");

        columnValue1.setText("Set_column            :");

        columnField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                columnField1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Viner Hand ITC", 0, 12)); // NOI18N
        jLabel1.setText("For update data, please field below");

        columnType2.setText("Set_value                :");

        columnValue2.setText("Condition_value      :");

        setValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setValueActionPerformed(evt);
            }
        });

        updateBtn.setText("Update");
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });

        modifyBtn.setText("Modifiy");
        modifyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyBtnActionPerformed(evt);
            }
        });

        dropBtn.setText("Drop");
        dropBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dropBtnActionPerformed(evt);
            }
        });

        updateTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        gta1.setViewportView(updateTable);

        view2.setText("View");
        view2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view2ActionPerformed(evt);
            }
        });

        jLabel5.setText("Column_type        :");

        columnType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                columnTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout updatePageLayout = new javax.swing.GroupLayout(updatePage);
        updatePage.setLayout(updatePageLayout);
        updatePageLayout.setHorizontalGroup(
            updatePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatePageLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(updatePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(updatePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(columnValue2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(columnType1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(columnType2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(columnValue1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tableName2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(columnName1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(updatePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(updatePageLayout.createSequentialGroup()
                        .addGroup(updatePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(setColumn, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                            .addComponent(setValue)
                            .addComponent(conditionValue, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tableField1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(conditionColumn))
                        .addGap(50, 50, 50))
                    .addGroup(updatePageLayout.createSequentialGroup()
                        .addGroup(updatePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(updatePageLayout.createSequentialGroup()
                                .addComponent(view2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(modifyBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(dropBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(columnType)
                            .addComponent(columnField1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)))
                .addComponent(gta1, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        updatePageLayout.setVerticalGroup(
            updatePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatePageLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(updatePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(updatePageLayout.createSequentialGroup()
                        .addComponent(tableName2)
                        .addGap(18, 18, 18)
                        .addGroup(updatePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(columnName1)
                            .addComponent(columnField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(updatePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(columnType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(updatePageLayout.createSequentialGroup()
                        .addComponent(tableField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75)
                        .addComponent(jLabel2)))
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(updatePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(columnValue1)
                    .addComponent(setColumn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(updatePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(columnType2)
                    .addComponent(setValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(updatePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(columnType1)
                    .addComponent(conditionColumn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(updatePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(columnValue2)
                    .addComponent(conditionValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(updatePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateBtn)
                    .addComponent(modifyBtn)
                    .addComponent(dropBtn)
                    .addComponent(view2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, updatePageLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(gta1, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Update", updatePage);

        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2))
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backBtn)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void column2TypeFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_column2TypeFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_column2TypeFieldActionPerformed

    private void keyFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keyFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_keyFieldActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        // TODO add your handling code here:
        ChooseAs role = new ChooseAs();
         role.setVisible(true);
         dispose();
    }//GEN-LAST:event_backBtnActionPerformed

    private void createBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createBtnActionPerformed
        // TODO add your handling code here:
         String tabel = tableNameField.getText();
         String column1 = column1NameField.getText();
         String column2 = column2NameField.getText();
         String type1 = column1TypeField.getText();
         String type2 = column2TypeField.getText();
         String key = keyField.getText();
         
         createTable(tabel, column1, type1, column2, type2, key);
    }//GEN-LAST:event_createBtnActionPerformed

    private void column1NameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_column1NameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_column1NameFieldActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:
        String tabel = tableField.getText();
        String pkcolumn = pkColumn.getText();
        String pkvalue = pkValue.getText();
        
        
        if (pkcolumn.isEmpty() && pkvalue.isEmpty()) {
            deleteData(tabel, null, null, true);
        } else {
            deleteData(tabel, pkcolumn, pkvalue, false);
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void viewBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewBtnActionPerformed
        // TODO add your handling code here:
        String tabel = tableField.getText();
        manipulationDatabase(tabel);
    }//GEN-LAST:event_viewBtnActionPerformed

    private void insertBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertBtnActionPerformed
        // TODO add your handling code here:
        String tabel = tableField.getText();
        String column = columnField.getText();
        String value = columnValueField.getText();
        String pkcolumn = pkColumn.getText();
        String pkvalue = pkValue.getText();
        
        insertData(tabel,pkcolumn, column,pkvalue, value);
    }//GEN-LAST:event_insertBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        // TODO add your handling code here:
        String tabel = tableField1.getText();
        String column = setColumn.getText();
        String value = setValue.getText();
        String condition = conditionColumn.getText();
        String conditionV = conditionValue.getText();
        
        updateData(tabel, column, value, condition, conditionV);     
    }//GEN-LAST:event_updateBtnActionPerformed

    private void dropBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dropBtnActionPerformed
        // TODO add your handling code here:
         String tabel = tableField1.getText();
         String column = columnField1.getText();
         
         executeDropOperation(tabel, column);
    }//GEN-LAST:event_dropBtnActionPerformed

    private void modifyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyBtnActionPerformed
        // TODO add your handling code here:
        String tabel = tableField1.getText();
        String column = columnField1.getText();
        String value = columnType.getText();
        
        alterData(tabel, column, value);
    }//GEN-LAST:event_modifyBtnActionPerformed

    private void informationSchemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_informationSchemaActionPerformed
        // TODO add your handling code here:
        String tabel = tableNameField.getText();       
        informationSchema(tabel);
    }//GEN-LAST:event_informationSchemaActionPerformed

    private void setValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_setValueActionPerformed

    private void view2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view2ActionPerformed
        // TODO add your handling code here:
        String tabel = tableField1.getText();
        updateDataTable(tabel);
    }//GEN-LAST:event_view2ActionPerformed

    private void pkColumnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pkColumnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pkColumnActionPerformed

    private void columnTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_columnTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_columnTypeActionPerformed

    private void columnField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_columnField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_columnField1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminGui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel addPage;
    private javax.swing.JToggleButton backBtn;
    private javax.swing.JPanel cdPage;
    private javax.swing.JLabel column1Name;
    private javax.swing.JTextField column1NameField;
    private javax.swing.JLabel column1Type;
    private javax.swing.JTextField column1TypeField;
    private javax.swing.JLabel column2Name;
    private javax.swing.JTextField column2NameField;
    private javax.swing.JLabel column2Type;
    private javax.swing.JTextField column2TypeField;
    private javax.swing.JTextField columnField;
    private javax.swing.JTextField columnField1;
    private javax.swing.JLabel columnName;
    private javax.swing.JLabel columnName1;
    private javax.swing.JTextField columnType;
    private javax.swing.JLabel columnType1;
    private javax.swing.JLabel columnType2;
    private javax.swing.JLabel columnValue;
    private javax.swing.JLabel columnValue1;
    private javax.swing.JLabel columnValue2;
    private javax.swing.JTextField columnValueField;
    private javax.swing.JTextField conditionColumn;
    private javax.swing.JTextField conditionValue;
    private javax.swing.JToggleButton createBtn;
    private javax.swing.JToggleButton deleteBtn;
    private javax.swing.JToggleButton dropBtn;
    private javax.swing.JScrollPane gta;
    private javax.swing.JScrollPane gta1;
    private javax.swing.JToggleButton informationSchema;
    private javax.swing.JToggleButton insertBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField keyField;
    private javax.swing.JToggleButton modifyBtn;
    private javax.swing.JTextField pkColumn;
    private javax.swing.JTextField pkValue;
    private javax.swing.JLabel primaryKey;
    private javax.swing.JTextField setColumn;
    private javax.swing.JTextField setValue;
    private javax.swing.JTable tableDDL;
    private javax.swing.JTable tableDML;
    private javax.swing.JTextField tableField;
    private javax.swing.JTextField tableField1;
    private javax.swing.JLabel tableName;
    private javax.swing.JLabel tableName1;
    private javax.swing.JLabel tableName2;
    private javax.swing.JTextField tableNameField;
    private javax.swing.JToggleButton updateBtn;
    private javax.swing.JPanel updatePage;
    private javax.swing.JTable updateTable;
    private javax.swing.JButton view2;
    private javax.swing.JToggleButton viewBtn;
    // End of variables declaration//GEN-END:variables
}
