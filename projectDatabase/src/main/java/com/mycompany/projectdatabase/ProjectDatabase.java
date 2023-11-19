/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.projectdatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 *
 * @author FX506
 */
public class ProjectDatabase {

    public static void main(String[] args) {
        Gui gui = new Gui();
        gui.setVisible(true);
//        String url = "jdbc:sqlserver://localhost:1433;databaseName=jdk;user=basdat;password=1234;encrypt=true;trustServerCertificate=true;";
//        ResultSet rs = null;
//        try(Connection connection = DriverManager.getConnection(url)){
//            String query = "SELECT TOP 10 pembayaran.no_transaksi, pelanggan.id_pelanggan, pelanggan.nama_pelanggan, barang.barcode, barang.nama_barang, pembayaran.tot_bayar " +
//               "FROM pembayaran " +
//               "JOIN pelanggan ON pembayaran.id_pelanggan = pelanggan.id_pelanggan " +
//               "JOIN barang ON barang.barcode = pembayaran.barcode " +
//               "ORDER BY tot_bayar ASC ";
//             Statement statement = connection.createStatement();
//             rs = statement.executeQuery(query);
//             while(rs.next()){
//                System.out.println(rs.getString("tot_bayar")+"\t"+ rs.getString(2)+"\t"+ rs.getString(3)+"\t"+ rs.getString(4)+"\t"+ rs.getString(5));
//             }
//                 
//        }catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
    }
}
