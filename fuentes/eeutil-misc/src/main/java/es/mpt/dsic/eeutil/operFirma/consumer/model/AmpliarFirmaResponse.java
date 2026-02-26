
package es.mpt.dsic.eeutil.operFirma.consumer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Clase Java para ampliarFirmaResponse complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ampliarFirmaResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resultadoAmpliarFirma" type="{http://service.ws.inside.dsic.mpt.es/}resultadoAmpliarFirma" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ampliarFirmaResponse", propOrder = {"resultadoAmpliarFirma"})
public class AmpliarFirmaResponse {

  protected ResultadoAmpliarFirma resultadoAmpliarFirma;

  /**
   * Obtiene el valor de la propiedad resultadoAmpliarFirma.
   * 
   * @return possible object is {@link ResultadoAmpliarFirma }
   * 
   */
  public ResultadoAmpliarFirma getResultadoAmpliarFirma() {
    return resultadoAmpliarFirma;
  }

  /**
   * Define el valor de la propiedad resultadoAmpliarFirma.
   * 
   * @param value allowed object is {@link ResultadoAmpliarFirma }
   * 
   */
  public void setResultadoAmpliarFirma(ResultadoAmpliarFirma value) {
    this.resultadoAmpliarFirma = value;
  }

}
