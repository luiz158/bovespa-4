package bovespa.teste;

import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

import bovespa.jdbc.dao.AcaoDAO;
import bovespa.jdbc.modelo.Acao;

public class TesteInsere {

	public static void main(String[] args) throws SQLException {

		Acao acao = new Acao();
		acao.setSigla("PETR4");
		acao.setSvalor("12.22");
		String steste = new String("02/09 - 10:09");
		// 2016-09-02 09:05:58
		acao.setSdata("2016-" + StringUtils.mid(steste,3,2) + "-"  + StringUtils.mid(steste,0,2) + " " + StringUtils.mid(steste,8,5)+":01" );
		System.out.println("valor:" + acao.getSdata());
		AcaoDAO dao = new AcaoDAO();
		//dao.altera(acao);
		
		
		dao.adiciona(acao);
		System.out.println("Alterado!");
	}

}
