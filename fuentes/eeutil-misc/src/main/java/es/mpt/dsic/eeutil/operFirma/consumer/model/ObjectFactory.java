
package es.mpt.dsic.eeutil.operFirma.consumer.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the es.mpt.dsic.eeutil.operFirma.consumer.model package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation
 * for XML content. The Java representation of XML content can consist of schema derived interfaces
 * and classes representing the binding of schema type definitions, element declarations and model
 * groups. Factory methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

  private static final String NAMESPACE_URI_SERVICE = "http://service.ws.inside.dsic.mpt.es/";

  private static final QName _ObtenerFirmantesResponse_QNAME =
      new QName(NAMESPACE_URI_SERVICE, "obtenerFirmantesResponse");
  private static final QName _PostProcesarFirma_QNAME =
      new QName(NAMESPACE_URI_SERVICE, "postProcesarFirma");
  private static final QName _ErrorTest_QNAME = new QName(NAMESPACE_URI_SERVICE, "ErrorTest");
  private static final QName _ValidacionFirmaResponse_QNAME =
      new QName(NAMESPACE_URI_SERVICE, "validacionFirmaResponse");
  private static final QName _ObtenerInformacionFirma_QNAME =
      new QName(NAMESPACE_URI_SERVICE, "obtenerInformacionFirma");
  private static final QName _ValidarCertificado_QNAME =
      new QName(NAMESPACE_URI_SERVICE, "validarCertificado");
  private static final QName _AmpliarFirma_QNAME = new QName(NAMESPACE_URI_SERVICE, "ampliarFirma");
  private static final QName _ObtenerInformacionFirmaResponse_QNAME =
      new QName(NAMESPACE_URI_SERVICE, "obtenerInformacionFirmaResponse");
  private static final QName _PostProcesarFirmaResponse_QNAME =
      new QName(NAMESPACE_URI_SERVICE, "postProcesarFirmaResponse");
  private static final QName _ValidarCertificadoResponse_QNAME =
      new QName(NAMESPACE_URI_SERVICE, "validarCertificadoResponse");
  private static final QName _AmpliarFirmaResponse_QNAME =
      new QName(NAMESPACE_URI_SERVICE, "ampliarFirmaResponse");
  private static final QName _ValidacionFirma_QNAME =
      new QName(NAMESPACE_URI_SERVICE, "validacionFirma");
  private static final QName _ObtenerFirmantes_QNAME =
      new QName(NAMESPACE_URI_SERVICE, "obtenerFirmantes");

  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes
   * for package: es.mpt.dsic.eeutil.operFirma.consumer.model
   * 
   */
  public ObjectFactory() {
    // Do nothing.
  }

  /**
   * Create an instance of {@link ListaFirmaInfo }
   * 
   */
  public ListaFirmaInfo createListaFirmaInfo() {
    return new ListaFirmaInfo();
  }

  /**
   * Create an instance of {@link ValidacionFirmaResponse }
   * 
   */
  public ValidacionFirmaResponse createValidacionFirmaResponse() {
    return new ValidacionFirmaResponse();
  }

  /**
   * Create an instance of {@link PostProcesarFirma }
   * 
   */
  public PostProcesarFirma createPostProcesarFirma() {
    return new PostProcesarFirma();
  }

  /**
   * Create an instance of {@link EstadoInfo }
   * 
   */
  public EstadoInfo createEstadoInfo() {
    return new EstadoInfo();
  }

  /**
   * Create an instance of {@link ObtenerFirmantesResponse }
   * 
   */
  public ObtenerFirmantesResponse createObtenerFirmantesResponse() {
    return new ObtenerFirmantesResponse();
  }

  /**
   * Create an instance of {@link AmpliarFirmaResponse }
   * 
   */
  public AmpliarFirmaResponse createAmpliarFirmaResponse() {
    return new AmpliarFirmaResponse();
  }

  /**
   * Create an instance of {@link ValidacionFirma }
   * 
   */
  public ValidacionFirma createValidacionFirma() {
    return new ValidacionFirma();
  }

  /**
   * Create an instance of {@link ObtenerFirmantes }
   * 
   */
  public ObtenerFirmantes createObtenerFirmantes() {
    return new ObtenerFirmantes();
  }

  /**
   * Create an instance of {@link ObtenerInformacionFirmaResponse }
   * 
   */
  public ObtenerInformacionFirmaResponse createObtenerInformacionFirmaResponse() {
    return new ObtenerInformacionFirmaResponse();
  }

  /**
   * Create an instance of {@link PostProcesarFirmaResponse }
   * 
   */
  public PostProcesarFirmaResponse createPostProcesarFirmaResponse() {
    return new PostProcesarFirmaResponse();
  }

  /**
   * Create an instance of {@link ValidarCertificadoResponse }
   * 
   */
  public ValidarCertificadoResponse createValidarCertificadoResponse() {
    return new ValidarCertificadoResponse();
  }

  /**
   * Create an instance of {@link AmpliarFirma }
   * 
   */
  public AmpliarFirma createAmpliarFirma() {
    return new AmpliarFirma();
  }

  /**
   * Create an instance of {@link ObtenerInformacionFirma }
   * 
   */
  public ObtenerInformacionFirma createObtenerInformacionFirma() {
    return new ObtenerInformacionFirma();
  }

  /**
   * Create an instance of {@link ValidarCertificado }
   * 
   */
  public ValidarCertificado createValidarCertificado() {
    return new ValidarCertificado();
  }

  /**
   * Create an instance of {@link ResultadoValidacionInfo }
   * 
   */
  public ResultadoValidacionInfo createResultadoValidacionInfo() {
    return new ResultadoValidacionInfo();
  }

  /**
   * Create an instance of {@link ConfiguracionAmpliarFirma }
   * 
   */
  public ConfiguracionAmpliarFirma createConfiguracionAmpliarFirma() {
    return new ConfiguracionAmpliarFirma();
  }

  /**
   * Create an instance of {@link FirmaInfo }
   * 
   */
  public FirmaInfo createFirmaInfo() {
    return new FirmaInfo();
  }

  /**
   * Create an instance of {@link ResultadoValidarCertificado }
   * 
   */
  public ResultadoValidarCertificado createResultadoValidarCertificado() {
    return new ResultadoValidarCertificado();
  }

  /**
   * Create an instance of {@link TipoDeFirma }
   * 
   */
  public TipoDeFirma createTipoDeFirma() {
    return new TipoDeFirma();
  }

  /**
   * Create an instance of {@link OpcionesObtenerInformacionFirma }
   * 
   */
  public OpcionesObtenerInformacionFirma createOpcionesObtenerInformacionFirma() {
    return new OpcionesObtenerInformacionFirma();
  }

  /**
   * Create an instance of {@link DatosFirmados }
   * 
   */
  public DatosFirmados createDatosFirmados() {
    return new DatosFirmados();
  }

  /**
   * Create an instance of {@link ContenidoInfo }
   * 
   */
  public ContenidoInfo createContenidoInfo() {
    return new ContenidoInfo();
  }

  /**
   * Create an instance of {@link ApplicationLogin }
   * 
   */
  public ApplicationLogin createApplicationLogin() {
    return new ApplicationLogin();
  }

  /**
   * Create an instance of {@link ResultadoAmpliarFirma }
   * 
   */
  public ResultadoAmpliarFirma createResultadoAmpliarFirma() {
    return new ResultadoAmpliarFirma();
  }

  /**
   * Create an instance of {@link InformacionFirma }
   * 
   */
  public InformacionFirma createInformacionFirma() {
    return new InformacionFirma();
  }

  /**
   * Create an instance of {@link CertificateList }
   * 
   */
  public CertificateList createCertificateList() {
    return new CertificateList();
  }

  /**
   * Create an instance of {@link ListaFirmaInfo.InformacionFirmas }
   * 
   */
  public ListaFirmaInfo.InformacionFirmas createListaFirmaInfoInformacionFirmas() {
    return new ListaFirmaInfo.InformacionFirmas();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerFirmantesResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/",
      name = "obtenerFirmantesResponse")
  public JAXBElement<ObtenerFirmantesResponse> createObtenerFirmantesResponse(
      ObtenerFirmantesResponse value) {
    return new JAXBElement<>(_ObtenerFirmantesResponse_QNAME, ObtenerFirmantesResponse.class, null,
        value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link PostProcesarFirma }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "postProcesarFirma")
  public JAXBElement<PostProcesarFirma> createPostProcesarFirma(PostProcesarFirma value) {
    return new JAXBElement<>(_PostProcesarFirma_QNAME, PostProcesarFirma.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link EstadoInfo }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "ErrorTest")
  public JAXBElement<EstadoInfo> createErrorTest(EstadoInfo value) {
    return new JAXBElement<>(_ErrorTest_QNAME, EstadoInfo.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link ValidacionFirmaResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/",
      name = "validacionFirmaResponse")
  public JAXBElement<ValidacionFirmaResponse> createValidacionFirmaResponse(
      ValidacionFirmaResponse value) {
    return new JAXBElement<>(_ValidacionFirmaResponse_QNAME, ValidacionFirmaResponse.class, null,
        value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerInformacionFirma }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/",
      name = "obtenerInformacionFirma")
  public JAXBElement<ObtenerInformacionFirma> createObtenerInformacionFirma(
      ObtenerInformacionFirma value) {
    return new JAXBElement<>(_ObtenerInformacionFirma_QNAME, ObtenerInformacionFirma.class, null,
        value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link ValidarCertificado }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "validarCertificado")
  public JAXBElement<ValidarCertificado> createValidarCertificado(ValidarCertificado value) {
    return new JAXBElement<>(_ValidarCertificado_QNAME, ValidarCertificado.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link AmpliarFirma }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "ampliarFirma")
  public JAXBElement<AmpliarFirma> createAmpliarFirma(AmpliarFirma value) {
    return new JAXBElement<>(_AmpliarFirma_QNAME, AmpliarFirma.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerInformacionFirmaResponse
   * }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/",
      name = "obtenerInformacionFirmaResponse")
  public JAXBElement<ObtenerInformacionFirmaResponse> createObtenerInformacionFirmaResponse(
      ObtenerInformacionFirmaResponse value) {
    return new JAXBElement<>(_ObtenerInformacionFirmaResponse_QNAME,
        ObtenerInformacionFirmaResponse.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link PostProcesarFirmaResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/",
      name = "postProcesarFirmaResponse")
  public JAXBElement<PostProcesarFirmaResponse> createPostProcesarFirmaResponse(
      PostProcesarFirmaResponse value) {
    return new JAXBElement<>(_PostProcesarFirmaResponse_QNAME, PostProcesarFirmaResponse.class,
        null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link ValidarCertificadoResponse
   * }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/",
      name = "validarCertificadoResponse")
  public JAXBElement<ValidarCertificadoResponse> createValidarCertificadoResponse(
      ValidarCertificadoResponse value) {
    return new JAXBElement<>(_ValidarCertificadoResponse_QNAME, ValidarCertificadoResponse.class,
        null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link AmpliarFirmaResponse }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/",
      name = "ampliarFirmaResponse")
  public JAXBElement<AmpliarFirmaResponse> createAmpliarFirmaResponse(AmpliarFirmaResponse value) {
    return new JAXBElement<>(_AmpliarFirmaResponse_QNAME, AmpliarFirmaResponse.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link ValidacionFirma }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "validacionFirma")
  public JAXBElement<ValidacionFirma> createValidacionFirma(ValidacionFirma value) {
    return new JAXBElement<>(_ValidacionFirma_QNAME, ValidacionFirma.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link ObtenerFirmantes }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "http://service.ws.inside.dsic.mpt.es/", name = "obtenerFirmantes")
  public JAXBElement<ObtenerFirmantes> createObtenerFirmantes(ObtenerFirmantes value) {
    return new JAXBElement<>(_ObtenerFirmantes_QNAME, ObtenerFirmantes.class, null, value);
  }

}
