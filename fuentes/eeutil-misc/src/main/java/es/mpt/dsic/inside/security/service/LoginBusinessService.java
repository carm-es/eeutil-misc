package es.mpt.dsic.inside.security.service;

import java.util.Properties;

import es.mpt.dsic.inside.utils.exception.EeutilException;


public interface LoginBusinessService {

  public byte[] generaTokenClave(String url, Properties properties) throws EeutilException;
}
