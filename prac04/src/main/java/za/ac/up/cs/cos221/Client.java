
package za.ac.up.cs.cos221;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

public class Client {
    private connection conClass;
    private Connection con;
    private DefaultTableModel tblModel;
    
    public Client(DefaultTableModel tblModel) throws SQLException{
        this.tblModel = tblModel;
        conClass = new connection();
        con = conClass.openConnection();
    }
    
    public void add(String FName, String LName, String Email, String Address, String District, String City, String Postal_code, String Phone, int Store)throws SQLException{
		System.out.println("Creating new customer...");
		
		int city_id = getCity(City);
		int address_id = createAddress(Address, District, city_id, Postal_code, Phone);
		try(PreparedStatement stmt = con.prepareStatement("INSERT INTO customer(store_id, first_name, last_name, email, address_id, active)"
														  + " VALUES(?, ?, ?, ?, ?, 1)"
															)){
			stmt.setInt(1, Store);
			stmt.setString(2, FName);
			stmt.setString(3, LName);
			stmt.setString(4, Email);
			stmt.setInt(5, address_id);
			stmt.executeQuery();
		}
    	read();
    }
    
    public int getCity(String city) throws SQLException{
    	int id = -1;
        try(PreparedStatement stmt = con.prepareStatement("SELECT DISTINCT city_id"
        		+ " FROM city"
        		+ " WHERE city.city = ?")){
        	stmt.setString(1, city);
        	ResultSet resultset = stmt.executeQuery();
        	if(resultset.next()) {
        		id = resultset.getInt(1);
        	}
        }
        return id;
    }
    
    public int createAddress(String address, String district, int city_id, String postal_code, String phone) throws SQLException{
        try(PreparedStatement stmt = con.prepareStatement("INSERT INTO address(address, district, city_id, postal_code, phone)"
				  + " VALUES(?, ?, ?, ?, ?)")){
        	stmt.setString(1, address);
        	stmt.setString(2, district);
        	stmt.setInt(3, city_id);
        	stmt.setString(4, postal_code);
        	stmt.setString(5, phone);
        	stmt.executeQuery();
        }
        int address_id = getAddress(address);
    	return address_id;
    }
    
    public int getAddress(String address) throws SQLException{
    	int address_id = -1;
        try(PreparedStatement stmt = con.prepareStatement("SELECT address_id"
				  + " FROM address"
				  + " WHERE address = ?")){
        	stmt.setString(1, address);
        	ResultSet resultset = stmt.executeQuery();
        	if(resultset.next()) {
        		address_id = resultset.getInt(1);
        	}
        	
        }
    	return address_id;
    }
    
    public int getAddress_id(String email) throws SQLException{
    	int address_id = -1;
        try(PreparedStatement stmt = con.prepareStatement("SELECT address_id"
				  + " FROM customer"
				  + " WHERE email = ?")){
        	stmt.setString(1, email);
        	ResultSet resultset = stmt.executeQuery();
        	if(resultset.next()) {
        		address_id = resultset.getInt(1);
        	}
        	
        }
    	return address_id;
    }
    
    public void updateAddress(String Email, String nAddress) throws SQLException{
        int address_id = getAddress_id(Email);
    	try(PreparedStatement stmt = con.prepareStatement("UPDATE address"
				+ " SET address = ?"
				+ " WHERE address_id = ?")){
    			stmt.setString(1, nAddress);
    			stmt.setInt(2, address_id);
    			int rows = stmt.executeUpdate();
    			System.out.println(rows + " rows changed");
    			read();
    	}
    }
    
    public void updateActive(String email, int active) throws SQLException{
    	System.out.println("Updating " + email + " active status...");
    	try(PreparedStatement stmt = con.prepareStatement("UPDATE customer"
    													+ " SET active = ?"
    													+ " WHERE email = ?")){
    		stmt.setInt(1, active);
    		stmt.setString(2, email);
    		int rows = stmt.executeUpdate();
    		System.out.println(rows + " rows changed");
    		read();
    	}
    }
    private void disable() throws SQLException{
		try(PreparedStatement stmt = con.prepareStatement("SET SQL_SAFE_UPDATES = 0")){
			stmt.executeUpdate();
		}
    }
    
    private void enable() throws SQLException{
		try(PreparedStatement stmt = con.prepareStatement("SET SQL_SAFE_UPDATES = 1")){
			stmt.executeUpdate();
		}
    }
    public void delete(String Email) throws SQLException{
		System.out.println("Deleting data...");
		disable();
		try(PreparedStatement stmt = con.prepareStatement(" DELETE FROM customer"
														+ " WHERE email=?")){
			stmt.setString(1, Email);
			int rowsDel = stmt.executeUpdate();
			System.out.println("Rows deleted: " + rowsDel);
			read();
		}
		enable();
    }
    
    public void read() throws SQLException{
    	tblModel.setRowCount(0);
        System.out.println("Reading customer data...");
        try(PreparedStatement stmt = con.prepareStatement("SELECT DISTINCT customer.first_name, customer.last_name, customer.email, address.address, customer.active"
        		+ " FROM customer"
        		+ " INNER JOIN address ON customer.address_id = address.address_id")){
        	ResultSet resultset = stmt.executeQuery();
			boolean empty = true;
			while(resultset.next()) {
				String fName = resultset.getString(1);
				String lName = resultset.getString(2);
				String email = resultset.getString(3);
				String address = resultset.getString(4);
				int activeNum = resultset.getInt(5);
				String active = "false";
				if(activeNum == 1)
					active = "true";
				
				String data[] = {fName, lName, email, address, active};
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
