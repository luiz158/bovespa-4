package bovespa.teste;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.lang3.StringUtils;
import bovespa.jdbc.dao.AcaoDAO;
import bovespa.jdbc.modelo.Acao;

public class GeraDados {
	public static void main(String[] args) throws Exception {
		// Integer li_liga;
		StringBuffer lc_mov = new StringBuffer();
		StringBuffer texto_email = new StringBuffer();
		String lc_processo = new String();
		String lc_classe = new String();
		String lc_monitoria = new String();
		String lc_area = new String();
		String lc_local_fisico = new String();
		String lc_distribuicao = new String();
		String lc_juiz = new String();
		String lc_valor_acao = new String();

		Date dt_fim, dt_ini;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		int ln_liga = 0;
		int ln_qtde_mov = 0;
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
					texto_email.append("<tr><td>" + tds.get(0).text() + "</td><td>" + tds.get(2).text() + "</td>" + 
					"<td>"	+ tds.get(3).text() + "</td>" +  
					"<td>"	+ tds.get(4).text() + "</td>" +		
					
							"</tr>");

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
		email.setHostName("smtp.mail.yahoo.com"); // o servidor SMTP para envio
													// do
		email.addTo("takeshiwaku@yahoo.com", "Takeshi"); // destinatario
		// email.addTo("takeshiw@br.ibm.com", "Takeshi"); // destinatario
		// email.addTo("luizgpalma@hotmail.com", "Luiz Gustavo Palma");
		// luiz.palma@br.pwc.com
		// email.setFrom("takeshiwaku@gmail.com", "Takeshi Waku"); // remetente
		email.setFrom("takeshiw@br.ibm.com", "Takeshi Waku");
		dt_fim = new Date();
		System.out.println("Inicio:" + dateFormat.format(dt_ini) + " fim:" + dateFormat.format(dt_ini));
		System.out.println(texto_email.toString());
		email.setSubject("Inicio:" + dateFormat.format(dt_ini) + " fim:" + dateFormat.format(dt_ini) + " versao 3.0");
		email.setHtmlMsg(texto_email.toString());
		email.setAuthentication("takeshiwaku@yahoo.com", "Brenda2005");
		email.setSmtpPort(587);
		email.setTLS(true);
		// email.send();
		System.out.println("encerrado com sucesso ! ");
	}

	// private static void //extracted(HtmlEmail email) {
	// email.setSSL(true);
	// }
}