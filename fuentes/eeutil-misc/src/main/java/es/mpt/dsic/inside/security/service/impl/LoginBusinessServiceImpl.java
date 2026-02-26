package es.mpt.dsic.inside.security.service.impl;


import java.util.Properties;

import org.springframework.stereotype.Service;

import es.mpt.dsic.eeutil.misc.web.controller.SPUtil;
import es.mpt.dsic.inside.security.service.LoginBusinessService;
import es.mpt.dsic.inside.security.util.ConstantsClave;
import es.mpt.dsic.inside.utils.exception.EeutilException;
import es.mpt.dsic.inside.ws.service.model.EstadoInfo;
import eu.eidas.auth.commons.attribute.AttributeDefinition;
import eu.eidas.auth.commons.attribute.ImmutableAttributeMap;
import eu.eidas.auth.commons.attribute.PersonType;
import eu.eidas.auth.commons.attribute.impl.StringAttributeValueMarshaller;
import eu.eidas.auth.commons.protocol.IRequestMessageNoMetadata;
import eu.eidas.auth.commons.protocol.eidas.LevelOfAssurance;
import eu.eidas.auth.commons.protocol.eidas.LevelOfAssuranceComparison;
import eu.eidas.auth.commons.protocol.eidas.impl.EidasAuthenticationRequestNoMetadata;
import eu.eidas.auth.commons.protocol.impl.EidasSamlBinding;
import eu.eidas.auth.commons.protocol.impl.SamlNameIdFormat;
import eu.eidas.auth.engine.ProtocolEngineNoMetadataI;
import eu.eidas.auth.engine.xml.opensaml.SAMLEngineUtils;
import eu.eidas.auth.engine.xml.opensaml.SecureRandomXmlIdGenerator;
import eu.eidas.engine.exceptions.EIDASSAMLEngineException;


@Service("loginBusinessService")
public class LoginBusinessServiceImpl implements LoginBusinessService {

  private static final String HTTP_WWW_W3_ORG_2001_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

  @Override
  public byte[] generaTokenClave(String url, Properties properties) throws EeutilException {


    byte[] token = null;

    ImmutableAttributeMap.Builder reqAttrMapBuilder = new ImmutableAttributeMap.Builder();


    reqAttrMapBuilder.putPrimaryValues(new AttributeDefinition.Builder<String>()
        .nameUri("http://es.minhafp.clave/RelayState").friendlyName("RelayState")
        .personType(PersonType.NATURAL_PERSON).required(false).uniqueIdentifier(true)
        .xmlType("http://eidas.europa.eu/attributes/naturalperson", "PersonIdentifierType",
            "eidas-natural")
        .attributeValueMarshaller(new StringAttributeValueMarshaller()).build(),
        SecureRandomXmlIdGenerator.INSTANCE.generateIdentifier(8));

    // Se comprueba si se quiere deshabilitar alguna de las opciones de acceso de Cl@ve (IDP)
    if (Boolean.valueOf(properties.getProperty(ConstantsClave.EXCLUDE_AFIRMA))) {
      reqAttrMapBuilder.put(new AttributeDefinition.Builder<String>()
          .nameUri("http://es.minhafp.clave/AFirmaIdP").friendlyName("AFirmaIdP")
          .personType(PersonType.NATURAL_PERSON).required(false).uniqueIdentifier(true)
          .xmlType(HTTP_WWW_W3_ORG_2001_XML_SCHEMA, "AFirmaIdPType", "cl")
          .attributeValueMarshaller(new StringAttributeValueMarshaller()).build());
    }

    if (Boolean.valueOf(properties.getProperty(ConstantsClave.EXCLUDE_SS))) {
      reqAttrMapBuilder
          .put(new AttributeDefinition.Builder<String>().nameUri("http://es.minhafp.clave/GISSIdP")
              .friendlyName("GISSIdP").personType(PersonType.NATURAL_PERSON).required(false)
              .uniqueIdentifier(true).xmlType(HTTP_WWW_W3_ORG_2001_XML_SCHEMA, "GISSIdPType", "cl")
              .attributeValueMarshaller(new StringAttributeValueMarshaller()).build());
    }

    if (Boolean.valueOf(properties.getProperty(ConstantsClave.EXCLUDE_AEAT))) {
      reqAttrMapBuilder
          .put(new AttributeDefinition.Builder<String>().nameUri("http://es.minhafp.clave/AEATIdP")
              .friendlyName("AEATIdP").personType(PersonType.NATURAL_PERSON).required(false)
              .uniqueIdentifier(true).xmlType(HTTP_WWW_W3_ORG_2001_XML_SCHEMA, "AEATIdPType", "cl")
              .attributeValueMarshaller(new StringAttributeValueMarshaller()).build());
    }

    if (Boolean.valueOf(properties.getProperty(ConstantsClave.EXCLUDE_UE))) {
      reqAttrMapBuilder
          .put(new AttributeDefinition.Builder<String>().nameUri("http://es.minhafp.clave/EIDASIdP")
              .friendlyName("EIDASIdP").personType(PersonType.NATURAL_PERSON).required(false)
              .uniqueIdentifier(true).xmlType(HTTP_WWW_W3_ORG_2001_XML_SCHEMA, "EIDASIdP", "cl")
              .attributeValueMarshaller(new StringAttributeValueMarshaller()).build());
    }

    EidasAuthenticationRequestNoMetadata.Builder reqBuilder =
        new EidasAuthenticationRequestNoMetadata.Builder();

    reqBuilder.destination(url);
    reqBuilder.providerName(properties.getProperty("provider.name"));
    reqBuilder.requestedAttributes(reqAttrMapBuilder.build());
    reqBuilder.levelOfAssurance(LevelOfAssurance.LOW.stringValue());
    reqBuilder
        .levelOfAssuranceComparison(LevelOfAssuranceComparison.fromString("minimum").stringValue());
    reqBuilder.nameIdFormat(SamlNameIdFormat.UNSPECIFIED.getNameIdFormat());
    reqBuilder.binding(EidasSamlBinding.POST.getName());
    reqBuilder.assertionConsumerServiceURL(properties.getProperty("sp.return"));
    reqBuilder.forceAuth(true);

    reqBuilder.spApplication(properties.getProperty("sp.aplication"));

    IRequestMessageNoMetadata binaryRequestMessage = null;
    EidasAuthenticationRequestNoMetadata authRequest = null;
    try {
      reqBuilder.id(SAMLEngineUtils.generateNCName());
      authRequest = reqBuilder.build();
      ProtocolEngineNoMetadataI engine = SPUtil.getProtocolEngine();
      binaryRequestMessage = engine.generateRequestMessage(authRequest);
    } catch (EIDASSAMLEngineException e) {
      EstadoInfo estadoInfo = new EstadoInfo();
      estadoInfo.setDescripcion("Could not generate token for Saml Request");
      throw new EeutilException("Could not generate token for Saml Request " + e.getMessage(), e);
    }

    token = binaryRequestMessage.getMessageBytes();

    return token;

  }

}
