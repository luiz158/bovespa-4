package bovespa.jdbc.dao;

import bovespa.jdbc.ConnectionFactory;
import bovespa.jdbc.modelo.Report;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import bovespa.jdbc.ConnectionFactory;

public class ReportDAO extends DAOException {
	private Connection connection;

	public ReportDAO() {
		new ConnectionFactory();
		this.connection = ConnectionFactory.getConnection();
	}

	public List<Report> getLista(String PC_CD_CLIENTE) {
		String sql = "select A.ID, " + "A.SIGLA,  " + "A.QT_COMPRADA, " + "A.QT_LOTE,  " + "A.VL_COMPRA, "
				+ "datediff(NOW(),A.DT_COMPRA) QT_DIAS, " + "A.DT_COMPRA,  " + "A.VL_TOTAL_COM_TAXA, " + "B.VALOR,  "
				+ "(B.VALOR * A.QT_COMPRADA) VALOR_VENDA, "
				+ "(B.VALOR * A.QT_COMPRADA) - A.VL_TOTAL_COM_TAXA LUCRO_LIQUIDO, "
				+ "TRUNCATE(A.QT_COMPRADA / ((B.VALOR * A.QT_COMPRADA) - A.VL_TOTAL_COM_TAXA),2) PERCENTUAL, "
				+ " A.CD_CORRETORA, "
				+ " A.CD_CLIENTE  "
				+ "FROM felixpalma.OPERACAO A, " + "     felixpalma.ACAO B  " + "WHERE A.SIGLA = B.SIGLA AND IC_FECHADO = 'N' AND CD_CLIENTE = '" + PC_CD_CLIENTE + "'" ;
//System.out.println(sql);
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			List<Report> reports = new ArrayList<Report>();

			while (rs.next()) {
				Report report = new Report();
				report.setSigla(rs.getString("sigla"));
				report.setQt_comprada(rs.getDouble("QT_COMPRADA"));
				report.setQt_lote(rs.getDouble("QT_LOTE"));
				report.setVl_compra(rs.getDouble("VL_COMPRA"));
				report.setQt_dias(rs.getDouble("QT_DIAS"));
				report.setQt_comprada(rs.getDouble("QT_COMPRADA"));

				Calendar data = Calendar.getInstance();
				data.setTime(rs.getDate("DT_COMPRA"));
				report.setDt_compra(data);
				report.setVl_total_com_taxa(rs.getDouble("VL_TOTAL_COM_TAXA"));
				report.setVl_valor(rs.getDouble("VALOR"));
				report.setVl_venda(rs.getDouble("VALOR_VENDA"));
				report.setVl_lucro_liquido(rs.getDouble("LUCRO_LIQUIDO"));
				report.setVl_percentual(rs.getDouble("PERCENTUAL"));
				report.setCd_cliente(rs.getString("CD_CLIENTE"));
				report.setCd_corretora(rs.getString("CD_CORRETORA"));
			
				
				
				
				// contato.setEndereco(rs.getString("endereco"));
				// contato.setEmail(rs.getString("email"));


				reports.add(report);
			}

			rs.close();
			stmt.close();
			connection.close();

			return reports;
		} catch (SQLException e) {
			throw new DAOException(e);// RuntimeException(e);
		}

	}
	

	public List<Report> getCompraNoMes(String PC_CD_CLIENTE) {
		String sql = "SELECT * FROM OPERACAO WHERE MONTH(DT_COMPRA)=MONTH(CURDATE()) AND YEAR(DT_COMPRA)=YEAR(CURDATE())  AND CD_CLIENTE = '" + PC_CD_CLIENTE + "'" ;
        //System.out.println(sql);
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			List<Report> reports = new ArrayList<Report>();

			while (rs.next()) {
				Report report = new Report();
				report.setSigla(rs.getString("SIGLA"));
				report.setQt_comprada(rs.getDouble("QT_COMPRADA"));
				report.setQt_lote(rs.getDouble("QT_LOTE"));
				report.setVl_compra(rs.getDouble("VL_COMPRA"));
				//report.setQt_dias(rs.getDouble("QT_DIAS"));
				report.setQt_comprada(rs.getDouble("QT_COMPRADA"));

				Calendar data = Calendar.getInstance();
				data.setTime(rs.getDate("DT_COMPRA"));
				report.setDt_compra(data);
				report.setVl_total_com_taxa(rs.getDouble("VL_TOTAL_COM_TAXA"));
				report.setCd_cliente(rs.getString("CD_CLIENTE"));
				report.setCd_corretora(rs.getString("CD_CORRETORA"));
				
				// contato.setEndereco(rs.getString("endereco"));
				// contato.setEmail(rs.getString("email"));

				reports.add(report);
			}

			rs.close();
			stmt.close();
			connection.close();

			return reports;
		} catch (SQLException e) {
			throw new DAOException(e);// RuntimeException(e);
		}

	}

	public List<Report> getVendaNoMes(String PC_CD_CLIENTE) {
		String sql = "SELECT 	SIGLA, " + 
		"QT_COMPRADA, " + 
		"VL_TOTAL_COM_TAXA, " + 
		"VL_TOTAL_VENDA,  " + 
		"DT_COMPRA, " + 
		"DT_VENDA,  " + 
		"VL_COMPRA, " + 
		"VL_ACAO_VENDA, " + 
		"datediff(DT_VENDA,DT_COMPRA) QT_DIAS , " + 
		"TRUNCATE(VL_TOTAL_VENDA - VL_TOTAL_COM_TAXA,2) VL_LUCRO , " + 
		"TRUNCATE((((VL_TOTAL_VENDA - VL_TOTAL_COM_TAXA ) * 100) / VL_TOTAL_COM_TAXA),2) PERCENTUAL, " + 
		"CD_CORRETORA  " + 
        "FROM OPERACAO  " + 
        "WHERE MONTH(DT_VENDA)=MONTH(CURDATE()) AND YEAR(DT_VENDA)=YEAR(CURDATE()) AND IC_FECHADO = 'S'  AND CD_CLIENTE = '" + PC_CD_CLIENTE + "'" ;
        //System.out.println(sql);
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			List<Report> reports = new ArrayList<Report>();

			while (rs.next()) {
				Report report = new Report();
				report.setSigla(rs.getString("SIGLA"));
				report.setQt_comprada(rs.getDouble("QT_COMPRADA"));
				report.setVl_total_com_taxa(rs.getDouble("VL_TOTAL_COM_TAXA"));
				report.setVl_total_venda(rs.getDouble("VL_TOTAL_VENDA"));

				Calendar data = Calendar.getInstance();
				data.setTime(rs.getDate("DT_COMPRA"));
				report.setDt_compra(data);

				data.setTime(rs.getDate("DT_VENDA"));
				report.setDt_compra(data);
				report.setVl_compra(rs.getDouble("VL_COMPRA"));
				report.setVl_venda(rs.getDouble("VL_ACAO_VENDA"));
				report.setQt_dias(rs.getDouble("QT_DIAS"));
				report.setVl_lucro_liquido(rs.getDouble("VL_LUCRO"));
				report.setVl_percentual(rs.getDouble("PERCENTUAL"));				
				//report.setQt_lote(rs.getDouble("QT_LOTE"));
				report.setCd_corretora(rs.getString("CD_CORRETORA"));
				reports.add(report);
			}

			rs.close();
			stmt.close();
			connection.close();

			return reports;
		} catch (SQLException e) {
			throw new DAOException(e);// RuntimeException(e);
		}

	}

	
	public List<Report> getListaAcaoUtilizada() {
		String sql = "select distinct A.SIGLA SG_SIGLA FROM felixpalma.OPERACAO A WHERE  A.IC_FECHADO = 'N'";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			List<Report> reports = new ArrayList<Report>();

			while (rs.next()) {
				Report report = new Report();
				report.setSigla(rs.getString("SG_SIGLA"));

				reports.add(report);
			}

			rs.close();
			stmt.close();
			connection.close();

			return reports;
		} catch (SQLException e) {
			throw new DAOException(e);// RuntimeException(e);
		}

	}
}
