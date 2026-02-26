package es.mpt.dsic.inside.ws.service.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "documentoContenido", propOrder = {"bytesDocumento", "mimeDocumento"})

public class DocumentoContenido {

  @XmlElement(required = true, name = "bytesDocumento")
  private byte[] bytesDocumento;
  @XmlElement(required = true, name = "mimeDocumento")
  private String mimeDocumento;

  public byte[] getBytesDocumento() {
    return bytesDocumento;
  }

  public void setBytesDocumento(byte[] bytesDocumento) {
    this.bytesDocumento = bytesDocumento;
  }

  public String getMimeDocumento() {
    return mimeDocumento;
  }

  public void setMimeDocumento(String mimeDocumento) {
    this.mimeDocumento = mimeDocumento;
  }


}
