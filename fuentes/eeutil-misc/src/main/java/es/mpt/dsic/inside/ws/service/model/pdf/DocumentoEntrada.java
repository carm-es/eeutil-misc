package es.mpt.dsic.inside.ws.service.model.pdf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import es.mpt.dsic.inside.exception.base.DataWsEntradaBase;
import es.mpt.dsic.inside.exception.interfaz.IMDCAble;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentoEntrada", propOrder = {"contenido", "mime", "password"})

public class DocumentoEntrada extends DataWsEntradaBase implements IMDCAble {

  @XmlElement(required = true, name = "contenido")
  private byte[] contenido;

  @XmlElement(required = false, name = "mime")
  private String mime;

  @XmlElement(required = false, name = "password")
  private String password;

  public byte[] getContenido() {
    return contenido;
  }

  public void setContenido(byte[] contenido) {
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
