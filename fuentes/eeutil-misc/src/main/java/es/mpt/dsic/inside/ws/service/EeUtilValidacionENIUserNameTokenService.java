package es.mpt.dsic.inside.ws.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import es.mpt.dsic.inside.ws.service.model.RespuestaValidacionENI;
import es.mpt.dsic.inside.ws.service.model.Validaciones;

@WebService
public interface EeUtilValidacionENIUserNameTokenService {

  @WebMethod(operationName = "validarDocumentoENI", action = "urn:validarDocumentoENI")
  @WebResult(name = "Resultado", partName = "Resultado")
  public RespuestaValidacionENI validarDocumentoENI(
      @WebParam(name = "documentoENI")
      @XmlElement(required = true, name = "documentoENI") byte[] documentoENI,
      @WebParam(name = "versionENI")
      @XmlElement(required = false, name = "versionENI") String versionENI,
      @WebParam(name = "validaciones")
      @XmlElement(required = true, name = "validaciones") Validaciones validaciones);

  @WebMethod(operationName = "validarExpedienteENI", action = "urn:validarExpedienteENI")
  @WebResult(name = "Resultado", partName = "Resultado")
  public RespuestaValidacionENI validarExpedienteENI(
      @WebParam(name = "expedienteENI")
      @XmlElement(required = true, name = "expedienteENI") byte[] expedienteENI,
      @WebParam(name = "versionENI")
      @XmlElement(required = false, name = "versionENI") String versionENI,
      @WebParam(name = "validaciones")
      @XmlElement(required = true, name = "validaciones") Validaciones validaciones);

  @WebMethod(operationName = "validarFirmaExpedienteENI", action = "urn:validarFirmaExpedienteENI")
  @WebResult(name = "Resultado", partName = "Resultado")
  public RespuestaValidacionENI validarFirmaExpedienteENI(@WebParam(name = "expedienteENI")
  @XmlElement(required = true, name = "expedienteENI") byte[] expedienteENI);

  @WebMethod(operationName = "validarFirmaDocumentoENI", action = "urn:validarFirmaDocumentoENI")
  @WebResult(name = "Resultado", partName = "Resultado")
  public RespuestaValidacionENI validarFirmaDocumentoENI(@WebParam(name = "documentoENI")
  @XmlElement(required = true, name = "documentoENI") byte[] documentoENI);

  @WebMethod(operationName = "validarExpedienteDocumentosENIZIP",
      action = "urn:validarExpedienteDocumentosENIZIP")
  @WebResult(name = "Resultado", partName = "Resultado")
  public RespuestaValidacionENI validarExpedienteDocumentosENIZIP(
      @WebParam(name = "expedienteENIConDocumentosENI") @XmlElement(required = true,
          name = "expedienteENIConDocumentosENI") byte[] expedienteENIConDocumentosENI,
      @WebParam(name = "versionENI")
      @XmlElement(required = false, name = "versionENI") String versionENI,
      @WebParam(name = "validacionesExpediente") @XmlElement(required = true,
          name = "validacionesExpediente") Validaciones validacionesExpediente,
      @WebParam(name = "validacionesDocumentos") @XmlElement(required = true,
          name = "validacionesDocumentos") Validaciones validacionesDocumentos);

}
