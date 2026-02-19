package es.gob.aapp.eeutil.eeutilrestigae.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MDCWrapper {

  @JsonProperty("idApli")
  private String idApli;

  @JsonProperty("uUId")
  private String uUId;

  @JsonProperty("ipClient")
  private String ipClient;

  @JsonProperty("clientHost")
  private String clientHost;

  @JsonProperty("clientURI")
  private String clientURI;

  @JsonProperty("contentLengh")
  private String contentLengh;

  @JsonProperty("ExtraParaM")
  private String ExtraParaM;

  public String getIdApli() {
    return idApli;
  }

  public void setIdApli(String idApli) {
    this.idApli = idApli;
  }

  public String getuUId() {
    return uUId;
  }

  public void setuUId(String uUId) {
    this.uUId = uUId;
  }

  public String getIpClient() {
    return ipClient;
  }

  public void setIpClient(String ipClient) {
    this.ipClient = ipClient;
  }

  public String getClientHost() {
    return clientHost;
  }

  public void setClientHost(String clientHost) {
    this.clientHost = clientHost;
  }

  public String getClientURI() {
    return clientURI;
  }

  public void setClientURI(String clientURI) {
    this.clientURI = clientURI;
  }

  public String getContentLengh() {
    return contentLengh;
  }

  public void setContentLengh(String contentLengh) {
    this.contentLengh = contentLengh;
  }

  public String getExtraParaM() {
    return ExtraParaM;
  }

  public void setExtraParaM(String extraParaM) {
    ExtraParaM = extraParaM;
  }

  public MDCWrapper(String idApli, String uUId, String ipClient, String clientHost,
      String clientURI, String contentLengh, String extraParaM) {
    super();
    this.idApli = idApli;
    this.uUId = uUId;
    this.ipClient = ipClient;
    this.clientHost = clientHost;
    this.clientURI = clientURI;
    this.contentLengh = contentLengh;
    ExtraParaM = extraParaM;
  }



}
