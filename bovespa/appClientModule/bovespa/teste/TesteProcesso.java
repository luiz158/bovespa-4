package bovespa.teste;
import java.io.IOException;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class TesteProcesso {

	public static void main(String[] args) throws IOException {
		//Integer li_liga;
				StringBuffer lc_mov = new StringBuffer();
				StringBuffer texto_email = new StringBuffer();
				String lc_processo = new String();
				String lc_classe = new String();
				String lc_area = new String();
				String lc_local_fisico = new String();
				String lc_distribuicao = new String();
				String lc_juiz = new String();
				String lc_valor_acao = new String();
				
				int ln_liga  = 0; 
				int ln_qtde_mov =0;
				Document doc = Jsoup
						.connect(
								"http://esaj.tjsp.jus.br/cpopg/show.do?processo.codigo=3J0001R9L0000&processo.foro=127&uuidCaptcha=sajcaptcha_b5baed84ee5b4043954109a452f23e09")
						.timeout(10 * 1000).get();
				for (Element table : doc.select("table")) {
					// System.out.println("tabela->" + table.text());

					for (Element row : table.select("tr")) {
						Elements tds = row.select("td");
						
						
						//System.out.println(tds.text());
						
						
						
						
						// texto_email.append(tds.text()+"\r");
						// System.out.println("tamanho=" + tds.size());
						for (int i = 0; i < tds.size(); i++) {
							// System.out.println("[" + i +"][" + tds.get(i).text() + "]");
							if (tds.get(0).text().equals("Movimentações")) {
								ln_liga = 1;
							}
							 
							if (tds.get(0).text().equals("Processo:")) {
								lc_processo = tds.get(1).text();
							}
							if (tds.get(0).text().equals("Classe:")) {
								lc_classe = tds.get(1).text();
								//texto_email.append("Classe:" + tds.get(1).text() + "\r");
							}
							if (tds.get(0).text().equals("Local Físico:")) {
								lc_local_fisico = tds.get(1).text();
								//texto_email.append("Local Físico:" + tds.get(1).text()	+ "\r");
							}
							if (tds.get(0).text().equals("Distribuição:")) {
								lc_distribuicao = tds.get(1).text();
								//texto_email.append("Distribuição:" + tds.get(1).text() + "\r");
							}
							if (tds.get(0).text().equals("Juiz:")) {
								lc_juiz = tds.get(1).text();
								//texto_email.append("Juiz:" + tds.get(1).text() + "\r");
							}
							if (tds.get(0).text().equals("Valor da ação:")) {
								lc_valor_acao = tds.get(1).text();
								//texto_email.append("Valor da ação:" + tds.get(1).text() + "\r");
							}

							// lc_monitoria

							// System.out.println(tds.get(0).text());
						}
						if ((ln_liga==1) && (ln_qtde_mov < 100)){
							lc_mov.append(tds.text()+"<br><br>");
							ln_qtde_mov++;
							System.out.println(tds.text());
						}
					}
				}
				texto_email.append("<html>");
				texto_email.append("<BODY>");
				//texto_email.append("<b><u>Me passa uma lista de processo que vc quer monitorar, e assim eu deixo programado p/ gerar um email todos os dias</b></u><br><br>" + lc_processo + "<br><br>");
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
				
				
				// String lc_processo = new String();
				// String lc_classe = new String();
				// String lc_monitoria = new String();
				// String lc_area = new String();
				// String lc_local_fisico = new String();
				// String lc_distribuicao = new String();
				// String lc_juiz = new String();
				// String lc_valor_acao = new String();

				// System.out.println("processsoooo:==2222=>" + lc_processo);
				System.out.println(texto_email.toString());

				//SimpleEmail email = new SimpleEmail();
				HtmlEmail email = new HtmlEmail();
				// configura a mensagem para o formato HTML
				
/*
				// configure uma mensagem alternativa caso o servidor não suporte HTML
				email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");
				
				email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do
														// e-mail
				// email.addTo("takeshiwaku@gmail.com", "Takeshi"); //destinatário
				email.addTo("takeshiwaku@yahoo.com", "Takeshi"); // destinatário
				email.addTo("luizgpalma@hotmail.com", "Luiz Gustavo Palma");
				// luiz.palma@br.pwc.com
				email.setFrom("takeshiwaku@gmail.com", "Takeshi Waku"); // remetente
				email.setSubject("Processo:" + lc_processo ); // assunto do e-mail
				// email.setMsg("Teste de Email\n\rlinha2\n\rlinha33333"); //conteudo do
				// e-mail
				email.setHtmlMsg(texto_email.toString());
				//email.setMsg(texto_email.toString()); // conteudo do e-mail
				// email.setMsg(texto_email.toString())
				email.setAuthentication("takeshiwaku", "catapora");
				email.setSmtpPort(465);
				email.setSSL(true);
				email.setTLS(true);
				 //email.send();
	*/			
	}

}
