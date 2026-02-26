
package es.mpt.dsic.eeutil.operFirma.consumer.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Clase Java para ListaFirmaInfo complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ListaFirmaInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="informacionFirmas">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="informacionFirmas" type="{http://service.ws.inside.dsic.mpt.es/}FirmaInfo" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListaFirmaInfo", propOrder = {"informacionFirmas"})
public class ListaFirmaInfo {

  @XmlElement(required = true)
  protected ListaFirmaInfo.InformacionFirmas informacionFirmas;

  /**
   * Obtiene el valor de la propiedad informacionFirmas.
   * 
   * @return possible object is {@link ListaFirmaInfo.InformacionFirmas }
   * 
   */
  public ListaFirmaInfo.InformacionFirmas getInformacionFirmas() {
    return informacionFirmas;
  }

  /**
   * Define el valor de la propiedad informacionFirmas.
   * 
   * @param value allowed object is {@link ListaFirmaInfo.InformacionFirmas }
   * 
   */
  public void setInformacionFirmas(ListaFirmaInfo.InformacionFirmas value) {
    this.informacionFirmas = value;
  }


  /**
   * <p>
   * Clase Java para anonymous complex type.
   * 
   * <p>
   * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
   * 
   * <pre>
   * &lt;complexType>
   *   &lt;complexContent>
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
   *       &lt;sequence>
   *         &lt;element name="informacionFirmas" type="{http://service.ws.inside.dsic.mpt.es/}FirmaInfo" maxOccurs="unbounded"/>
   *       &lt;/sequence>
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   * 
   * 
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {"informacionFirmas"})
  public static class InformacionFirmas {

    @XmlElement(required = true)
    protected List<FirmaInfo> informacionFirmas;

    /**
     * Gets the value of the informacionFirmas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the informacionFirmas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getInformacionFirmas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link FirmaInfo }
     * 
     * 
     */
    public List<FirmaInfo> getInformacionFirmas() {
      if (informacionFirmas == null) {
        informacionFirmas = new ArrayList<>();
      }
      return this.informacionFirmas;
    }

  }

}
