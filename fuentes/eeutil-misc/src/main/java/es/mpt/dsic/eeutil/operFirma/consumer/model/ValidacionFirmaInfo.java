
package es.mpt.dsic.eeutil.operFirma.consumer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Clase Java para validacionFirmaInfo complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="validacionFirmaInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="aplicacionInfo" type="{http://service.ws.inside.dsic.mpt.es/}applicationLogin"/>
 *         &lt;element name="Firma" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="TipoFirma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DatosFirmados" type="{http://service.ws.inside.dsic.mpt.es/}DatosFirmados" minOccurs="0"/>
 *         &lt;element name="InfoCertificados" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "validacionFirmaInfo",
    propOrder = {"aplicacionInfo", "firma", "tipoFirma", "datosFirmados", "infoCertificados"})
public class ValidacionFirmaInfo {

  @XmlElement(required = true)
  protected ApplicationLogin aplicacionInfo;
  @XmlElement(name = "Firma", required = true)
  protected byte[] firma;
  @XmlElement(name = "TipoFirma")
  protected String tipoFirma;
  @XmlElement(name = "DatosFirmados")
  protected DatosFirmados datosFirmados;
  @XmlElement(name = "InfoCertificados")
  protected boolean infoCertificados;

  /**
   * Obtiene el valor de la propiedad aplicacionInfo.
   * 
   * @return possible object is {@link ApplicationLogin }
   * 
   */
  public ApplicationLogin getAplicacionInfo() {
    return aplicacionInfo;
  }

  /**
   * Define el valor de la propiedad aplicacionInfo.
   * 
   * @param value allowed object is {@link ApplicationLogin }
   * 
   */
  public void setAplicacionInfo(ApplicationLogin value) {
    this.aplicacionInfo = value;
  }

  /**
   * Obtiene el valor de la propiedad firma.
   * 
   * @return possible object is byte[]
   */
  public byte[] getFirma() {
    return firma;
  }

  /**
   * Define el valor de la propiedad firma.
   * 
   * @param value allowed object is byte[]
   */
  public void setFirma(byte[] value) {
    this.firma = value;
  }

  /**
   * Obtiene el valor de la propiedad tipoFirma.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getTipoFirma() {
    return tipoFirma;
  }

  /**
   * Define el valor de la propiedad tipoFirma.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setTipoFirma(String value) {
    this.tipoFirma = value;
  }

  /**
   * Obtiene el valor de la propiedad datosFirmados.
   * 
   * @return possible object is {@link DatosFirmados }
   * 
   */
  public DatosFirmados getDatosFirmados() {
    return datosFirmados;
  }

  /**
   * Define el valor de la propiedad datosFirmados.
   * 
   * @param value allowed object is {@link DatosFirmados }
   * 
   */
  public void setDatosFirmados(DatosFirmados value) {
    this.datosFirmados = value;
  }

  /**
   * Obtiene el valor de la propiedad infoCertificados.
   * 
   */
  public boolean isInfoCertificados() {
    return infoCertificados;
  }

  /**
   * Define el valor de la propiedad infoCertificados.
   * 
   */
  public void setInfoCertificados(boolean value) {
    this.infoCertificados = value;
  }

}
