package bovespa.jdbc.dao;

import java.sql.Connection;
//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bovespa.jdbc.ConnectionFactory;
import bovespa.jdbc.modelo.Acao;
import bovespa.jdbc.modelo.Report;

@SuppressWarnings("serial")
public class AcaoDAO extends DAOException {

	private Connection connection;

	public AcaoDAO() {
		new ConnectionFactory();
		this.connection = ConnectionFactory.getConnection();
	}

	public void adiciona(Acao acao) throws SQLException {

		String sql = "insert into ACAO" + " (sigla, valor, data_carga, data , descricao, variacao )"
				+ " values (?,?, now(), ? , ? , ?  )";
		/*
		 * String sql = "insert into acao" + " (sigla, valor)" + " values ('" +
		 * acao.getSigla() + "'," + "10" + ")" ;
		 */

		try {

			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, acao.getSigla());
			stmt.setString(2, acao.getSvalor());
			stmt.setString(3, acao.getSdata());
			stmt.setString(4, acao.getDescricao());
			stmt.setString(5, acao.getVariacao());

			// stmt.setString(3, acao.getSigla());
			// stmt.setString(3, contato.getEmail());
			// stmt.setDate(4, new Date(contato.getDataNascimento()
			// .getTimeInMillis()));

			stmt.execute();
			stmt.close();
			System.out.println("insert da "+ acao.getSigla() + " ok !");

		} catch (SQLException e) {
			//this.connection.close();
			System.out.println("erro !!! " + e.getMessage());
			altera(acao);
			//throw new DAOException(e);
		}
		finally{
			this.connection.close();
        }

	}

	public void altera(Acao acao) {
		String sql = "update ACAO set valor=?, data_carga = now(), data=?, variacao=? where sigla=?";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);

			stmt.setString(1, acao.getSvalor());
			stmt.setString(2, acao.getSdata());
			stmt.setString(3, acao.getVariacao());
			stmt.setString(4, acao.getSigla());
			// stmt.setString(3,contato.getEndereco());
			// stmt.setDate(4, new
			// Date(contato.getDataNascimento().getTimeInMillis()));
			// stmt.setLong(5, contato.getId());

			stmt.execute();
			stmt.close();

			System.out.println("update da "+ acao.getSigla() + " valor= " + acao.getSvalor());

		} catch (Exception e) {
			//stmt.close();
			System.out.println("Erro");
			throw new DAOException();
		}
	}
	
	public List<Acao> getLista() {
		String sql = "select * from ACAO order by variacao desc ";
        //System.out.println(sql);
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			List<Acao> acaos = new ArrayList<Acao>();

			while (rs.next()) {
				Acao acao = new Acao();
				acao.setSigla(rs.getString("sigla"));
				
				acao.setDescricao(rs.getString("SIGLA"));
				acao.setValor(rs.getDouble("VALOR"));
				//acao.setData(rs.getDate("DATA"));
				acao.setDescricao(rs.getString("DESCRICAO"));
				//acao.setData_carga(rs.getdatetime("DATA_CARGA"));
				//acao.setQt_padrao(rs.getDouble("QT_PADRAO"));
				acao.setVariacao(rs.getString("VARIACAO"));
				acao.setDs_obs(rs.getString("DS_OBS"));
				
								/*Calendar data = Calendar.getInstance();
				data.setTime(rs.getDate("DT_COMPRA"));
				report.setDt_compra(data);
				report.setVl_total_com_taxa(rs.getDouble("VL_TOTAL_COM_TAXA"));
				report.setVl_valor(rs.getDouble("VALOR"));
				report.setVl_venda(rs.getDouble("VALOR_VENDA"));
				report.setVl_lucro_liquido(rs.getDouble("LUCRO_LIQUIDO"));
				report.setVl_percentual(rs.getDouble("PERCENTUAL"));
				report.setCd_cliente(rs.getString("CD_CLIENTE"));
				report.setCd_corretora(rs.getString("CD_CORRETORA"));
				*/
				
				
				
				// contato.setEndereco(rs.getString("endereco"));
				// contato.setEmail(rs.getString("email"));


				acaos.add(acao);
			}

			rs.close();
			stmt.close();
			connection.close();

			return acaos;
		} catch (SQLException e) {
			throw new DAOException(e);// RuntimeException(e);
		}

	}
	
	
	
	
	
	
	
	/*
	 * public List<Contato> getLista() { String sql =
	 * "select * from contatos where email like 'bla%'";
	 * 
	 * try { PreparedStatement stmt = connection.prepareStatement(sql);
	 * 
	 * ResultSet rs = stmt.executeQuery();
	 * 
	 * List<Contato> contatos = new ArrayList<Contato>();
	 * 
	 * while (rs.next()) { Contato contato = new Contato();
	 * contato.setNome(rs.getString("nome"));
	 * contato.setEndereco(rs.getString("endereco"));
	 * contato.setEmail(rs.getString("email"));
	 * 
	 * Calendar data = Calendar.getInstance();
	 * data.setTime(rs.getDate("dataNascimento"));
	 * contato.setDataNascimento(data);
	 * 
	 * contatos.add(contato); }
	 * 
	 * rs.close(); stmt.close(); connection.close();
	 * 
	 * return contatos; } catch (SQLException e) { throw new DAOException(e);//
	 * RuntimeException(e); }
	 * 
	 * }
	 * 
	 * public List<Contato> pesquisar(int id){ String sql =
	 * "Select * from contatos where id="+id; try { PreparedStatement stmt =
	 * connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery();
	 * List<Contato> contatos = new ArrayList<Contato>(); while(rs.next()){
	 * Contato contato = new Contato(); contato.setId(rs.getLong("id"));
	 * contato.setNome(rs.getString("nome"));
	 * contato.setEndereco(rs.getString("endereco"));
	 * contato.setEmail(rs.getString("email"));
	 * 
	 * Calendar data = Calendar.getInstance();
	 * data.setTime(rs.getDate("dataNascimento"));
	 * contato.setDataNascimento(data);
	 * 
	 * contatos.add(contato); }
	 * 
	 * rs.close(); stmt.close(); connection.close();
	 * 
	 * return contatos;
	 * 
	 * } catch (Exception e) { throw new DAOException(e); } }
	 * 
	 * public void altera(Contato contato){ String sql =
	 * "update contatos set nome=?, email=?, endereco=?, dataNascimento=? where id=?"
	 * ; try { PreparedStatement stmt = connection.prepareStatement(sql);
	 * 
	 * stmt.setString(1,contato.getNome());
	 * stmt.setString(2,contato.getEmail());
	 * stmt.setString(3,contato.getEndereco()); stmt.setDate(4, new
	 * Date(contato.getDataNascimento().getTimeInMillis())); stmt.setLong(5,
	 * contato.getId());
	 * 
	 * stmt.execute(); stmt.close();
	 * 
	 * System.out.println("Alterado!");
	 * 
	 * } catch (Exception e) { throw new DAOException(); } }
	 * 
	 * public void remove(Contato contato){ String sql =
	 * "delete from contatos where id=?"; try { PreparedStatement stmt =
	 * connection.prepareStatement(sql); stmt.setLong(1, contato.getId());
	 * stmt.execute(); stmt.close();
	 * 
	 * System.out.println("Excluido");
	 * 
	 * } catch (Exception e) { throw new DAOException(); } }
	 */

}