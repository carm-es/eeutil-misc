package es.mpt.dsic.inside.ws.service.model.pdf;

import java.beans.Transient;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PdfSalidaMtom", propOrder = {"contenido", "mime"})

public class PdfSalidaMtom implements IPdfSalida {

  @XmlElement(required = true, name = "contenido")
  @XmlMimeType("application/octet-stream")
  private DataHandler contenido;

  @XmlElement(required = true, name = "mime")
  private String mime;

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

  @Override
  @Transient
  public void setContenido(byte[] entrada) {
    // throw new UnsupportedOperationException("OPERACION NO VALIDA");
    contenido = new DataHandler(entrada, "application/octet-stream");
  }

}
