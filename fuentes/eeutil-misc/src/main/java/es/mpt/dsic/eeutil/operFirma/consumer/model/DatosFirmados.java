
package es.mpt.dsic.eeutil.operFirma.consumer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Clase Java para DatosFirmados complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DatosFirmados">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="documento" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="hash" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="algoritmo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatosFirmados", propOrder = {"documento", "hash", "algoritmo"})
public class DatosFirmados {

  protected byte[] documento;
  protected byte[] hash;
  protected String algoritmo;

  /**
   * Obtiene el valor de la propiedad documento.
   * 
   * @return possible object is byte[]
   */
  public byte[] getDocumento() {
    return documento;
  }

  /**
   * Define el valor de la propiedad documento.
   * 
   * @param value allowed object is byte[]
   */
  public void setDocumento(byte[] value) {
    this.documento = value;
  }

  /**
   * Obtiene el valor de la propiedad hash.
   * 
   * @return possible object is byte[]
   */
  public byte[] getHash() {
    return hash;
  }

  /**
   * Define el valor de la propiedad hash.
   * 
   * @param value allowed object is byte[]
   */
  public void setHash(byte[] value) {
    this.hash = value;
  }

  /**
   * Obtiene el valor de la propiedad algoritmo.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getAlgoritmo() {
    return algoritmo;
  }

  /**
   * Define el valor de la propiedad algoritmo.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setAlgoritmo(String value) {
    this.algoritmo = value;
  }

}
