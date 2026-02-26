
package es.mpt.dsic.eeutil.operFirma.consumer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Clase Java para ContenidoInfo complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ContenidoInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contenido" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="tipoMIME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContenidoInfo", propOrder = {"contenido", "tipoMIME"})
public class ContenidoInfo {

  @XmlElement(required = true)
  protected byte[] contenido;
  protected String tipoMIME;

  /**
   * Obtiene el valor de la propiedad contenido.
   * 
   * @return possible object is byte[]
   */
  public byte[] getContenido() {
    return contenido;
  }

  /**
   * Define el valor de la propiedad contenido.
   * 
   * @param value allowed object is byte[]
   */
  public void setContenido(byte[] value) {
    this.contenido = value;
  }

  /**
   * Obtiene el valor de la propiedad tipoMIME.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getTipoMIME() {
    return tipoMIME;
  }

  /**
   * Define el valor de la propiedad tipoMIME.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setTipoMIME(String value) {
    this.tipoMIME = value;
  }

}
