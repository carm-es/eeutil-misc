
package es.mpt.dsic.eeutil.operFirma.consumer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Clase Java para validacionFirmaResponse complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="validacionFirmaResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResultadoValidacionInfo" type="{http://service.ws.inside.dsic.mpt.es/}ResultadoValidacionInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "validacionFirmaResponse", propOrder = {"resultadoValidacionInfo"})
public class ValidacionFirmaResponse {

  @XmlElement(name = "ResultadoValidacionInfo")
  protected ResultadoValidacionInfo resultadoValidacionInfo;

  /**
   * Obtiene el valor de la propiedad resultadoValidacionInfo.
   * 
   * @return possible object is {@link ResultadoValidacionInfo }
   * 
   */
  public ResultadoValidacionInfo getResultadoValidacionInfo() {
    return resultadoValidacionInfo;
  }

  /**
   * Define el valor de la propiedad resultadoValidacionInfo.
   * 
   * @param value allowed object is {@link ResultadoValidacionInfo }
   * 
   */
  public void setResultadoValidacionInfo(ResultadoValidacionInfo value) {
    this.resultadoValidacionInfo = value;
  }

}
