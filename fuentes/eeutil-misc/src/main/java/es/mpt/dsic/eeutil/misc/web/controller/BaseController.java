package es.mpt.dsic.eeutil.misc.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class BaseController {

  protected synchronized String getVersion() {
    String version = null;

    InputStream is = null;
    // try to load from maven properties first
    try {
      Properties p = new Properties();
      is = getClass().getResourceAsStream("/pom.properties");
      if (is != null) {
        p.load(is);
        version = p.getProperty("version", "");
      }
    } catch (Exception e) {
      // ignore
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }

    // fallback to using Java API
    if (version == null) {
      Package aPackage = getClass().getPackage();
      if (aPackage != null) {
        version = aPackage.getImplementationVersion();
        if (version == null) {
          version = aPackage.getSpecificationVersion();
        }
      }
    }

    if (version == null) {
      // we could not compute the version so use a blank
      version = "ERROR";
    }

    return version;
  }

}
