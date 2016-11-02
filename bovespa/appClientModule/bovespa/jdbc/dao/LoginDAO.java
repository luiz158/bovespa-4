package bovespa.jdbc.dao;

import bovespa.jdbc.ConnectionFactory;
import bovespa.jdbc.modelo.Login;

import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Calendar;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import bovespa.jdbc.ConnectionFactory;

public class LoginDAO extends DAOException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection connection;

	public LoginDAO() {
		new ConnectionFactory();
		this.connection = ConnectionFactory.getConnection();
	}

	public List<Login> getLista() {
		String sql = "select * FROM login ";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			List<Login> logins = new ArrayList<Login>();

			while (rs.next()) {
				Login login = new Login();
				login.setId(rs.getDouble("ID"));
				login.setName(rs.getString("NAME"));
				login.setEmail(rs.getString("EMAIL"));
				login.setUsername(rs.getString("USERNAME"));
				
				logins.add(login);
			}

			rs.close();
			stmt.close();
			connection.close();

			return logins;
		} catch (SQLException e) {
			throw new DAOException(e);// RuntimeException(e);
		}

	}
    
}
