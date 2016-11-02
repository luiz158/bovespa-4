package bovespa.teste;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import bovespa.jdbc.dao.AcaoDAO;
import bovespa.jdbc.dao.ReportDAO;
import bovespa.jdbc.modelo.Acao;
import bovespa.jdbc.modelo.Report;

public class TesteLista {

	public static void main(String[] args) throws SQLException {
      lista2();     
		
		
		
		
	}
  public static void lista1(){
		ReportDAO dao = new ReportDAO();
		List<Report> reports = dao.getLista("LEIDE");	
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		
		for (Report Report : reports) {
			System.out.println("Sigla: ["+
					Report.getSigla()+ "][" +  
					Report.getQt_comprada()+ "][" +  
					Report.getQt_lote()+ "][" +  
					Report.getVl_compra()+ "][" +  
					Report.getQt_dias()+ "][" +  
					Report.getQt_comprada()+ "][" +  
					formatador.format(Report.getDt_compra().getTime())+ "][" +  
					//Report.getDt_compra()+ "][" +  
					Report.getVl_total_com_taxa()+ "][" +  
					Report.getVl_valor()+ "][" +  
					Report.getVl_venda()+ "][" +  
					Report.getVl_lucro_liquido()+ "][" +  
					Report.getVl_percentual()+ "]" 
			);
		}
  }
  public static void lista2(){
		AcaoDAO dao = new AcaoDAO();
		List<Acao> acaos = dao.getLista();	
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		
		for (Acao acao : acaos) {
			System.out.println("Sigla: ["+
		      acao.getSigla() + "][" + 
			  acao.getValor() + "][" +
			  acao.getVariacao() + "][" +
			  acao.getSdata() + "]" 		
					);
//					formatador.format(Report.getDt_compra().getTime())+ "][" +  
	
		}
}
}
