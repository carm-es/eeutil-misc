package es.mpt.dsic.inside.ws.service.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SalidaVisualizacion", propOrder = {"documentoContenido"})
public class SalidaVisualizacion {

  @XmlElement(required = true, name = "documentoContenido")
  private DocumentoContenido documentoContenido;

  public DocumentoContenido getDocumentoContenido() {
    return documentoContenido;
  }

  public void setDocumentoContenido(DocumentoContenido documentoContenido) {
    this.documentoContenido = documentoContenido;
  }

}
