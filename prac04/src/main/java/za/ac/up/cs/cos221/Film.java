
package za.ac.up.cs.cos221;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

public class Film {
    private connection conClass;
    private Connection con;
    private DefaultTableModel tblModel;
    
    public Film(DefaultTableModel tblModel) throws SQLException{
        this.tblModel = tblModel;
        conClass = new connection();
        con = conClass.openConnection();
    }
    
    public void read() throws SQLException{
    	tblModel.setRowCount(0);
        System.out.println("Reading film data...");
        try(PreparedStatement stmt = con.prepareStatement("SELECT DISTINCT film.title, film.description, film.release_year, language.name,"
        		+ " film.rental_duration, film.rental_rate, film.length, film.replacement_cost, film.rating, film.special_features" 
        		+ " FROM film"
        		+ " INNER JOIN language ON film.language_id = language.language_id")){
        	ResultSet resultset = stmt.executeQuery();
			boolean empty = true;
			while(resultset.next()) {
				String title = resultset.getString(1);
				String description = resultset.getString(2);
				String release_year = resultset.getString(3);
				String language = resultset.getString(4);
				String rental_duration = resultset.getString(5);
				String rental_rate = resultset.getString(6);
				String length = resultset.getString(7);
				String replacement_cost = resultset.getString(8);
				String rating = resultset.getString(9);
				String special_feature = resultset.getString(10);
				
				String data[] = {title, description, release_year, language, rental_duration, rental_rate, length, replacement_cost, rating, special_feature};
				tblModel.addRow(data);
				empty = false;
			}
			if(empty)
				System.out.println("(no data)");
        }
    }
    
    public void add(String title, String description, String release, String language, int rental_duration, double rental_rate, int length, double replacement_cost, String rating, String special_features) throws SQLException{
		System.out.println("Creating new film...");

		int language_id = getLanguage(language);
		try(PreparedStatement stmt = con.prepareStatement("INSERT INTO film(title, description, release_year, language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features)"
														  + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
															)){
			stmt.setString(1, title);
			stmt.setString(2, description);
			stmt.setString(3, release);
			stmt.setInt(4, language_id);
			stmt.setInt(5, rental_duration);
			stmt.setDouble(6, rental_rate);
			stmt.setInt(7, length);
			stmt.setDouble(8, replacement_cost);
			stmt.setString(9, rating);
			stmt.setString(10, special_features);
			stmt.executeQuery();
		}
    	read();
    }
    
    public int getLanguage(String language) throws SQLException{
    	int language_id = -1;
        try(PreparedStatement stmt = con.prepareStatement("SELECT DISTINCT language_id"
        		+ " FROM language"
        		+ " WHERE name = ?")){
        	stmt.setString(1, language);
        	ResultSet resultset = stmt.executeQuery();
        	if(resultset.next())
        		language_id = resultset.getInt(1);
        }
        return language_id;
    }
    
    public void closeCon() throws SQLException{
    	con.close();
    }
    
}
