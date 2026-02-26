
package es.mpt.dsic.eeutil.operFirma.consumer.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Clase Java para EstadoInfo complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="EstadoInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EstadoInfo", propOrder = {"status", "codigo", "descripcion"})
public class EstadoInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  protected String status;
  protected String codigo;
  protected String descripcion;

  /**
   * Obtiene el valor de la propiedad status.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getStatus() {
    return status;
  }

  /**
   * Define el valor de la propiedad status.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setStatus(String value) {
    this.status = value;
  }

  /**
   * Obtiene el valor de la propiedad codigo.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getCodigo() {
    return codigo;
  }

  /**
   * Define el valor de la propiedad codigo.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setCodigo(String value) {
    this.codigo = value;
  }

  /**
   * Obtiene el valor de la propiedad descripcion.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDescripcion() {
    return descripcion;
  }

  /**
   * Define el valor de la propiedad descripcion.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setDescripcion(String value) {
    this.descripcion = value;
  }

}
