import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import bovespa.jdbc.dao.AcaoDAO;
import bovespa.jdbc.dao.LoginDAO;
import bovespa.jdbc.dao.ProcessoDAO;
import bovespa.jdbc.dao.ReportDAO;
import bovespa.jdbc.modelo.Acao;
import bovespa.jdbc.modelo.Processo;
import bovespa.jdbc.modelo.Report;
import bovespa.jdbc.modelo.Login;

public class Main {
	//version 1
	/*
	 * public static String SMTP_SERVER = new String("smtp.felixpalma.com.br");
	 * public static String SMTP_USER = new String("sistema@felixpalma.com.br");
	 * public static String SMTP_FROM = new String("sistema@felixpalma.com.br");
	 * public static String SMTP_PASSWORD = new String("tksh.wk1972"); public
	 * static Integer SMTP_PORT = new Integer(587); public static Integer
	 * SMTP_TIMEOUT = new Integer(10000);
	 */

	public static String SMTP_SERVER = new String("smtp.mail.yahoo.com");
	public static String SMTP_USER = new String("takeshiwaku");
	public static String SMTP_FROM = new String("takeshiwaku@yahoo.com");
	public static String SMTP_PASSWORD = new String("Brenda2005");
	public static Integer SMTP_PORT = new Integer(587);
	public static Integer SMTP_TIMEOUT = new Integer(10000);
	public static Integer i_debug =0; // 1 ligado 0 desligado debug

