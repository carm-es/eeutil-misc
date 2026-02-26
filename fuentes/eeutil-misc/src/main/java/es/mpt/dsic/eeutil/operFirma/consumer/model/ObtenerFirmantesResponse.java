
package es.mpt.dsic.eeutil.operFirma.consumer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Clase Java para obtenerFirmantesResponse complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="obtenerFirmantesResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ListaFirmantes" type="{http://service.ws.inside.dsic.mpt.es/}ListaFirmaInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerFirmantesResponse", propOrder = {"listaFirmantes"})
public class ObtenerFirmantesResponse {

  @XmlElement(name = "ListaFirmantes")
  protected ListaFirmaInfo listaFirmantes;

  /**
   * Obtiene el valor de la propiedad listaFirmantes.
   * 
   * @return possible object is {@link ListaFirmaInfo }
   * 
   */
  public ListaFirmaInfo getListaFirmantes() {
    return listaFirmantes;
  }

  /**
   * Define el valor de la propiedad listaFirmantes.
   * 
   * @param value allowed object is {@link ListaFirmaInfo }
   * 
   */
  public void setListaFirmantes(ListaFirmaInfo value) {
    this.listaFirmantes = value;
  }

}
