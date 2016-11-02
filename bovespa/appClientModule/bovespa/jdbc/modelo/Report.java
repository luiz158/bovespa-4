package bovespa.jdbc.modelo;

import java.util.Calendar;

public class Report {
	/*
	 * select A.ID, A.SIGLA, A.QT_COMPRADA, A.QT_LOTE, A.VL_COMPRA,
	 * datediff(NOW(),A.DT_COMPRA) QT_DIAS, A.DT_COMPRA, A.VL_TOTAL_COM_TAXA,
	 * B.VALOR, (B.VALOR * A.QT_COMPRADA) VALOR_VENDA, (B.VALOR * A.QT_COMPRADA)
	 * - A.VL_TOTAL_COM_TAXA LUCRO_LIQUIDO, TRUNCATE(A.QT_COMPRADA / ((B.VALOR *
	 * A.QT_COMPRADA) - A.VL_TOTAL_COM_TAXA),2) PERCENTUAL FROM
	 * felixpalma.OPERACAO A, felixpalma.ACAO B WHERE A.SIGLA = B.SIGLA
	 */

	private String sigla;
	private double qt_comprada;
	private double qt_lote;
	private double vl_compra;
	private double qt_dias;
	private Calendar dt_compra;
	private double vl_total_com_taxa;
	private double vl_valor;
	private double vl_venda;
	private double vl_lucro_liquido;
	private double vl_percentual;
	private String cd_corretora;
	private String cd_cliente;
	private double vl_total_venda;
	

	public double getVl_total_venda() {
		return vl_total_venda;
	}

	public void setVl_total_venda(double vl_total_venda) {
		this.vl_total_venda = vl_total_venda;
	}

	public String getCd_corretora() {
		return cd_corretora;
	}

	public void setCd_corretora(String cd_corretora) {
		this.cd_corretora = cd_corretora;
	}

	public String getCd_cliente() {
		return cd_cliente;
	}

	public void setCd_cliente(String cd_cliente) {
		this.cd_cliente = cd_cliente;
	}

	public double getVl_venda() {
		return vl_venda;
	}

	public void setVl_venda(double vl_venda) {
		this.vl_venda = vl_venda;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public double getQt_comprada() {
		return qt_comprada;
	}

	public void setQt_comprada(double qt_comprada) {
		this.qt_comprada = qt_comprada;
	}

	public double getQt_lote() {
		return qt_lote;
	}

	public void setQt_lote(double qt_lote) {
		this.qt_lote = qt_lote;
	}

	public double getVl_compra() {
		return vl_compra;
	}

	public void setVl_compra(double vl_compra) {
		this.vl_compra = vl_compra;
	}

	public double getQt_dias() {
		return qt_dias;
	}

	public void setQt_dias(double qt_dias) {
		this.qt_dias = qt_dias;
	}

	public Calendar getDt_compra() {
		return dt_compra;
	}

	public void setDt_compra(Calendar dt_compra) {
		this.dt_compra = dt_compra;
	}

	public double getVl_total_com_taxa() {
		return vl_total_com_taxa;
	}

	public void setVl_total_com_taxa(double vl_total_com_taxa) {
		this.vl_total_com_taxa = vl_total_com_taxa;
	}

	public double getVl_valor() {
		return vl_valor;
	}

	public void setVl_valor(double vl_valor) {
		this.vl_valor = vl_valor;
	}

	public double getVl_lucro_liquido() {
		return vl_lucro_liquido;
	}

	public void setVl_lucro_liquido(double vl_lucro_liquido) {
		this.vl_lucro_liquido = vl_lucro_liquido;
	}

	public double getVl_percentual() {
		return vl_percentual;
	}

	public void setVl_percentual(double vl_percentual) {
		this.vl_percentual = vl_percentual;
	}

}
