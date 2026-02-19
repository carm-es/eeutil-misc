
package es.mpt.dsic.eeutil.operFirma.consumer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Clase Java para postProcesarFirmaResponse complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="postProcesarFirmaResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FirmaProcesada" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "postProcesarFirmaResponse", propOrder = {"firmaProcesada"})
public class PostProcesarFirmaResponse {

  @XmlElement(name = "FirmaProcesada")
  protected byte[] firmaProcesada;

  /**
   * Obtiene el valor de la propiedad firmaProcesada.
   * 
   * @return possible object is byte[]
   */
  public byte[] getFirmaProcesada() {
    return firmaProcesada;
  }

  /**
   * Define el valor de la propiedad firmaProcesada.
   * 
   * @param value allowed object is byte[]
   */
  public void setFirmaProcesada(byte[] value) {
    this.firmaProcesada = value;
  }

}
