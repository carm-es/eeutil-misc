
package es.mpt.dsic.eeutil.operFirma.consumer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Clase Java para obtenerInformacionFirmaResponse complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="obtenerInformacionFirmaResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resultadoObtenerInformacionFirma" type="{http://service.ws.inside.dsic.mpt.es/}InformacionFirma" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerInformacionFirmaResponse", propOrder = {"resultadoObtenerInformacionFirma"})
public class ObtenerInformacionFirmaResponse {

  protected InformacionFirma resultadoObtenerInformacionFirma;

  /**
   * Obtiene el valor de la propiedad resultadoObtenerInformacionFirma.
   * 
   * @return possible object is {@link InformacionFirma }
   * 
   */
  public InformacionFirma getResultadoObtenerInformacionFirma() {
    return resultadoObtenerInformacionFirma;
  }

  /**
   * Define el valor de la propiedad resultadoObtenerInformacionFirma.
   * 
   * @param value allowed object is {@link InformacionFirma }
   * 
   */
  public void setResultadoObtenerInformacionFirma(InformacionFirma value) {
    this.resultadoObtenerInformacionFirma = value;
  }

}
