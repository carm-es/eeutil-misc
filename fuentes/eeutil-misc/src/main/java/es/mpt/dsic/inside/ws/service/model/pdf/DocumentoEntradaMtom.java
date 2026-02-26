package es.mpt.dsic.inside.ws.service.model.pdf;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;

import es.mpt.dsic.inside.exception.base.DataWsEntradaBase;
import es.mpt.dsic.inside.exception.interfaz.IMDCAble;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentoEntradaMtom", propOrder = {"contenido", "mime", "password"})

public class DocumentoEntradaMtom extends DataWsEntradaBase implements IMDCAble {

  @XmlElement(required = true, name = "contenido")
  @XmlMimeType("application/octet-stream")
  private DataHandler contenido;

  @XmlElement(required = false, name = "mime")
  private String mime;

  @XmlElement(required = false, name = "password")
  private String password;

  public DataHandler getContenido() {
    return contenido;
  }

  public void setContenido(DataHandler contenido) {
    this.contenido = contenido;
  }

  public String getMime() {
    return mime;
  }

  public void setMime(String mime) {
    this.mime = mime;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