	public static void main(String[] args) throws Exception {
		Date dt_sysdate;
		
		Integer i_dia_semana = 0;
		Integer i_hora = 0;
		SimpleDateFormat DiaSemana = new SimpleDateFormat("u");
		SimpleDateFormat Hora = new SimpleDateFormat("k");
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		dt_sysdate = new Date();
		System.out.println("Inicio ciclo execucao:");
		System.out.println("DiaSemana=" + DiaSemana.format(dt_sysdate));
		System.out.println("Hora=" + Hora.format(dt_sysdate));
		i_dia_semana = Integer.parseInt(DiaSemana.format(dt_sysdate));
		i_hora = Integer.parseInt(Hora.format(dt_sysdate));
		
		//i_hora = 18;
		
		if (i_debug == 0) {
			if (i_dia_semana >= 1 && i_dia_semana <= 5) {
				if (i_hora >= 8 && i_hora <= 20) {
					CargaDados();
					ListaAcaoDecrescente();
				}
				if (i_hora >= 9 && i_hora <= 18) {
					RelatorioPosicaoCarteira("TAKESHI", i_hora);
					RelatorioPosicaoCarteira("LEIDE", i_hora);
				}
			}
			
			// processo por porcesso enviando email dos diferentes.
			EnvioTodosProcessoVerificacao(i_hora);
			// ultimo relatorio
			EnvioRelatorioProcesso(i_hora);
		} else if (i_debug == 1 ) {
			// ListaAcaoDecrescente();

			ProcessoDAO dao = new ProcessoDAO();
			List<Processo> processos = dao.getLista();
			for (Processo p : processos) {
				GetProcesso(p);

				System.out.println("[" + p.getCd_processo() + "][" + p.getDs_processo() + "][" + p.getCd_juiz() + "][" + p.getDs_envio_notificacao() + "]");
				ProcessoDAO daoUpdate = new ProcessoDAO();
				daoUpdate.update(p);
			}
		} else  if (i_debug == 2 ) {
			ReportDAO daoCompra = new ReportDAO();
			List<Report> compras = daoCompra.getCompraNoMes("TAKESHI");
			for (Report c:compras){
				System.out.println("[" + c.getSigla() + "][" + 
									c.getQt_comprada()  + "][" + 
									c.getVl_total_com_taxa()  + "][" + 
									formatador.format(c.getDt_compra().getTime()) + "][" +
									c.getVl_compra()  + "]" ); 
								
			}
			RelatorioPosicaoCarteira("TAKESHI",18);
		} else  if (i_debug == 3 ) {
			ListaAcaoDecrescente();
		}
		else  if (i_debug == 4) {
			EnvioRelatorioProcesso(18);
		}
		else  if (i_debug == 5) {
			Processo po;
			po = new Processo(); 
			po.setCd_processo("B90004TXS0000");
			po.setCd_email("takeshiwaku@yahoo.com");
			GetProcesso(po);
		}
		else  if (i_debug == 6) {
			//GetProcessoPorOAB(); 
			EnvioTodosProcessoVerificacao(18);
		}
		else  if (i_debug == 7) {
			//GetProcessoPorOAB(); 
			RelatorioPosicaoCarteira("TAKESHI",18);
		}		
		
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see java.lang.Object#Object()
	 */
	public Main() {
		super();
	}

	public static void CargaDados() throws Exception {
		StringBuffer texto_email = new StringBuffer();
		String s_horario = new String();

		Date dt_fim, dt_ini;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		dt_ini = new Date();
		Document doc = Jsoup.connect("http://pregao-online.bmfbovespa.com.br/Cotacoes.aspx?idioma=pt-BR")
				.timeout(10 * 1000).get();
		System.out.println("Versao:1.0.0.1");
		System.out.println("Data:06/05/2016 11:52");
		texto_email.append("<html>");
		texto_email.append("<body>");
		texto_email.append("<table border=\"1\">");
		texto_email.append("<tr>");
		texto_email.append("<td>Sigla</td><td>valor</td>");
		texto_email.append("</tr>");

		for (Element table : doc.select("table")) {
			// System.out.println("tabela->" + table.text());

			for (Element row : table.select("tr")) {
				Elements tds = row.select("td");
				// System.out.println(tds.text());
				// texto_email.append(tds.text() + "\r");
				if (tds.size() > 3) {
					// System.out.println(tds.get(0).text());
					texto_email.append("<tr><td>" + tds.get(0).text() + "</td><td>" + tds.get(2).text() + "</td>"
							+ "<td>" + tds.get(3).text() + "</td>" + "<td>" + tds.get(1).text() + "</td>" + "<td>"
							+ tds.get(4).text() + "</td>" + "</tr>");
					s_horario = tds.get(4).text();
					Acao acao = new Acao();
					acao.setSigla(tds.get(0).text());
					acao.setSvalor(StringUtils.replace(tds.get(2).text(), ",", "."));
					acao.setVariacao(StringUtils.replace(tds.get(3).text(), ",", "."));
					acao.setDescricao(tds.get(1).text());

					// tds.get(2).text()
					// System.out.println(tds.get(4).text());
					// 02/09 - 10:09
					String steste = new String(tds.get(4).text());
					acao.setSdata("2016-" + StringUtils.mid(steste, 3, 2) + "-" + StringUtils.mid(steste, 0, 2) + " "
							+ StringUtils.mid(steste, 8, 5) + ":00");
					AcaoDAO dao = new AcaoDAO();
					// dao.altera(acao);
					dao.adiciona(acao);
				}
				// System.out.println("tamanho=" + tds.size());
				// for (int i = 0; i < tds.size(); i++) {
				// System.out
				// .println("[" + i + "][" + tds.get(i).text() + "]");
				// }
				// if (tds.size() > 0)
				// texto_email.append(tds.get(0).text() + "\r");
				// if (tds.size() > 0) {
				// texto_email.append(tds.get(0).text() + " " +
				// tds.get(2).text() + " " + tds.get(3).text() + "\r"
				// );
				// }
			}
		}
		texto_email.append("</table></body></html>");

		HtmlEmail email = new HtmlEmail();
		// configura a mensagem para o formato HTML

		// configure uma mensagem alternativa caso o servidor nao suporte HTML
		email.setTextMsg("Seu servidor de e-mail nso suporta mensagem HTML");
		email.setHostName(SMTP_SERVER); // o servidor SMTP para envio

		email.addTo("takeshiwaku@yahoo.com", "Takeshi"); // destinatario
		// email.addTo("takeshiw@br.ibm.com", "Takeshi"); // destinatario
		// email.addTo("luizgpalma@hotmail.com", "Luiz Gustavo Palma");
		// luiz.palma@br.pwc.com
		// email.setFrom("takeshiwaku@gmail.com", "Takeshi Waku"); // remetente
		email.setFrom(SMTP_FROM, SMTP_FROM); // remetente
		dt_fim = new Date();
		System.out.println("Inicio:" + dateFormat.format(dt_ini) + " fim:" + dateFormat.format(dt_ini));
		System.out.println(texto_email.toString());
		email.setSubject("I:" + dateFormat.format(dt_ini) + " F:" + dateFormat.format(dt_ini) + " " + s_horario
				+ " versao 06/09/2016 13:02");
		email.setHtmlMsg(texto_email.toString());
		email.setAuthentication(SMTP_USER, SMTP_PASSWORD);
		email.setSmtpPort(SMTP_PORT);
		email.setSocketConnectionTimeout(SMTP_TIMEOUT);
		email.setTLS(true);
		// email.send();
		System.out.println("encerrado com sucesso ! ");

	}

	public static void RelatorioPosicaoCarteira(String PC_CD_CLIENTE, int pn_hora) throws Exception {
		StringBuffer texto_email = new StringBuffer();
		String s_horario = new String();
		double d_total_carteira = 0;
		int i_counter =0;
		String lc_odd;
		String lc_link; 

		Date dt_fim, dt_ini;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		dt_ini = new Date();
		//Document doc = Jsoup.connect("http://pregao-online.bmfbovespa.com.br/Cotacoes.aspx?idioma=pt-BR")
		//		.timeout(10 * 1000).get();
		System.out.println("Versao:1.0.0.1");
		System.out.println("Data:06/05/2016 11:52");
		texto_email.append("<html>");
		
		texto_email.append("<style>");
		texto_email.append(".pure-table {");
		texto_email.append("    border-collapse: collapse;");
		texto_email.append("border-spacing: 0;");
		texto_email.append("empty-cells: show;");
		texto_email.append("border: 1px solid #cbcbcb");
		texto_email.append("}");
		texto_email.append(".pure-table caption {");
		texto_email.append("color: #000;");
		texto_email.append("font: italic 85%/1 arial, sans-serif;");
		texto_email.append("padding: 1em 0;");
		texto_email.append("text-align: center");
		texto_email.append("}");
		texto_email.append(".pure-table td,");
		texto_email.append(".pure-table th {");
		texto_email.append("border-left: 1px solid #cbcbcb;");
		texto_email.append("border-width: 0 0 0 1px;");
		texto_email.append("font-size: inherit;");
		texto_email.append("margin: 0;");
		texto_email.append("overflow: visible;");
		texto_email.append("padding: .5em 1em");
		texto_email.append("}");
		texto_email.append(".pure-table td:first-child,");
		texto_email.append(".pure-table th:first-child {");
		texto_email.append("border-left-width: 0");
		texto_email.append("}");
		texto_email.append(".pure-table thead {");
		texto_email.append("background-color: #e0e0e0;");
		texto_email.append("color: #000;");
		texto_email.append("text-align: left;");
		texto_email.append("vertical-align: bottom");
		texto_email.append("}");
		texto_email.append(".pure-table td {");
		texto_email.append("background-color: transparent");
		texto_email.append("}");
		texto_email.append(".pure-table-odd td {");
		texto_email.append("background-color: #f2f2f2");
		texto_email.append("}");
		texto_email.append(".pure-table-striped tr:nth-child(2n-1) td {");
		texto_email.append("background-color: #f2f2f2");
		texto_email.append("}");
		texto_email.append(".pure-table-bordered td {");
		texto_email.append("border-bottom: 1px solid #cbcbcb");
		texto_email.append("}");
		texto_email.append(".pure-table-bordered tbody>tr:last-child>td {");
		texto_email.append("border-bottom-width: 0");
		texto_email.append("}");
		texto_email.append(".pure-table-horizontal td,");
		texto_email.append(".pure-table-horizontal th {");
		texto_email.append("border-width: 0 0 1px;");
		texto_email.append("border-bottom: 1px solid #cbcbcb");
		texto_email.append("}");
		texto_email.append(".pure-table-horizontal tbody>tr:last-child>td {");
		texto_email.append("border-bottom-width: 0");
		texto_email.append("}");
		texto_email.append("</style>");

		
		
		texto_email.append("<body>");
		texto_email.append("<table class='pure-table' border='1'>");
		texto_email.append("<thead> ");
		
		texto_email.append("<tr>");
		texto_email.append("<th>Ação</th><th>G</th><th>Qtde</th><th>Com</th><th>Ven</th><th>D</th><th>C</th><th>V</th><th>Luc</th><th>%</th><th>Corr</th>");
		texto_email.append("</tr>");
		texto_email.append("</thead> ");
		texto_email.append("<tbody>");
		ReportDAO dao = new ReportDAO();
		List<Report> reports = dao.getLista(PC_CD_CLIENTE);
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat df = new DecimalFormat("#,##0");
		DecimalFormat ddf = new DecimalFormat("#,##0.##");
		DecimalFormat dfi = new DecimalFormat("##");
		for (Report Report : reports) {
			i_counter++;
			if (i_counter%2==0){
				lc_odd=" class='pure-table-odd'";
			} else {
				lc_odd="";
		    }
		    lc_link= "http://web.apligraf.com.br/fazgrafico.php?cod=barrasgif%3b" + Report.getSigla().toLowerCase() + "&indexador=&x=650&y=350";
			texto_email.append("<tr" + lc_odd + "><td>" + Report.getSigla() + "</td>"
					+ "<td align=right><a href=" + lc_link + ">G</a></td>"
		            + "<td align=right>" + dfi.format(Report.getQt_comprada()) + "</td>"
					+ "<td align=right>" + Report.getVl_compra()   + "</td>"
					+ "<td align=right>" + Report.getVl_valor()    + "</td>"
					+ "<td align=right>" + dfi.format(Report.getQt_dias())     + "</td>" 
					+ "<td align=right>" + df.format(Report.getVl_total_com_taxa()) + "</td>" 
					+ "<td align=right>" + df.format(Report.getVl_venda()) + "</td>" 
					+ "<td align=right>" + df.format(Report.getVl_venda() - Report.getVl_total_com_taxa()) + "</td>" 
					+ "<td align=right>" + ddf.format(((Report.getVl_venda() - Report.getVl_total_com_taxa()) * 100) / Report.getVl_total_com_taxa()) + "</td>" 
					+ "<td>" + StringUtils.substring(Report.getCd_corretora(), 0, 3) + "</td></tr>");
			// System.out.println(df.format(Report.getVl_total_com_taxa()));
			d_total_carteira = d_total_carteira + Report.getVl_venda();
		}
		// Adiciona o total
		texto_email.append("</tbody>");
		texto_email.append("<thead>");
		texto_email.append("<tr><td colspan = 6  align=right >Total</td><td align=right>"
				+ df.format(d_total_carteira) + "</td> <td colspan=4></td> </tr>");
		texto_email.append("</thead>");
		texto_email.append("</table>");
		///////////// relatorio de compras no mes
		texto_email.append("<BR>");
		texto_email.append("<table  class='pure-table' >");
		texto_email.append("<thead>");
		texto_email.append("<tr><th colspan=6 align=center >COMPRAS NO MÊS</th>");
		texto_email.append("</tr>");
		texto_email.append("<tr>");
		texto_email.append("<th>Ação</th><th>G</th><th>Data</th><th>Qtde</th><th>Valor</th><th>Compra</th>");
		texto_email.append("</tr>");
		texto_email.append("</thead>");
		texto_email.append("<tbody>");

		Double ln_venda_mes=new Double(0);
		Double ln_compra_mes= new Double(0);
		Double ln_lucro_mes= new Double(0);
		ReportDAO daoCompra = new ReportDAO();
		
		List<Report> compras = daoCompra.getCompraNoMes(PC_CD_CLIENTE);
		i_counter=0;
		for (Report c:compras){
			i_counter++;
			if (i_counter%2==0){
				lc_odd=" class='pure-table-odd'";
			} else {
				lc_odd="";
		    }
			lc_link="http://web.apligraf.com.br/fazgrafico.php?cod=barrasgif%3b" + c.getSigla().toLowerCase() + "&indexador=&x=650&y=350";
			texto_email.append("<tr"+ lc_odd+"><td align=center>" +  c.getSigla() + "</td><td align=center >" +
			                    "<a href=" + lc_link +">G</a></td><td align=center >" +     
					            formatador.format(c.getDt_compra().getTime()) + "</td><td align=right >" +
					            dfi.format(c.getQt_comprada())  + "</td><td align=right >" +
								c.getVl_compra()    + "</td><td align=right >" +
								 df.format(c.getVl_total_com_taxa())  + "</td></tr>"  
								);
			ln_compra_mes=ln_compra_mes + c.getVl_total_com_taxa() ;
		}
		texto_email.append("</tbody>");
		texto_email.append("<thead>");
		texto_email.append("<tr>");
		texto_email.append("<td colspan = 5  align=right >TOTAL</td><td align=right> " + df.format(ln_compra_mes) + "</td>");
		texto_email.append("</tr>");	
		texto_email.append("</thead>");
		texto_email.append("</table>");
		
		texto_email.append("<BR>");
		
		texto_email.append("<table  class='pure-table' >");
		texto_email.append("<thead>");
		texto_email.append("<tr><th colspan=10 align=center >VENDAS NO MÊS</Th></tr>");
		texto_email.append("<tr>");
		texto_email.append("<th>Ação</th><th>G</th><th>Data</th><th>Qtde</th><th>Valor</th><th>Venda</th><th>Luc</th><th>Dias</th><th>%</th><th>Corr</th>");
		texto_email.append("</tr>");
		texto_email.append("</thead>");
		texto_email.append("<tbody>");
		
		
		ReportDAO daoVenda = new ReportDAO();
		List<Report> vendas = daoVenda.getVendaNoMes(PC_CD_CLIENTE);
		i_counter=0;
		for (Report v:vendas){
			i_counter++;
			if (i_counter%2==0){
				lc_odd=" class='pure-table-odd'";
			} else {
				lc_odd="";
		    }
			lc_link= "http://web.apligraf.com.br/fazgrafico.php?cod=barrasgif%3b" + v.getSigla().toLowerCase() + "&indexador=&x=650&y=350";
			texto_email.append("<tr"+ lc_odd+"><td align=center>"+v.getSigla() + "</td><td align=center >" +
			                    "<a href=" + lc_link + ">G</a></td><td align=center >" +  
					            formatador.format(v.getDt_compra().getTime()) + "</td><td align=right >" +
					            dfi.format(v.getQt_comprada())  + "</td><td align=right >" +
								v.getVl_venda()    + "</td><td align=right >" +
								df.format(v.getVl_total_com_taxa())  + "</td><td align=right>" +
								df.format(v.getVl_lucro_liquido())   + "</td><td align=right>" +
								dfi.format(v.getQt_dias())                       + "</td><td align=right>" +
								ddf.format(v.getVl_percentual())   + "</td><td align=center>" +
								StringUtils.substring(v.getCd_corretora(),0,3)  + "</td>" +
								"</tr>"  
								);
			ln_venda_mes=ln_venda_mes + v.getVl_total_venda();
			ln_lucro_mes=ln_lucro_mes + v.getVl_lucro_liquido();
			//System.out.println("v.getVl_total_venda()=" +v.getVl_total_venda());  
		}
		texto_email.append("</tbody>");
		texto_email.append("<thead>");
		texto_email.append("<tr>");
		texto_email.append("<td colspan =5  align=right >TOTAL</td><td align=right> " + df.format(ln_venda_mes) + "</td><td align=right> " + df.format(ln_lucro_mes) + "</td><td colspan=3></td>");
		texto_email.append("</tr>");	
		texto_email.append("<tr>");
		texto_email.append("<td colspan =5  align=right >SALDO</td><td align=right> " + df.format(20000-ln_venda_mes) + "</td><td align=right></td><td colspan=3></td>");
		texto_email.append("</tr>");		
		texto_email.append("</thead>");
		texto_email.append("</table>");		
		
		
		
				
		texto_email.append("</body></html>");

		HtmlEmail email = new HtmlEmail();
		// configura a mensagem para o formato HTML

		// configure uma mensagem alternativa caso o servidor nao suporte
		// HTML
		email.setTextMsg("Seu servidor de e-mail nso suporta mensagem HTML");
		email.setHostName(SMTP_SERVER); // o servidor SMTP para
										// envio
										// do
		email.addTo("takeshiwaku@yahoo.com", "Takeshi"); // destinatario
		if (PC_CD_CLIENTE.equals("LEIDE") && pn_hora == 18) {
			email.addTo("marisato@hotmail.com", "Leide");
		}
		// email.addTo("takeshiw@br.ibm.com", "Takeshi"); // destinatario
		// email.addTo("luizgpalma@hotmail.com", "Luiz Gustavo Palma");
		// luiz.palma@br.pwc.com
		// email.setFrom("takeshiwaku@gmail.com", "Takeshi Waku"); //
		// remetente
	
		
		
		email.setFrom(SMTP_FROM, SMTP_FROM); // remetente
		dt_fim = new Date();
		System.out.println("Inicio:" + dateFormat.format(dt_ini) + " fim:" + dateFormat.format(dt_ini));
		System.out.println(texto_email.toString());
		email.setSubject("Posicao acao:" + PC_CD_CLIENTE + " I:" + dateFormat.format(dt_ini) + " F:"
				+ dateFormat.format(dt_ini) + " " + s_horario + " versao 09/09/2016 10:10");
		email.setHtmlMsg(texto_email.toString());
		email.setAuthentication(SMTP_USER, SMTP_PASSWORD);
		email.setSmtpPort(SMTP_PORT);
		email.setSocketConnectionTimeout(SMTP_TIMEOUT);
		email.setTLS(true);
		System.out.println("inicio envio email posicao carteira ! ");
		email.send();
		System.out.println("envio com sucesso!");
	
		if (i_debug != 0 ) { 
			File arquivo = new File("g:\\teste.html");
			try( FileWriter fw = new FileWriter(arquivo) ){
			    fw.write(texto_email.toString());
			    //fw.write("25");
			    fw.flush();
			    fw.close();
			}catch(IOException ex){
			  ex.printStackTrace();
			  System.out.println(ex.getMessage()); 
			}
		}
		

	}

	public static void ListaAcaoDecrescente() throws Exception {
		StringBuffer texto_email = new StringBuffer();
		String s_horario = new String();

		Date dt_fim, dt_ini;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		dt_ini = new Date();
		//System.out.println("Versao:1.0.0.1");
		//System.out.println("Data:06/05/2016 11:52");
		texto_email.append("<html>");
		texto_email.append("<body>");
		texto_email.append("<table border=\"1\">");
		texto_email.append("<tr>");
		texto_email.append("<td>Sigla</td><td>Valor</td><td>Variação</td><td>Link</td>");
		texto_email.append("</tr>");

		AcaoDAO dao = new AcaoDAO();
		List<Acao> acaos = dao.getLista();
		////////////
		ReportDAO Rdao = new ReportDAO();
		List<Report> reports = Rdao.getListaAcaoUtilizada();
		////////
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat df = new DecimalFormat("#,##0.00");
		for (Acao acao : acaos) {

			texto_email.append("<tr " + f_verifica(reports, acao.getSigla()) + "><td>" + acao.getSigla() + "</td><td>"
					+ acao.getValor() + "</td><td align=right>" + acao.getVariacao() + "</td><td> <a href=" + 
"http://web.apligraf.com.br/fazgrafico.php?cod=barrasgif%3b" + acao.getSigla().toLowerCase() +"&indexador=&x=650&y=350" + ">link</a>"+					
					
					"</tr>");
			// System.out.println(df.format(Report.getVl_total_com_taxa()));

		}
		// Adiciona o total
		texto_email.append("</table></body></html>");

		HtmlEmail email = new HtmlEmail();
		// configura a mensagem para o formato HTML

		// configure uma mensagem alternativa caso o servidor nao suporte
		// HTML
		email.setTextMsg("Seu servidor de e-mail nso suporta mensagem HTML");
		email.setHostName(SMTP_SERVER);
		email.addTo("takeshiwaku@yahoo.com", "Takeshi"); // destinatario
		email.setFrom(SMTP_FROM, SMTP_FROM); // remetente
		dt_fim = new Date();
		System.out.println("Inicio:" + dateFormat.format(dt_ini) + " fim:" + dateFormat.format(dt_ini));
		System.out.println(texto_email.toString());
		email.setSubject("Lista Acao ordem descrente " + " I:" + dateFormat.format(dt_ini) + " F:"
				+ dateFormat.format(dt_ini) + " " + s_horario + " versao 09/09/2016 10:10");
		email.setHtmlMsg(texto_email.toString());
		email.setAuthentication(SMTP_USER, SMTP_PASSWORD);
		email.setSmtpPort(SMTP_PORT);
		email.setSocketConnectionTimeout(SMTP_TIMEOUT);
		email.setTLS(true);
		System.out.println("inicio envio email decrescente ");
		email.send();
		System.out.println("envio com sucesso!");
		if (i_debug != 0 ) { 
			File arquivo = new File("g:\\teste.html");
			try( FileWriter fw = new FileWriter(arquivo) ){
			    fw.write(texto_email.toString());
			    //fw.write("25");
			    fw.flush();
			    fw.close();
			}catch(IOException ex){
			  ex.printStackTrace();
			  System.out.println(ex.getMessage()); 
			}
		}		

	}

	public static String f_verifica(List<Report> lt_acao, String s_acao) {
		String lc_retorno = new String("");
		for (Report Report : lt_acao) {
			if (Report.getSigla().equals(s_acao)) {
				lc_retorno = "bgcolor=\"#FFA5A5\"";
			}
		}
		return lc_retorno;
	}

	public static void GeraEmailProcesso(int pn_hora) throws Exception {

		ProcessoDAO dao = new ProcessoDAO();
		List<Processo> processos = dao.getLista();
		// Processo processo=new Processo();
		// processo.setCd_processo("3J0001M580000");
		// EnvioEmailProcesso(processo.getCd_processo(),
		// processo.getCd_email(),processo);
		// System.out.println("juiz=" + processo.getCd_juiz());
		for (Processo Processo : processos) {
			// EnvioEmailProcesso("3J0001M580000","takeshiwaku@yahoo.com");
			System.out.println("Processo=" + Processo.getCd_processo() + " " + Processo.getCd_email() + " "
					+ Processo.getNr_hora_envio());
			if (pn_hora == Processo.getNr_hora_envio()) {

				EnvioEmailProcesso(Processo.getCd_processo(), Processo.getCd_email());
			}
		}
	}

	public static void EnvioEmailProcesso(String PC_PROCESSO, String PC_EMAIL) throws Exception {
		StringBuffer lc_mov = new StringBuffer();
		StringBuffer texto_email = new StringBuffer();
		String lc_processo = new String();
		String lc_classe = new String();
		String lc_area = new String();
		String lc_local_fisico = new String();
		String lc_distribuicao = new String();
		String lc_juiz = new String();
		String lc_valor_acao = new String();

		int ln_liga = 0;
		int ln_qtde_mov = 0;
		Document doc = null;

		doc = Jsoup
				.connect("http://esaj.tjsp.jus.br/cpopg/show.do?processo.codigo=" + PC_PROCESSO
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

					// lc_monitoria

					// System.out.println(tds.get(0).text());
				}
				if ((ln_liga == 1) && (ln_qtde_mov < 10)) {
					lc_mov.append(tds.text() + "<br><br>");
					ln_qtde_mov++;
					System.out.println(tds.text());
				}
			}
		}
		texto_email.append("<html>");
		texto_email.append("<BODY>");
		// texto_email.append("<b><u>Me passa uma lista de processo que vc quer
		// monitorar, e assim eu deixo programado p/ gerar um email todos os
		// dias</b></u><br><br>" + lc_processo + "<br><br>");
		texto_email.append("<b><u>Processo:</b></u>" + lc_processo + "<br><br>");
		texto_email.append("<b><u>Classe:</b></u>" + lc_classe + "<br><br>");
		texto_email.append("<b><u>Área:</b></u>" + lc_area + "<br><br>");
		texto_email.append("<b><u>Local Fisico:</b></u>" + lc_local_fisico + "<br><br>");
		texto_email.append("<b><u>Distribuicao:</b></u>" + lc_distribuicao + "<br><br>");
		texto_email.append("<b><u>Juiz:</b></u>" + lc_juiz + "<br><br>");
		texto_email.append("<b><u>Valor da ação:</b></u>" + lc_valor_acao + "<br><br>");
		texto_email.append(lc_mov);
		texto_email.append("</BODY>");
		texto_email.append("</html>");
		// System.out.println(" lc_juiz = " +lc_juiz);
		// set dos valores
		// pt_processo.setDs_processo(lc_processo);
		// pt_processo.setCd_classe(lc_classe);
		// pt_processo.setCd_area(lc_area);
		// pt_processo.setCd_local_fisico(lc_local_fisico);
		// pt_processo.setCd_juiz(lc_juiz);

