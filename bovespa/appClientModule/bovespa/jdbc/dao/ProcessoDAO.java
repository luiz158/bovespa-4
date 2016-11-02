package bovespa.jdbc.dao;

import bovespa.jdbc.ConnectionFactory;
import bovespa.jdbc.modelo.Acao;
import bovespa.jdbc.modelo.Processo;

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

public class ProcessoDAO extends DAOException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection connection;

	public ProcessoDAO() {
		new ConnectionFactory();
		this.connection = ConnectionFactory.getConnection();
	}

	public List<Processo> getLista() {
		String sql = "select  A.ID, " +
					  "A.CD_PROCESSO, " +
					  "A.DS_PROCESSO, " +
					  "A.CD_CLASSE, " +
					  "A.CD_AREA, " +
					  "A.CD_LOCAL_FISICO, " +
					  "A.CD_DISTRIBUICAO, " +
					  "A.CD_JUIZ, " +
					  "A.DS_VALOR_ACAO, " +
					  "A.NR_HORA_ENVIO, " +
					  "A.DS_ULTIMO_MOVIMENTO, " +
					  "A.DT_ULTIMA_VERIFICACAO, " +
					  "A.DT_ENVIO_NOTIFICACAO, " +
					  "A.IC_ATIVO, " +
					  "A.login_id, " +
					  "B.email CD_EMAIL " +
					"FROM PROCESSO A, login B " +
					"WHERE A.login_id = B.id";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			List<Processo> processos = new ArrayList<Processo>();

			while (rs.next()) {
				Processo processo = new Processo();
				processo.setId(rs.getDouble("ID"));
				processo.setCd_processo(rs.getString("CD_PROCESSO"));
				processo.setDs_processo(rs.getString("DS_PROCESSO"));
				processo.setCd_classe(rs.getString("CD_CLASSE"));
				processo.setCd_area(rs.getString("CD_AREA"));
				processo.setCd_local_fisico(rs.getString("CD_LOCAL_FISICO"));
				processo.setCd_distribuicao(rs.getString("CD_DISTRIBUICAO"));
				processo.setCd_juiz(rs.getString("CD_JUIZ"));
				processo.setDs_valor_acao(rs.getString("DS_VALOR_ACAO"));
				processo.setCd_email(rs.getString("CD_EMAIL"));
				processo.setNr_hora_envio(rs.getInt("NR_HORA_ENVIO"));
				processo.setDs_ultimo_movimento(rs.getString("DS_ULTIMO_MOVIMENTO"));
				// Calendar data = Calendar.getInstance();
				// data.setTime(rs.getDate("DT_COMPRA"));

				processos.add(processo);
			}

			rs.close();
			stmt.close();
			connection.close();

			return processos;
		} catch (SQLException e) {
			throw new DAOException(e);// RuntimeException(e);
		}

	}

	public void update(Processo processo) {
		String sql=null;
		if (processo.getDs_dt_envio_notificacao()==null) { 
		   sql = "update PROCESSO set DT_ULTIMA_VERIFICACAO= now(),  DS_PROCESSO = ? , DS_ULTIMO_MOVIMENTO = ? where ID= ?";
		}else {
		   sql = "update PROCESSO set DT_ULTIMA_VERIFICACAO= now(),  DS_PROCESSO = ? , DT_ENVIO_NOTIFICACAO = '" + processo.getDs_dt_envio_notificacao() + "' , DS_ULTIMO_MOVIMENTO = ? where ID= ?";			
		}
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			//System.out.println("===> processo.getDs_dt_envio_notificacao()="+processo.getDs_dt_envio_notificacao());
			stmt.setString(1, processo.getDs_processo());
			stmt.setString(2, processo.getDs_ultimo_movimento());
			stmt.setDouble(3, processo.getId());
			// stmt.setString(3, acao.getVariacao());
			// stmt.setString(4, acao.getSigla());
			// stmt.setString(3,contato.getEndereco());
			// stmt.setDate(4, new
			// Date(contato.getDataNascimento().getTimeInMillis()));
			// stmt.setLong(5, contato.getId());

			stmt.execute();
			stmt.close();

			System.out.println("update da " + processo.getId() + " processo= " + processo.getCd_processo()+ "-=>" + processo.getDs_dt_envio_notificacao());

		} catch (Exception e) {
			// stmt.close();
			System.out.println("Erro" + e.getMessage());
			throw new DAOException();
		}
	}

	public Processo ObtemProcessoSite(String Pc_processo) throws IOException {
		Processo proc = new Processo();
		StringBuffer lc_mov = new StringBuffer();
		StringBuffer texto_email = new StringBuffer();
		String lc_processo = new String();
		String lc_classe = new String();
		String lc_area = new String();
		String lc_local_fisico = new String();
		String lc_distribuicao = new String();
		String lc_juiz = new String();
		String lc_valor_acao = new String();
		Document doc = null;
		int ln_liga = 0;
		int ln_qtde_mov = 0;

		doc = Jsoup
				.connect("http://esaj.tjsp.jus.br/cpopg/show.do?processo.codigo=" + Pc_processo
						+ "&processo.foro=127&uuidCaptcha=sajcaptcha_b5baed84ee5b4043954109a452f23e09")
				.timeout(10 * 1000).get();

		for (Element table : doc.select("table")) {
			// System.out.println("tabela->" + table.text());
			for (Element row : table.select("tr")) {
				Elements tds = row.select("td");
				// System.out.println(tds.text());
				// texto_email.append(tds.text()+"\r");
				// System.out.println("tamanho=" + tds.size());
				for (int i = 0; i < tds.size(); i++) {
					// System.out.println("[" + i +"][" + tds.get(i).text() +
					// "]");
					if (tds.get(0).text().equals("Movimentações")) {
						ln_liga = 1;
					}

					if (tds.get(0).text().equals("Processo:")) {
						lc_processo = tds.get(1).text();
					}
					if (tds.get(0).text().equals("Classe:")) {
						lc_classe = tds.get(1).text();
						// texto_email.append("Classe:" + tds.get(1).text() +
						// "\r");
					}
					if (tds.get(0).text().equals("Local Físico:")) {
						lc_local_fisico = tds.get(1).text();
						// texto_email.append("Local Físico:" +
						// tds.get(1).text() + "\r");
					}
					if (tds.get(0).text().equals("Distribuição:")) {
						lc_distribuicao = tds.get(1).text();
						// texto_email.append("Distribuição:" +
						// tds.get(1).text() + "\r");
					}
					if (tds.get(0).text().equals("Juiz:")) {
						lc_juiz = tds.get(1).text();
						// texto_email.append("Juiz:" + tds.get(1).text() +
						// "\r");
					}
					if (tds.get(0).text().equals("Valor da ação:")) {
						lc_valor_acao = tds.get(1).text();
						// texto_email.append("Valor da ação:" +
						// tds.get(1).text() + "\r");
					}
					// System.out.println(tds.get(0).text());
				}
				if ((ln_liga == 1) && (ln_qtde_mov < 100)) {
					lc_mov.append(tds.text() + "<br><br>");
					ln_qtde_mov++;
					// System.out.println(tds.text());
				}
			}
		}
		proc.setDs_processo(lc_processo);

		return proc;
	}
	
	public List<Processo> getListaEmailDistinto() {
		String sql = "select distinct CD_EMAIL FROM PROCESSO where IC_ATIVO = 'S'";
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			List<Processo> processos = new ArrayList<Processo>();

			while (rs.next()) {
				Processo processo = new Processo();
				processo.setCd_email(rs.getString("CD_EMAIL"));
				processos.add(processo);
			}

			rs.close();
			stmt.close();
			connection.close();

			return processos;
		} catch (SQLException e) {
			throw new DAOException(e);// RuntimeException(e);
		}

	}
	
	public List<Processo> getListaProcesso(Double pn_login_id) {
		String sql = "		SELECT 	ID, CD_PROCESSO, " + 
		"DS_PROCESSO, " +
		"DATEDIFF(NOW(), STR_TO_DATE(LEFT(DS_ULTIMO_MOVIMENTO,10),'%d/%m/%Y')) DIAS, " +
		"LEFT(DS_ULTIMO_MOVIMENTO,10) DS_ULTIMO_MOVIMENTO, " +
		"DATE_FORMAT(DT_ULTIMA_VERIFICACAO, '%d/%m/%Y %H:%i:%s') DS_ULTIMA_VERIFICACAO , " + 
		"DATE_FORMAT(DT_ENVIO_NOTIFICACAO, '%d/%m/%Y %H:%i:%s') DS_ENVIO_NOTIFICACAO  " +
		"FROM PROCESSO WHERE login_id = " + pn_login_id + " and IC_ATIVO = 'S'  ORDER BY STR_TO_DATE(LEFT(DS_ULTIMO_MOVIMENTO,10),'%d/%m/%Y') DESC ";
		System.out.println(sql);
		try {
			PreparedStatement stmt = connection.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			List<Processo> processos = new ArrayList<Processo>();

			while (rs.next()) {
				Processo processo = new Processo();
				processo.setId(rs.getDouble("ID"));
				processo.setCd_processo(rs.getString("CD_PROCESSO"));
				processo.setDs_processo(rs.getString("DS_PROCESSO"));
				//processo.setCd_classe(rs.getString("CD_CLASSE"));
				//processo.setCd_area(rs.getString("CD_AREA"));
				//processo.setCd_local_fisico(rs.getString("CD_LOCAL_FISICO"));
				//processo.setCd_distribuicao(rs.getString("CD_DISTRIBUICAO"));
				//processo.setCd_juiz(rs.getString("CD_JUIZ"));
				//processo.setDs_valor_acao(rs.getString("DS_VALOR_ACAO"));
				//processo.setCd_email(rs.getString("CD_EMAIL"));
				//processo.setNr_hora_envio(rs.getInt("NR_HORA_ENVIO"));
				processo.setDs_ultimo_movimento(rs.getString("DS_ULTIMO_MOVIMENTO"));
				processo.setDs_envio_notificacao(rs.getString("DS_ENVIO_NOTIFICACAO"));
				processo.setDs_ultima_verificacao(rs.getString("DS_ULTIMA_VERIFICACAO"));
				processo.setNr_dias_alz(rs.getInt("DIAS"));
				processos.add(processo);
			}

			rs.close();
			stmt.close();
			connection.close();

			return processos;
		} catch (SQLException e) {
			throw new DAOException(e);// RuntimeException(e);
		}

	}


}
