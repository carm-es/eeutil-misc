package es.mpt.dsic.inside.validacioneni;


import es.mpt.dsic.inside.ws.service.model.Detalle;

public interface EeutilEniValidationENIService {


  // obtener identificador del objeto pasado
  void setIdentificadorExpedienteXML(String xml) throws Exception;

  // validaciones documento
  Detalle validarShemaDocumentoEniFile(String dataXml, String version);

  Detalle validarUnidadOrganicaDocumentoEniFile(String xml);

  Detalle validarFirmaDocumentoEniFile(String idApp, String password, String xml);


  // validaciones expediente
  Detalle validarShemaExpedienteEniFile(String xml, String version);

  Detalle validarUnidadOrganicaExpedienteEniFile(String xml);

  Detalle validarCodigoSIAExpedienteEniFile(String xml);

  Detalle validarFirmaExpedienteEniFile(String idApp, String password, String xml,
      String xmlNoProcedenteInside, byte[] expedienteRecibidoOriginalSinTocar, String version);



}
