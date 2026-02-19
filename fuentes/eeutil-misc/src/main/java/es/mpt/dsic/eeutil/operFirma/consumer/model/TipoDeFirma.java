
package es.mpt.dsic.eeutil.operFirma.consumer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Clase Java para TipoDeFirma complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TipoDeFirma">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipoFirma" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="modoFirma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoDeFirma", propOrder = {"tipoFirma", "modoFirma"})
public class TipoDeFirma {

  @XmlElement(required = true)
  protected String tipoFirma;
  protected String modoFirma;

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
   * Obtiene el valor de la propiedad modoFirma.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getModoFirma() {
    return modoFirma;
  }

  /**
   * Define el valor de la propiedad modoFirma.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setModoFirma(String value) {
    this.modoFirma = value;
  }

}
