
package es.mpt.dsic.eeutil.operFirma.consumer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Clase Java para ResultadoValidacionFirmaInfo complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ResultadoValidacionFirmaInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="detalle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numeroFirmantes" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="certificados" type="{http://service.ws.inside.dsic.mpt.es/}CertificadosLista" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultadoValidacionFirmaInfo",
    propOrder = {"estado", "detalle", "numeroFirmantes", "certificados"})
public class ResultadoValidacionFirmaInfo {

  protected boolean estado;
  @XmlElement(required = true)
  protected String detalle;
  protected int numeroFirmantes;
  protected CertificadosLista certificados;

  /**
   * Obtiene el valor de la propiedad estado.
   * 
   */
  public boolean isEstado() {
    return estado;
  }

  /**
   * Define el valor de la propiedad estado.
   * 
   */
  public void setEstado(boolean value) {
    this.estado = value;
  }

  /**
   * Obtiene el valor de la propiedad detalle.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDetalle() {
    return detalle;
  }

  /**
   * Define el valor de la propiedad detalle.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setDetalle(String value) {
    this.detalle = value;
  }

  /**
   * Obtiene el valor de la propiedad numeroFirmantes.
   * 
   */
  public int getNumeroFirmantes() {
    return numeroFirmantes;
  }

  /**
   * Define el valor de la propiedad numeroFirmantes.
   * 
   */
  public void setNumeroFirmantes(int value) {
    this.numeroFirmantes = value;
  }

  /**
   * Obtiene el valor de la propiedad certificados.
   * 
   * @return possible object is {@link CertificadosLista }
   * 
   */
  public CertificadosLista getCertificados() {
    return certificados;
  }

  /**
   * Define el valor de la propiedad certificados.
   * 
   * @param value allowed object is {@link CertificadosLista }
   * 
   */
  public void setCertificados(CertificadosLista value) {
    this.certificados = value;
  }

}
