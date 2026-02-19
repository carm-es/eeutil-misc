
package es.mpt.dsic.eeutil.operFirma.consumer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Clase Java para validacionFirmaInfoResponse complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="validacionFirmaInfoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResultadoValidacionFirmaInfo" type="{http://service.ws.inside.dsic.mpt.es/}ResultadoValidacionFirmaInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "validacionFirmaInfoResponse", propOrder = {"resultadoValidacionFirmaInfo"})
public class ValidacionFirmaInfoResponse {

  @XmlElement(name = "ResultadoValidacionFirmaInfo")
  protected ResultadoValidacionFirmaInfo resultadoValidacionFirmaInfo;

  /**
   * Obtiene el valor de la propiedad resultadoValidacionFirmaInfo.
   * 
   * @return possible object is {@link ResultadoValidacionFirmaInfo }
   * 
   */
  public ResultadoValidacionFirmaInfo getResultadoValidacionFirmaInfo() {
    return resultadoValidacionFirmaInfo;
  }

  /**
   * Define el valor de la propiedad resultadoValidacionFirmaInfo.
   * 
   * @param value allowed object is {@link ResultadoValidacionFirmaInfo }
   * 
   */
  public void setResultadoValidacionFirmaInfo(ResultadoValidacionFirmaInfo value) {
    this.resultadoValidacionFirmaInfo = value;
  }

}
