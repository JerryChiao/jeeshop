Jeeshop (Work In Progress)
=======

# Description
Jeeshop is a e-commerce solution allowing you to setup quickly your store using following technologies :
* AngularJS
* Java EE 7
* Java 8

# Links
Project website: http://jeeshop.org

# Components
## REST API
TODO

## Jeeshop-Admin
TODO

# Deployment
Jeeshop components can be deployed to any Java EE 7 compatible server.
## Apache TomEE 2.x
This section describes deployment of Jeeshop components to Apache TomEE 2.x.
TODO

## Wildfly 8 Openshift cartridge
This section describes deployment of Jeeshop components to Openshift PaaS.
TODO

## Wildfly 8
This section describes deployment of Jeeshop components to a Wildfly 8 server.

### Datasources
The following XA datasources are currently used by jeeshop modules and have to be created in server configuration
* JeeshopDS

Sample of configuration for a standalone server with datasources referencing a single jeeshop database:

  ``` xml
  <xa-datasource jndi-name="java:/JeeshopDS" pool-name="JeeshopDS" enabled="true">
      <xa-datasource-property name="ServerName">
          localhost
      </xa-datasource-property>
      <xa-datasource-property name="DatabaseName">
          jeeshop
      </xa-datasource-property>
      <driver>mysql</driver>
      <security>
          <user-name>jeeshop</user-name>
          <password>test</password>
      </security>
      <validation>
          <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLValidConnectionChecker"/>
          <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLExceptionSorter"/>
      </validation>
  </xa-datasource>
  <driver name="mysql" module="com.mysql">
      <driver-class>com.mysql.jdbc.Driver</driver-class>
      <xa-datasource-class>com.mysql.jdbc.jdbc2.optional.MysqlXADataSource</xa-datasource-class>
  </driver>
  ```
  
### Security domain configuration
A security domain named "jeeshop" has to be created to allow BASIC authentication and Role based access to protected REST Resources, using JaaS.

* Sample of configuration for a standalone server:

``` xml
  <security-domain name="jeeshop" cache-type="default">
      <authentication>
          <login-module code="Database" flag="required">
              <module-option name="dsJndiName" value="java:/JeeshopDS"/>
              <module-option name="principalsQuery" value="select password from User where login = ? and (disabled is null or disabled = 0) and activated = 1"/>
              <module-option name="rolesQuery" value="select name,'Roles' from Role r, User_Role ur, User u where u.login=? and u.id = ur.userId and r.id = ur.roleId"/>
              <module-option name="hashAlgorithm" value="SHA-256"/>
              <module-option name="hashEncoding" value="base64"/>
              <module-option name="unauthenticatedIdentity" value="guest"/>
          </login-module>
      </authentication>
  </security-domain>
  ```

#### Configure SSL to secure channels

SSL has to be configured in order to secure credentials sent in requests performed by store customers or Jeeshop-admin users (ie store administrators).
This action is not required under a PaaS such as Openshift. (See Wildfly 8 Openshift cartridge section)

* Create server certificate

Execute the following command in a temp directory

    keytool -genkeypair -alias serverkey -keyalg RSA -keysize 2048 -validity 7360 -keystore server.keystore -keypass <PASSWORD FOR PRIVATE KEY>  -storepass <PASSWORD FOR KEYSTORE> -dname "cn=Server Administrator,o=jeeshop,c=FR"

Copy the server.keystore file in to the ${jboss.home.dir}/standalone/configuration folder

* In standalone.xml configuration file

Add the following security realm block :

``` xml
    <security-realms>
        ...
        <security-realm name="SSLRealm">
            <server-identities>
                <ssl>
                    <keystore path="server.keystore" relative-to="jboss.server.config.dir" keystore-password="THE KEYSTORE PASSWORD"/>
                </ssl>
            </server-identities>
        </security-realm>
    </security-realms>
  ```

Add the following http-listener line to the server block

``` xml
    <server name="default-server">
        ...
       <https-listener name="default-https" socket-binding="https" security-realm="SSLRealm"/>
       ...
     </server>
  ```

### Jeeshop JBOSS Module
A JBOSS Module named "jeeshop" have to be created to <WILDFLY HOME>/modules directory.
It contains multiple configuration properties such as:
* Mailer parameters (SMTP parameters, Sender...)
Sample of this module configuration is available in .openshift directory

## Database setup
Database setup scripts are provided in ./install/db directory

* jeeshop-install.sql contains ddl and jeeshop reference data. It creates also a single user with login/password admin/jeeshop (password is hashed using SHA-256 in this script, which must match security domain configuration, see section above). This user should be deleted in production environment for security reason.
* jeeshop-drop.sql empties database
* demo-catalog-data contains jeeshop demonstration catalog data

Notes:
Current database scripts works with a single database referenced in server datasources configuration. See "Datasources" section above.
However, it is possible to use several database for each Jeeshop domains. For sample one database for Catalog and another for User and Order domains.
TODO add section and scripts to document use of databases per domain
