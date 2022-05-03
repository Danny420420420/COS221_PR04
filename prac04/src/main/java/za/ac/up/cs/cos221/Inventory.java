package za.ac.up.cs.cos221;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

public class Inventory {
    private connection conClass;
    private Connection con;
    private DefaultTableModel tblModel;
    
    public class store {
    	public int store_id;
    	public String address = "nothing";
    	public store(int store_id, String address) {
    		this.store_id = store_id;
    		this.address = address;
    	}
    }
    
    public class genre {
    	int category_id;
    	public String name;
    	public genre(int category_id, String name) {
    		this.category_id = category_id;
    		this.name = name;
    	}
    }
    
    public Inventory(DefaultTableModel tblModel) throws SQLException{
        this.tblModel = tblModel;
        conClass = new connection();
        con = conClass.openConnection();
    }
    
    public void read() throws SQLException{
    	tblModel.setRowCount(0);
    	store stores[] = getStores();
    	genre genres[] = getGenres();
    	for(int i = 0; i < stores.length; i++) {
    		for(int j = 0; j < genres.length; j++) {
    			readStore(stores[i].store_id, stores[i].address, genres[j].category_id, genres[j].name);
    		}
    	}
    }
    
    public void readStore(int store_id, String store_address, int genre_id, String genre_name) throws SQLException{
    	try(PreparedStatement stmt = con.prepareStatement("SELECT inventory.store_id, film_category.category_id"
    			+ " FROM inventory"
    			+ " INNER JOIN film_category ON inventory.film_id = film_category.film_id"
    			+ " WHERE inventory.store_id = ? AND film_category.category_id = ?")){
    		stmt.setInt(1, store_id);
    		stmt.setInt(2, genre_id);
    		ResultSet resultset = stmt.executeQuery();
    		int count = 0;
    		while(resultset.next()) {
    			count++;
    		}
    		
    		String data[] = {store_address, genre_name, Integer.toString(count)};
    		tblModel.addRow(data);
    	}
    }
    
    
    public store[] getStores() throws SQLException{
    	int size = countStore();
    	store stores[] = new store[size];
    	int index = 0;
    	try(PreparedStatement stmt = con.prepareStatement("SELECT store.store_id, address.address"
    	+ " FROM store"
    	+ " INNER JOIN address ON store.address_id=address.address_id")){
    		ResultSet resultset = stmt.executeQuery();
    		while(resultset.next()) {
    			stores[index] = new store(resultset.getInt(1), resultset.getString(2));
    			index++;
    		}	
    	}
    	return stores;
    	
    }
    
    public genre[] getGenres() throws SQLException{
    	int size = countGenre();
    	int index = 0;
    	genre genres[] = new genre[size];
    	try(PreparedStatement stmt = con.prepareStatement("SELECT category_id, name FROM category")){
    		ResultSet resultset = stmt.executeQuery();
    		while(resultset.next()) {
    			genres[index] = new genre(resultset.getInt(1), resultset.getString(2));
    			index++;
    		}	
    	}
    	return genres;
    }
    
    public int countGenre() throws SQLException{
    	int size = 0;
    	try(PreparedStatement stmt = con.prepareStatement("SELECT COUNT(category_id) FROM category")){
    		ResultSet resultset = stmt.executeQuery();
    		if(resultset.next())
    			size = resultset.getInt(1);
    	}
    	return size;
    }
    
    public int countStore() throws SQLException{
    	int size = 0;
    	try(PreparedStatement stmt = con.prepareStatement("SELECT COUNT(store_id) FROM store")){
    		ResultSet resultset = stmt.executeQuery();
    		if(resultset.next())
    			size = resultset.getInt(1);
    	}
    	return size;
    }
    
    public void closeCon() throws SQLException{
    	con.close();
    }
    
}
