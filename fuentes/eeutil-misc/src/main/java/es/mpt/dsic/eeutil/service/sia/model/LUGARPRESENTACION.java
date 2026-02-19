
package es.mpt.dsic.eeutil.service.sia.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Clase Java para LUGARPRESENTACION complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="LUGARPRESENTACION">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CODLUGARPRESENTACION" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DESLUGARPRESENTACION" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LUGARPRESENTACION",
    namespace = "http://www.map.es/sgca/consultar/messages/v2_4/EnviaSIA",
    propOrder = {"codlugarpresentacion", "deslugarpresentacion"})
public class LUGARPRESENTACION {

  @XmlElement(name = "CODLUGARPRESENTACION")
  protected String codlugarpresentacion;
  @XmlElement(name = "DESLUGARPRESENTACION")
  protected String deslugarpresentacion;

  /**
   * Obtiene el valor de la propiedad codlugarpresentacion.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCODLUGARPRESENTACION() {
    return codlugarpresentacion;
  }

  /**
   * Define el valor de la propiedad codlugarpresentacion.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setCODLUGARPRESENTACION(String value) {
    this.codlugarpresentacion = value;
  }

  /**
   * Obtiene el valor de la propiedad deslugarpresentacion.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDESLUGARPRESENTACION() {
    return deslugarpresentacion;
  }

  /**
   * Define el valor de la propiedad deslugarpresentacion.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setDESLUGARPRESENTACION(String value) {
    this.deslugarpresentacion = value;
  }

}
