package es.mpt.dsic.eeutil.operFirma.consumer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Contains a List of {@link Document}
 * 
 * @version 2.0.0
 * @since 2.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CertificadosLista", propOrder = {"certificado"})
public class CertificadosLista implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @XmlElement(required = true)
  protected List<String> certificado;

  /**
   * Gets the list of certificates.
   * 
   * @return {@link byte[]} list.
   */
  public List<String> getCertificado() {
    if (certificado == null) {
      certificado = new ArrayList<String>();
    }
    return this.certificado;
  }

  public void setCertificado(List<String> certificado) {
    this.certificado = certificado;
  }

}
