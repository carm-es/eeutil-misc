
package es.mpt.dsic.eeutil.operFirma.consumer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Clase Java para resultadoAmpliarFirma complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="resultadoAmpliarFirma">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="firma" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultadoAmpliarFirma", propOrder = {"firma"})
public class ResultadoAmpliarFirma {

  protected byte[] firma;

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

}
