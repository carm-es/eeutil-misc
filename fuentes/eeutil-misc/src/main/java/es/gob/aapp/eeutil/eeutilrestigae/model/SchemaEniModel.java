package es.gob.aapp.eeutil.eeutilrestigae.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchemaEniModel {

  @JsonProperty("entrada")
  public byte[] entrada;

  @JsonProperty("version")
  public String version;

  @JsonProperty("mdcWrapper")
  public MDCWrapper mdcWrapper;


  public MDCWrapper getMdcWrapper() {
    return mdcWrapper;
  }

  public void setMdcWrapper(MDCWrapper mdcWrapper) {
    this.mdcWrapper = mdcWrapper;
  }

  public byte[] getEntrada() {
    return entrada;
  }

  public void setEntrada(byte[] entrada) {
    this.entrada = entrada;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }



}
