/*
 * Copyright (C) 2012-13 MINHAP, Gobierno de España This program is licensed and may be used,
 * modified and redistributed under the terms of the European Public License (EUPL), either version
 * 1.1 or (at your option) any later version as soon as they are approved by the European
 * Commission. Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * more details. You should have received a copy of the EUPL1.1 license along with this program; if
 * not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */


package es.mpt.dsic.eeutil.operFirma.consumer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Clase Java para opcionesObtenerInformacionFirma complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="opcionesObtenerInformacionFirma">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="obtenerFirmantes" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="obtenerDatosFirmados" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="obtenerTipoFirma" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "opcionesObtenerInformacionFirma",
    propOrder = {"obtenerFirmantes", "obtenerDatosFirmados", "obtenerTipoFirma"})
public class OpcionesObtenerInformacionFirma {

  protected boolean obtenerFirmantes;
  protected boolean obtenerDatosFirmados;
  protected boolean obtenerTipoFirma;

  /**
   * Obtiene el valor de la propiedad obtenerFirmantes.
   * 
   */
  public boolean isObtenerFirmantes() {
    return obtenerFirmantes;
  }

  /**
   * Define el valor de la propiedad obtenerFirmantes.
   * 
   */
  public void setObtenerFirmantes(boolean value) {
    this.obtenerFirmantes = value;
  }

  /**
   * Obtiene el valor de la propiedad obtenerDatosFirmados.
   * 
   */
  public boolean isObtenerDatosFirmados() {
    return obtenerDatosFirmados;
  }

  /**
   * Define el valor de la propiedad obtenerDatosFirmados.
   * 
   */
  public void setObtenerDatosFirmados(boolean value) {
    this.obtenerDatosFirmados = value;
  }

  /**
   * Obtiene el valor de la propiedad obtenerTipoFirma.
   * 
   */
  public boolean isObtenerTipoFirma() {
    return obtenerTipoFirma;
  }

  /**
   * Define el valor de la propiedad obtenerTipoFirma.
   * 
   */
  public void setObtenerTipoFirma(boolean value) {
    this.obtenerTipoFirma = value;
  }

}
