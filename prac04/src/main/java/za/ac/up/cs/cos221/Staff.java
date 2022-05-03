
package za.ac.up.cs.cos221;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;


public class Staff {

    private final Connection con;
    DefaultTableModel tblModel;

    public Staff(DefaultTableModel tblModel) throws SQLException {
    	this.tblModel = tblModel;
        connection temp = new connection();
        con = temp.openConnection();
    }

    public void read() throws SQLException {
    	tblModel.setRowCount(0);
        System.out.println("Reading staff data...");
        try(PreparedStatement stmt = con.prepareStatement("SELECT DISTINCT staff.first_name, staff.last_name, staff.active, address.address, address.address2, address.district, address.postal_code, address.phone, city.city" 
        		+ " FROM staff"
        		+ " INNER JOIN address ON staff.address_id=address.address_id" 
        		+ " INNER JOIN city ON address.city_id = city.city_id")){
        	ResultSet resultset = stmt.executeQuery();
			boolean empty = true;
			while(resultset.next()) {
				String fName = resultset.getString(1);
				String lName = resultset.getString(2);
				
				int activeNum = resultset.getInt(3);
				String address = resultset.getString(4);
				String address2 = resultset.getString(5);
				String district = resultset.getString(6);
				String postal_code = resultset.getString(7);
				String phone = resultset.getString(8);
				String city = resultset.getString(9);
				

				String active = "false";
				if(activeNum == 1) {
					active = "true";
				}
				String store = "Place holder";
				
				String data[] = {fName, lName, address, address2, district, city, postal_code, phone, store, active};
				tblModel.addRow(data);
				empty = false;
			}
			if(empty)
				System.out.println("(no data)");
		}
    }
    
    public void filter(int store_id) throws SQLException{
    	tblModel.setNumRows(0);
    	System.out.println("Filtering data...");
    	try(PreparedStatement stmt = con.prepareStatement("SELECT DISTINCT staff.first_name, staff.last_name, staff.active, address.address, address.address2, address.district, address.postal_code, address.phone, city.city" 
        		+ " FROM staff"
        		+ " INNER JOIN address ON staff.address_id=address.address_id" 
        		+ " INNER JOIN city ON address.city_id = city.city_id"
        		+ " WHERE store_id = ?")){
    		stmt.setInt(1, store_id);
    		ResultSet resultset = stmt.executeQuery();
			boolean empty = true;
			while(resultset.next()) {
				String fName = resultset.getString(1);
				String lName = resultset.getString(2);
				
				int activeNum = resultset.getInt(3);
				String address = resultset.getString(4);
				String address2 = resultset.getString(5);
				String district = resultset.getString(6);
				String postal_code = resultset.getString(7);
				String phone = resultset.getString(8);
				String city = resultset.getString(9);
				

				String active = "false";
				if(activeNum == 1) {
					active = "true";
				}
				String store = "Place holder";
				
				String data[] = {fName, lName, address, address2, district, city, postal_code, phone, store, active};
				tblModel.addRow(data);
				empty = false;
			}
			if(empty)
				System.out.println("(no data)");
    	}
    }
    
    public void closeCon() throws SQLException{
    	con.close();
    }
}
