package es.mpt.dsic.eeutil.misc.web.controller;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.mpt.dsic.inside.security.util.ConstantsClave;
import eu.eidas.auth.engine.ProtocolEngineFactoryNoMetadata;
import eu.eidas.auth.engine.ProtocolEngineNoMetadataI;
import eu.eidas.auth.engine.configuration.dom.ProtocolEngineConfigurationFactoryNoMetadata;

public class SPUtil {

  /** Sistema de traceo. */
  static Log logger = LogFactory.getLog(SPUtil.class);

  static ProtocolEngineConfigurationFactoryNoMetadata protocolEngineConfigurationFactory = null;
  static ProtocolEngineFactoryNoMetadata defaultProtocolEngineFactory = null;

  public static Properties loadConfigs(String path) throws IOException {
    Properties properties = new Properties();
    properties.load(SPUtil.class.getClassLoader().getResourceAsStream(path));
    return properties;
  }

  public static synchronized ProtocolEngineNoMetadataI getProtocolEngine() {
    if (defaultProtocolEngineFactory == null) {
      try {
        String configPath = StringUtils.isEmpty(System.getProperty(ConstantsClave.CONFIG_PATH_VAR))
            ? System.getenv(ConstantsClave.CONFIG_PATH_VAR)
            : System.getProperty(ConstantsClave.CONFIG_PATH_VAR);
        protocolEngineConfigurationFactory = new ProtocolEngineConfigurationFactoryNoMetadata(
            "SPSamlEngine.xml", null, configPath + "/");
        defaultProtocolEngineFactory =
            new ProtocolEngineFactoryNoMetadata(protocolEngineConfigurationFactory);
      } catch (Exception e) {
        logger.error("Error creating protocol engine factory : ", e);
      }
    }
    return defaultProtocolEngineFactory != null
        ? defaultProtocolEngineFactory.getProtocolEngine(ConstantsClave.SP_CONF)
        : null;
  }
}
