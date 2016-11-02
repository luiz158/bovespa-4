package bovespa.jdbc.modelo;

import java.util.Calendar;

public class Processo {
	private Double id;
	private String cd_processo;
	private String ds_processo;
	private String cd_classe;
	private String cd_area;
	private String cd_local_fisico;
	private String cd_distribuicao;
	private String cd_juiz;
	private String ds_valor_acao;
	private String cd_email;
	private Integer nr_hora_envio;
	private String ds_ultimo_movimento;
	private Calendar dt_ultima_verificacao;
	private Calendar dt_envio_notificacao;
	private String ds_envio_notificacao;
	private String ds_ultima_verificacao;
	private String Ds_dt_envio_notificacao;
	private Integer nr_dias_alz;
	
	public Processo(){
		
	}
	public Integer getNr_dias_alz() {
		return nr_dias_alz;
	}

	public void setNr_dias_alz(Integer nr_dias_alz) {
		this.nr_dias_alz = nr_dias_alz;
	}

	public String getDs_dt_envio_notificacao() {
		return Ds_dt_envio_notificacao;
	}

	public void setDs_dt_envio_notificacao(String ds_dt_envio_notificacao) {
		Ds_dt_envio_notificacao = ds_dt_envio_notificacao;
	}

	public String getDs_envio_notificacao() {
		return ds_envio_notificacao;
	}

	public void setDs_envio_notificacao(String ds_envio_notificacao) {
		this.ds_envio_notificacao = ds_envio_notificacao;
	}

	public String getDs_ultima_verificacao() {
		return ds_ultima_verificacao;
	}

	public void setDs_ultima_verificacao(String ds_ultima_verificacao) {
		this.ds_ultima_verificacao = ds_ultima_verificacao;
	}

	public String getDs_ultimo_movimento() {
		return ds_ultimo_movimento;
	}

	public void setDs_ultimo_movimento(String ds_ultimo_movimento) {
		this.ds_ultimo_movimento = ds_ultimo_movimento;
	}

	public Calendar getDt_ultima_verificacao() {
		return dt_ultima_verificacao;
	}

	public void setDt_ultima_verificacao(Calendar dt_ultima_verificacao) {
		this.dt_ultima_verificacao = dt_ultima_verificacao;
	}

	public Calendar getDt_envio_notificacao() {
		return dt_envio_notificacao;
	}

	public void setDt_envio_notificacao(Calendar dt_envio_notificacao) {
		this.dt_envio_notificacao = dt_envio_notificacao;
	}

	public Double getId() {
		return id;
	}

	public String getCd_email() {
		return cd_email;
	}

	public void setCd_email(String cd_email) {
		this.cd_email = cd_email;
	}

	public Integer getNr_hora_envio() {
		return nr_hora_envio;
	}

	public void setNr_hora_envio(Integer nr_hora_envio) {
		this.nr_hora_envio = nr_hora_envio;
	}

	public void setId(Double id) {
		this.id = id;
	}

	public String getCd_processo() {
		return cd_processo;
	}

	public void setCd_processo(String cd_processo) {
		this.cd_processo = cd_processo;
	}

	public String getDs_processo() {
		return ds_processo;
	}

	public void setDs_processo(String ds_processo) {
		this.ds_processo = ds_processo;
	}

	public String getCd_classe() {
		return cd_classe;
	}

	public void setCd_classe(String cd_classe) {
		this.cd_classe = cd_classe;
	}

	public String getCd_area() {
		return cd_area;
	}

	public void setCd_area(String cd_area) {
		this.cd_area = cd_area;
	}

	public String getCd_local_fisico() {
		return cd_local_fisico;
	}

	public void setCd_local_fisico(String cd_local_fisico) {
		this.cd_local_fisico = cd_local_fisico;
	}

	public String getCd_distribuicao() {
		return cd_distribuicao;
	}

	public void setCd_distribuicao(String cd_distribuicao) {
		this.cd_distribuicao = cd_distribuicao;
	}

	public String getCd_juiz() {
		return cd_juiz;
	}

	public void setCd_juiz(String cd_juiz) {
		this.cd_juiz = cd_juiz;
	}

	public String getDs_valor_acao() {
		return ds_valor_acao;
	}

	public void setDs_valor_acao(String ds_valor_acao) {
		this.ds_valor_acao = ds_valor_acao;
	}

}
