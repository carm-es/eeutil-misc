package es.mpt.dsic.inside.security.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;

import es.mpt.dsic.eeutil.misc.web.controller.SPUtil;
import es.mpt.dsic.inside.security.authentication.UserAuthentication;
import es.mpt.dsic.inside.security.model.UsuarioCredencialInfo;
import es.mpt.dsic.inside.security.service.CredentialnfoService;
import es.mpt.dsic.inside.security.util.ConstantsClave;
import eu.eidas.auth.commons.EidasStringUtil;
import eu.eidas.auth.commons.attribute.AttributeDefinition;
import eu.eidas.auth.commons.attribute.AttributeValue;
import eu.eidas.auth.commons.protocol.IAuthenticationResponseNoMetadata;
import eu.eidas.auth.engine.ProtocolEngineNoMetadataI;
import eu.eidas.engine.exceptions.EIDASSAMLEngineException;


@Component
public class CertificateLoginFilter extends AbstractAuthenticationProcessingFilter {

  private static final String ACCESO_NO_AUTORIZADO_MSG = "Acceso no autorizado";
  public static final String CLAVE_PERSONAL_ATTR_SURNAME = "FirstSurname";
  public static final String CLAVE_PERSONAL_ATTR_NAME = "FirstName";
  public static final String CLAVE_PERSONAL_ATTR_ID = "PersonIdentifier";

  protected static final Log log = LogFactory.getLog(CertificateLoginFilter.class);

  public CertificateLoginFilter() {
    super("/accesoCertificado");
  }

  public CertificateLoginFilter(String defaultFilterProcessesUrl) {
    super(defaultFilterProcessesUrl);
  }

  @Autowired
  private Environment env;

  @Autowired
  private CredentialnfoService credentialnfoService;

  @Qualifier("autenticationManager")
  private AuthenticationManager autenticationManager;

  private static final String ACCESO_NO_AUTORIZADO = ACCESO_NO_AUTORIZADO_MSG;



  private UsuarioCredencialInfo extractUserObjectFromClaveAttr(
      ImmutableMap<AttributeDefinition<?>, ImmutableSet<? extends AttributeValue<?>>> attrList) {

    HashMap<String, String> mapAtributos = new HashMap<>();
    UnmodifiableIterator<AttributeDefinition<?>> iterator = attrList.keySet().iterator();

    while (iterator.hasNext()) {
      AttributeDefinition<?> it = iterator.next();
      if (attrList.get(it) != null) {
        mapAtributos.put(it.getFriendlyName(), attrList.get(it).toString());
      }
    }

    String nif = null;

    try {
      nif = mapAtributos.get(env.getProperty(ConstantsClave.PROPERTY_IDENTIFICADOR));
      nif = nif.replaceAll("\\[", "").replaceAll("\\]", "");
      String[] nifParts = nif.split("/");
      // preparado para ciudadano europeo.
      nif = nifParts[nifParts.length - 1];
      return credentialnfoService.getCredentialInfo(nif);

    } catch (Exception e) {
      log.error(
          String.format("Se ha producido un error buscando el nif '%s': %s", nif, e.getMessage()),
          e);
      throw new BadCredentialsException(e.getMessage(), e);
    }
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) {

    log.info("*** login by cl@ve ***");

    /* Recuperamos los atributos */
    ImmutableMap<AttributeDefinition<?>, ImmutableSet<? extends AttributeValue<?>>> attrList =
        extractClaveAttrFromRequest(request);

    UsuarioCredencialInfo userObject = extractUserObjectFromClaveAttr(attrList);

    if (userObject != null) {

      List<GrantedAuthority> roles = new ArrayList<>();
      setUserRoles(userObject, roles);

      return new UserAuthentication(userObject.getNif(), null, roles, "1", userObject.getNif());
    } else {
      logger.error(ACCESO_NO_AUTORIZADO_MSG);
      throw new PreAuthenticatedCredentialsNotFoundException(ACCESO_NO_AUTORIZADO_MSG);
    }

  }



  private ImmutableMap<AttributeDefinition<?>, ImmutableSet<? extends AttributeValue<?>>> extractClaveAttrFromRequest(
      HttpServletRequest request) {

    IAuthenticationResponseNoMetadata authnResponse = null;

    /* Recuperamos la respuesta de la aplicaci�n */
    String samlResponse = request.getParameter(ConstantsClave.ATRIBUTO_SAML_RESPONSE);
    if (StringUtils.isBlank(samlResponse)) {
      throw new BadCredentialsException("ERROR EN LA CREDENCIALES DE CLAVE2");
    }

    /* Decodificamos la respuesta SAML */
    byte[] decSamlToken = EidasStringUtil.decodeBytesFromBase64(samlResponse);
    String samlResponseXML = new String(decSamlToken);

    /* Obtenemos la instancia de SAMLEngine */
    ProtocolEngineNoMetadataI engine = SPUtil.getProtocolEngine();

    /* Validamos el token SAML */
    try {
      authnResponse = engine.unmarshallResponseAndValidate(decSamlToken,
          (String) request.getRemoteHost(), 0, 0, env.getProperty(ConstantsClave.SP_RETURN));

    } catch (EIDASSAMLEngineException e) {
      log.error(e.getMessage());
      throw new BadCredentialsException("No se ha podido validar el token de la SAML Response", e);
    }

    if (authnResponse.isFailure()) {
      throw new BadCredentialsException("ERROR EN LA CREDENCIALES DE CLAVE2");
    }

    return authnResponse.getAttributes().getAttributeMap();
  }


  private void setUserRoles(UsuarioCredencialInfo userObject, List<GrantedAuthority> roles) {

    if (userObject != null) {
      roles.add(new SimpleGrantedAuthority("USER_ROLE"));

    }

  }



}