		System.out.println(texto_email.toString());

		// SimpleEmail email = new SimpleEmail();
		HtmlEmail email = new HtmlEmail();
		// configura a mensagem para o formato HTML
		// Antigo yahoo.
		// configure uma mensagem alternativa caso o servidor não suporte HTML
		/*
		 * email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");
		 * email.setHostName("smtp.mail.yahoo.com"); // o servidor SMTP para
		 * envio email.addTo(PC_EMAIL, PC_EMAIL); // destinatário
		 * email.addTo("takeshiwaku@yahoo.com", "Takeshi");
		 * email.setFrom("takeshiwaku@gmail.com", "Takeshi Waku"); // remetente
		 * email.setSubject("Processo:" + lc_processo); // assunto do e-mail
		 * email.setHtmlMsg(texto_email.toString());
		 * email.setAuthentication("takeshiwaku", "Brenda2005");
		 * email.setSmtpPort(587); email.setSSL(true);
		 * System.out.println("inicio envio email processo !!"); email.send();
		 * System.out.println("envio com sucesso!");
		 */

		email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");
		email.setHostName(SMTP_SERVER); // o servidor SMTP para envio
		email.addTo(PC_EMAIL, PC_EMAIL); // destinatário
		email.addTo("takeshiwaku@yahoo.com", "Takeshi");
		email.setFrom(SMTP_FROM, SMTP_FROM); // remetente
		email.setSubject("Processo:" + lc_processo); // assunto do e-mail
		email.setHtmlMsg(texto_email.toString());
		email.setAuthentication(SMTP_USER, SMTP_PASSWORD);
		email.setSmtpPort(SMTP_PORT);
		email.setSocketConnectionTimeout(SMTP_TIMEOUT);
		email.setSSL(true);
		System.out.println("inicio envio email processo !!");
		email.send();
		System.out.println("envio com sucesso!");
	}

	public static void GetProcesso(Processo pt_processo) throws Exception {
		StringBuffer lc_mov = new StringBuffer();
		StringBuffer texto_email = new StringBuffer();
		String lc_processo = new String();
		String lc_classe = new String();
		String lc_area = new String();
		String lc_local_fisico = new String();
		String lc_distribuicao = new String();
		String lc_juiz = new String();
		String lc_valor_acao = new String();
		String lc_ultima_atz = new String();
		String lc_movimento_anterior = new String();

		int ln_liga = 0;
		int ln_liga_audiencia =0;
		int ln_qtde_mov = 0;
		int ln_passagem =0; 
		Document doc = null;
		lc_movimento_anterior = pt_processo.getDs_ultimo_movimento();
		doc = Jsoup
				.connect("http://esaj.tjsp.jus.br/cpopg/show.do?processo.codigo=" + pt_processo.getCd_processo()
						+ "&processo.foro=127&uuidCaptcha=sajcaptcha_b5baed84ee5b4043954109a452f23e09")
				.timeout(10 * 1000).get();
		System.out.println("1iinicio");
		for (Element table : doc.select("table")) {
			//if (ln_liga_audiencia==1)
			 //System.out.println("tabela->" + table.text());

			for (Element row : table.select("tr")) {
				
				Elements tds = row.select("td");
				
				// System.out.println(tds.text());

				// texto_email.append(tds.text()+"\r");
				// System.out.println("tamanho=" + tds.size());
				for (int i = 0; i < tds.size(); i++) {
					 System.out.println("[" + i +"][" + tds.get(i).text() + "]");
					if (tds.get(0).text().equals("Movimentações")) {
						ln_liga = 1;
					}
					if (tds.get(0).text().equals("Audiências")) {
						ln_liga_audiencia=1;
						ln_passagem++;
						System.out.println("--------------------------- achou audiencias !!!!!!!");
						System.out.println("ln_passagem= "+ln_passagem);
						System.out.println("[" + i +"][" + tds.get(i).text() + "]");
						
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

					// lc_monitoria

					// System.out.println(tds.get(0).text());
				}
				if (ln_liga_audiencia ==1) {
					System.out.println("audiencia [" + tds.text() + "]1");
					if (StringUtils.isEmpty(tds.text())) {
						System.out.println("vazio pegar o " + tds.text());
					}
				}
				if ((ln_liga == 1) && (ln_qtde_mov < 10)) {
					lc_mov.append(tds.text() + "<br><br>");
					ln_qtde_mov++;
					if (ln_qtde_mov == 4) {
						lc_ultima_atz = tds.text();
					}

				}
			}
		}
		System.out.println("----------------------------");
		
		texto_email.append("<html>");
		texto_email.append("<BODY>");
		// texto_email.append("<b><u>Me passa uma lista de processo que vc quer
		// monitorar, e assim eu deixo programado p/ gerar um email todos os
		// dias</b></u><br><br>" + lc_processo + "<br><br>");
		texto_email.append("<b><u>Processo:</b></u>" + lc_processo + "<br><br>");
		texto_email.append("<b><u>Classe:</b></u>" + lc_classe + "<br><br>");
		texto_email.append("<b><u>Área:</b></u>" + lc_area + "<br><br>");
		texto_email.append("<b><u>Local Fisico:</b></u>" + lc_local_fisico + "<br><br>");
		texto_email.append("<b><u>Distribuicao:</b></u>" + lc_distribuicao + "<br><br>");
		texto_email.append("<b><u>Juiz:</b></u>" + lc_juiz + "<br><br>");
		texto_email.append("<b><u>Valor da ação:</b></u>" + lc_valor_acao + "<br><br>");
		texto_email.append(lc_mov);
		texto_email.append("</BODY>");
		texto_email.append("</html>");
		System.out.println(" lc_juiz = " +lc_juiz);
		// set dos valores
		pt_processo.setDs_processo(lc_processo);
		pt_processo.setCd_classe(lc_classe);
		pt_processo.setCd_area(lc_area);
		pt_processo.setCd_local_fisico(lc_local_fisico);
		pt_processo.setCd_juiz(lc_juiz);
		pt_processo.setDs_ultimo_movimento(lc_ultima_atz);
		//System.out.println("[" + lc_ultima_atz + "]");
		System.out.println(texto_email.toString());

		// SimpleEmail email = new SimpleEmail();
		HtmlEmail email = new HtmlEmail();
		// configura a mensagem para o formato HTML
		// Antigo yahoo.
		// configure uma mensagem alternativa caso o servidor não suporte HTML
		/*
		 * email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");
		 * email.setHostName("smtp.mail.yahoo.com"); // o servidor SMTP para
		 * envio email.addTo(PC_EMAIL, PC_EMAIL); // destinatário
		 * email.addTo("takeshiwaku@yahoo.com", "Takeshi");
		 * email.setFrom("takeshiwaku@gmail.com", "Takeshi Waku"); // remetente
		 * email.setSubject("Processo:" + lc_processo); // assunto do e-mail
		 * email.setHtmlMsg(texto_email.toString());
		 * email.setAuthentication("takeshiwaku", "Brenda2005");
		 * email.setSmtpPort(587); email.setSSL(true);
		 * System.out.println("inicio envio email processo !!"); email.send();
		 * System.out.println("envio com sucesso!");
		 */
		System.out.println("------3333---------");
		email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");
		email.setHostName(SMTP_SERVER); // o servidor SMTP para envio
		email.addTo(pt_processo.getCd_email(), pt_processo.getCd_email()); // destinatário
		email.addTo("takeshiwaku@yahoo.com", "Takeshi");
		email.setFrom(SMTP_FROM, SMTP_FROM); // remetente
		email.setSubject("Processo:" + lc_processo); // assunto do e-mail
		email.setHtmlMsg(texto_email.toString());
		email.setAuthentication(SMTP_USER, SMTP_PASSWORD);
		email.setSmtpPort(SMTP_PORT);
		email.setSocketConnectionTimeout(SMTP_TIMEOUT);
		email.setSSL(true);
		// caso seja diferente a ultima atualizacao enviar e-mail
		
		System.out.println("lc_movimento_anterior=[" + lc_movimento_anterior +"]");
		System.out.println("lc_ultima_atz=        [" + lc_ultima_atz+"]");

		if (!lc_ultima_atz.equals(lc_movimento_anterior)) {

			java.util.Date dt = new java.util.Date();

			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//System.out.println("----- sdf.format(dt)=" +sdf.format(dt));
			pt_processo.setDs_dt_envio_notificacao(sdf.format(dt));

			System.out.println("inicio envio email processo !!");
			if (i_debug == 0)
			  email.send();
			System.out.println("envio com sucesso!");
		}
		
		if (i_debug != 0 ) {
			System.out.println("---------inicio gravacao arquivo------");
			File arquivo = new File("g:\\teste.html");
			try( FileWriter fw = new FileWriter(arquivo) ){
			    fw.write(texto_email.toString());
			    //fw.write("25");
			    fw.flush();
			    fw.close();
			}catch(IOException ex){
			  ex.printStackTrace();
			  System.out.println(ex.getMessage()); 
			}
		}		
		
	}
	public static void EnvioRelatorioProcesso(int pn_hora) throws Exception {
		//envio uma vez ao dia, dando o status dos processos
		/*ProcessoDAO dao = new ProcessoDAO();
		List<Processo> lista_emails = dao.getListaEmailDistinto();
		for (Processo lista : lista_emails) {
			// para cada email de usuario enviar o relatorio.
			EnvioRelatorioProcessoPorEmail(lista.getCd_email());
			System.out.println("[" + lista.getCd_processo() + "][" + lista.getCd_email());
		}*/
		if (pn_hora==18) { 
			LoginDAO daoLogin = new LoginDAO();
			List<Login> logins = daoLogin.getLista();
			for (Login login:logins){
				EnvioRelatorioProcessoPorEmail(login);
			}
			
		}
	}

	public static void EnvioTodosProcessoVerificacao(int pn_hora) throws Exception {
		//envio uma vez ao dia, dando o status dos processos
		/*ProcessoDAO dao = new ProcessoDAO();
		List<Processo> lista_emails = dao.getListaEmailDistinto();
		for (Processo lista : lista_emails) {
			// para cada email de usuario enviar o relatorio.
			EnvioRelatorioProcessoPorEmail(lista.getCd_email());
			System.out.println("[" + lista.getCd_processo() + "][" + lista.getCd_email());
		}*/
		ProcessoDAO dao = new ProcessoDAO();
		List<Processo> processos = dao.getLista();
		for (Processo p : processos) {
			System.out.println("p.getNr_hora_envio()="+p.getNr_hora_envio()); 
			System.out.println("anterior valor p.getDs_envio_notificacao()= [" + p.getDs_envio_notificacao() ); 
			if (p.getNr_hora_envio()==pn_hora){
				GetProcesso(p);
				System.out.println("--- pos getprocesso --------");
				System.out.println("[" + p.getCd_processo() + "][" + p.getDs_processo() + "][" + p.getCd_juiz() + "][" + p.getDs_envio_notificacao() + "]");
				ProcessoDAO daoUpdate = new ProcessoDAO();
				daoUpdate.update(p);
			}
		}

	}

	
	
	public static void EnvioRelatorioProcessoPorEmail(Login login) throws Exception {
		//envio uma vez ao dia, dando o status dos processos
		StringBuffer texto_email = new StringBuffer();
		String s_horario = new String();
		double d_total_carteira = 0;
		int i_counter =0;
		String lc_odd;

		Date dt_fim, dt_ini;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		dt_ini = new Date();
		//Document doc = Jsoup.connect("http://pregao-online.bmfbovespa.com.br/Cotacoes.aspx?idioma=pt-BR")
		//		.timeout(10 * 1000).get();
		System.out.println("Versao:1.0.0.1");
		System.out.println("Data:06/05/2016 11:52");
		texto_email.append("<html>");
		
		texto_email.append("<style>");
		texto_email.append(".pure-table {");
		texto_email.append("    border-collapse: collapse;");
		texto_email.append("border-spacing: 0;");
		texto_email.append("empty-cells: show;");
		texto_email.append("border: 1px solid #cbcbcb");
		texto_email.append("}");
		texto_email.append(".pure-table caption {");
		texto_email.append("color: #000;");
		texto_email.append("font: italic 85%/1 arial, sans-serif;");
		texto_email.append("padding: 1em 0;");
		texto_email.append("text-align: center");
		texto_email.append("}");
		texto_email.append(".pure-table td,");
		texto_email.append(".pure-table th {");
		texto_email.append("border-left: 1px solid #cbcbcb;");
		texto_email.append("border-width: 0 0 0 1px;");
		texto_email.append("font-size: inherit;");
		texto_email.append("margin: 0;");
		texto_email.append("overflow: visible;");
		texto_email.append("padding: .5em 1em");
		texto_email.append("}");
		texto_email.append(".pure-table td:first-child,");
		texto_email.append(".pure-table th:first-child {");
		texto_email.append("border-left-width: 0");
		texto_email.append("}");
		texto_email.append(".pure-table thead {");
		texto_email.append("background-color: #e0e0e0;");
		texto_email.append("color: #000;");
		texto_email.append("text-align: left;");
		texto_email.append("vertical-align: bottom");
		texto_email.append("}");
		texto_email.append(".pure-table td {");
		texto_email.append("background-color: transparent");
		texto_email.append("}");
		texto_email.append(".pure-table-odd td {");
		texto_email.append("background-color: #f2f2f2");
		texto_email.append("}");
		texto_email.append(".pure-table-striped tr:nth-child(2n-1) td {");
		texto_email.append("background-color: #f2f2f2");
		texto_email.append("}");
		texto_email.append(".pure-table-bordered td {");
		texto_email.append("border-bottom: 1px solid #cbcbcb");
		texto_email.append("}");
		texto_email.append(".pure-table-bordered tbody>tr:last-child>td {");
		texto_email.append("border-bottom-width: 0");
		texto_email.append("}");
		texto_email.append(".pure-table-horizontal td,");
		texto_email.append(".pure-table-horizontal th {");
		texto_email.append("border-width: 0 0 1px;");
		texto_email.append("border-bottom: 1px solid #cbcbcb");
		texto_email.append("}");
		texto_email.append(".pure-table-horizontal tbody>tr:last-child>td {");
		texto_email.append("border-bottom-width: 0");
		texto_email.append("}");
		texto_email.append("</style>");

		
		
		texto_email.append("<body>");
		texto_email.append("<table class='pure-table' border='1'>");
		texto_email.append("<thead> ");
		
		texto_email.append("<tr>");
		texto_email.append(
				"<td>Código</td><td>Processo</td><td>EnvioEmail</td><td>Verificação</td><td>Atualização TJSP</td><td>dias</td>");
		texto_email.append("</tr>");
		texto_email.append("</thead> ");
		texto_email.append("<tbody>");
		
		ProcessoDAO dao = new ProcessoDAO();
		List<Processo> lista_proc = dao.getListaProcesso(login.getId());
		for (Processo rel: lista_proc){
			i_counter++;
			if (i_counter%2==0){
				lc_odd=" class='pure-table-odd'";
			} else {
				lc_odd="";
		    }
			texto_email.append("<tr" + lc_odd + "><td>" + rel.getCd_processo() + "</td>" +
			        "<td><a href=http://esaj.tjsp.jus.br/cpopg/show.do?processo.codigo=" + rel.getCd_processo() 
			+ "&processo.foro=127&uuidCaptcha=sajcaptcha_b5baed84ee5b4043954109a452f23e09>" + rel.getDs_processo() +"</a></td>" + 
					//+ "</td><td align=right>" + Report.getVl_compra() + "</td><td align=right>" + Report.getVl_valor()
					//+ "</td><td align=right>" + Report.getQt_dias() + "</td><td align=right>"
					"<td>" +  rel.getDs_envio_notificacao() + "</td>" +  
					"<td>" +  rel.getDs_ultima_verificacao() + "</td>" +
					"<td>" +  rel.getDs_ultimo_movimento() + "</td>" +
					"<td align=center>" +  rel.getNr_dias_alz() + "</td>" +
					// "</td><td>" +
					// Report.getDt_compra()+ "][" +
					//+ df.format(Report.getVl_total_com_taxa()) + "</td><td align=right>"
					// + Report.getVl_total_com_taxa()+ "</td><td>"
					//+ df.format(Report.getVl_venda()) + "</td><td align=right>"
					//+ df.format(Report.getVl_venda() - Report.getVl_total_com_taxa()) + "</td><td align=right>"
					//+ df.format(((Report.getVl_venda() - Report.getVl_total_com_taxa()) * 100)
					//		/ Report.getVl_total_com_taxa())
					//+ "</td><td>" + Report.getCd_corretora() + 
					"</tr>");
		}
		// Adiciona o total
		texto_email.append("</tbody>");
		
		// SimpleEmail email = new SimpleEmail();
		HtmlEmail email = new HtmlEmail();
		email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");
		email.setHostName(SMTP_SERVER); // o servidor SMTP para envio
		email.addTo(login.getEmail(),login.getName()); // destinatário
		email.addTo("takeshiwaku@yahoo.com", "Takeshi");
		email.setFrom(SMTP_FROM, SMTP_FROM); // remetente
		email.setSubject("Relatório diário de atualizações do usuario:" + login.getName()); // assunto do e-mail
		email.setHtmlMsg(texto_email.toString());
		email.setAuthentication(SMTP_USER, SMTP_PASSWORD);
		email.setSmtpPort(SMTP_PORT);
		email.setSocketConnectionTimeout(SMTP_TIMEOUT);
		email.setSSL(true);
		email.send();
		
		if (i_debug != 0 ) { 
			File arquivo = new File("g:\\" + login.getEmail() +  " .html");
			try( FileWriter fw = new FileWriter(arquivo) ){
			    fw.write(texto_email.toString());
			    //fw.write("25");
			    fw.flush();
			    fw.close();
			}catch(IOException ex){
			  ex.printStackTrace();
			  System.out.println(ex.getMessage()); 
			}
		}		
	}
	
	
	public static void GetProcessoPorOAB() throws Exception {
		Document doc = null;
		doc = Jsoup
				.connect("http://esaj.tjsp.jus.br/cpopg/search.do?conversationId=&dadosConsulta.localPesquisa.cdLocal=-1&cbPesquisa=NUMOAB&dadosConsulta.tipoNuProcesso=UNIFICADO&dadosConsulta.valorConsulta=347754&uuidCaptcha=sajcaptcha_b5baed84ee5b4043954109a452f23e09")
				.timeout(10 * 1000).get();
		System.out.println("Inicio");
		Elements links = doc.select("a[href]");
		System.out.println("Links:"+  links.size());
        for (Element link : links) {
        	if (StringUtils.containsAny(link.attr("abs:href"), "show.do?processo.codigo="))
        	System.out.println("RESULTADO=[" +  link.attr("abs:href") + "]["+  link.text());
        }
	}	
	
}