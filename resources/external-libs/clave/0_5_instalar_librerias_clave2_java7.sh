

mvn install:install-file -Dfile=./libs/0.5/openws-1.5.5.jar -DgroupId=org.opensaml -DartifactId=openws -Dversion=1.5.5 -Dpackaging=jar
mvn install:install-file -Dfile=./libs/0.5/xmltooling-1.4.5.jar -DgroupId=org.opensaml -DartifactId=xmltooling -Dversion=1.4.5 -Dpackaging=jar
mvn install:install-file -Dfile=./libs/0.5/opensaml-2.6.5-eidas_1.jar -DgroupId=org.opensaml -DartifactId=opensaml -Dversion=2.6.5-eidas_1 -Dpackaging=jar
mvn install:install-file -Dfile=./libs/0.5/eidas-commons-1.4.3-clave-jvm7.jar -DgroupId=eu.eidas -DartifactId=eidas-commons -Dversion=1.4.3-clave -Dpackaging=jar
mvn install:install-file -Dfile=./libs/0.5/eidas-configmodule-1.4.3-clave-jvm7.jar -DgroupId=eu.eidas -DartifactId=eidas-configmodule -Dversion=1.4.3-clave -Dpackaging=jar
mvn install:install-file -Dfile=./libs/0.5/eidas-encryption-1.4.3-clave-jvm7.jar -DgroupId=eu.eidas -DartifactId=eidas-encryption -Dversion=1.4.3-clave -Dpackaging=jar
mvn install:install-file -Dfile=./libs/0.5/eidas-light-commons-1.4.3-clave-jvm7.jar -DgroupId=eu.eidas -DartifactId=eidas-light-commons -Dversion=1.4.3-clave -Dpackaging=jar
mvn install:install-file -Dfile=./libs/0.5/eidas-saml-engine-1.4.3-clave-jvm7.jar -DgroupId=eu.eidas -DartifactId=eidas-saml-engine -Dversion=1.4.3-clave -Dpackaging=jar