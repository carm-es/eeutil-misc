package es.mpt.dsic.inside.ws.service.model.pdf;

import java.beans.Transient;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PdfSalida", propOrder = {"contenido", "mime"})

public class PdfSalida implements IPdfSalida {

  @XmlElement(required = true, name = "contenido")
  private byte[] contenido;

  @XmlElement(required = true, name = "mime")
  private String mime;

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

  @Override
  @Transient
  public void setContenido(DataHandler contenido) {
    throw new UnsupportedOperationException("OPERACION NO VALIDA");

  }

}
