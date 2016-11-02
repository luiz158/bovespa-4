package bovespa.jdbc.modelo;

import java.util.Calendar;

public class Acao {

	private String sigla;
	private String descricao;
	private String svalor;
	private double valor;
	private String variacao;
	private String sdata;
	private int qt_lote_padrao;
	private String ds_obs;
	
	public String getDs_obs() {
		return ds_obs;
	}
	public void setDs_obs(String ds_obs) {
		this.ds_obs = ds_obs;
	}
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getSvalor() {
		return svalor;
	}
	public void setSvalor(String svalor) {
		this.svalor = svalor;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public String getVariacao() {
		return variacao;
	}
	public void setVariacao(String variacao) {
		this.variacao = variacao;
	}
	public String getSdata() {
		return sdata;
	}
	public void setSdata(String sdata) {
		this.sdata = sdata;
	}
	public int getQt_lote_padrao() {
		return qt_lote_padrao;
	}
	public void setQt_lote_padrao(int qt_lote_padrao) {
		this.qt_lote_padrao = qt_lote_padrao;
	}
	
}