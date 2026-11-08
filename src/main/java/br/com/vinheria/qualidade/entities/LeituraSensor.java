package br.com.vinheria.qualidade.entities;

import java.sql.Timestamp;

// Representa uma leitura de sensor de temperatura ou umidade na adega
public class LeituraSensor {

    private Long id;
    private String tipoSensor;    // TEMPERATURA ou UMIDADE
    private Double valor;
    private String localizacao;   // ex: "Adega Principal", "Barril-07"
    private Timestamp registradoEm;

    public LeituraSensor() {}

    public LeituraSensor(Long id, String tipoSensor, Double valor, String localizacao, Timestamp registradoEm) {
        this.id = id;
        this.tipoSensor = tipoSensor;
        this.valor = valor;
        this.localizacao = localizacao;
        this.registradoEm = registradoEm;
    }

    public LeituraSensor(String tipoSensor, Double valor, String localizacao, Timestamp registradoEm) {
        this.tipoSensor = tipoSensor;
        this.valor = valor;
        this.localizacao = localizacao;
        this.registradoEm = registradoEm;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoSensor() { return tipoSensor; }
    public void setTipoSensor(String tipoSensor) { this.tipoSensor = tipoSensor; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public Timestamp getRegistradoEm() { return registradoEm; }
    public void setRegistradoEm(Timestamp registradoEm) { this.registradoEm = registradoEm; }
}
