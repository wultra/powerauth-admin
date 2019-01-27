# Deploying PowerAuth Admin

This chapter explains how to deploy PowerAuth Admin.

PowerAuth Admin is a Java EE application (packaged as an executable WAR file) that you can use to work with the PowerAuth Server services in a easy to use visual way. Also, PowerAuth Admin project may serve as a simple example application for the Internet banking integrators, since in essence, it performs the very same tasks.

*__Important note: Since PowerAuth Admin is a very simple application with direct access to the PowerAuth Server SOAP services, it must not be under any circumstances published publicly and must be constrained to the in-house closed infrastructure.__*

## Downloading PowerAuth Admin

You can download the latest `powerauth-admin.war` at the [PowerAuth Admin releases page](https://github.com/wultra/powerauth-admin/releases).

## Configuring PowerAuth Admin

The default implementation of a PowerAuth Admin has only one compulsory configuration parameter `powerauth.service.url` that configures the SOAP endpoint location of a PowerAuth Server. The default value for this property points to `localhost`:

```bash
powerauth.service.url=http://localhost:8080/powerauth-java-server/soap
```

## Setting Up SOAP Service Credentials

_(optional)_ In case PowerAuth Server uses a [restricted access flag in the server configuration](https://github.com/wultra/powerauth-server/blob/master/docs/Deploying-PowerAuth-Server.md#enabling-powerauth-server-security), you need to configure credentials for the PowerAuth Admin so that it can connect to the SOAP service:

```sh
powerauth.service.security.clientToken=
powerauth.service.security.clientSecret=
```

The credentials are stored in the `pa_integration` table.

_Note: For SOAP interface, PowerAuth Server uses WS-Security, `UsernameToken` validation (plain text password). The RESTful interface is secured using Basic HTTP Authentication (pre-emptive)._

## Disabling SSL Validation During Development

_(optional)_ While this is **strongly discouraged in production environment** (we cannot emphasize this enough), some development environments may use self-signed certificate for HTTPS communication. In case PowerAuth SOAP service uses HTTPS with such certificate, and in case you are not able to correctly configure a custom keystore in your server container, you may disable SSL certificate validation by setting this property:

```bash
powerauth.service.ssl.acceptInvalidSslCertificate=true
```

## Configuring Admin User Authentication

_(recommended)_ PowerAuth Admin supports optional authentication using the LDAP protocol. This option is disabled by default, but we recommend setting up LDAP based authentication at least for the production environment. Read more about how to setup LDAP Authentication in a separate chapter.

- [Setting Up LDAP Authentication](./Setting-Up-LDAP-Authentication.md)

## Deploying PowerAuth Admin

You can deploy PowerAuth Admin into any Java EE container.

The default configuration works best with Apache Tomcat server running on default port 8080. In this case, the deployed server is accessible on `http://localhost:8080/powerauth-admin/`.

To deploy PowerAuth Admin to Apache Tomcat, simply copy the WAR file in your `webapps` folder or deploy it using the "Tomcat Web Application Manager" application (usually deployed on default Tomcat address `http://localhost:8080/manager`).

*__Important note: Since PowerAuth Admin is a very simple application with direct access to the PowerAuth Server SOAP services, it must not be under any circumstances published publicly and must be constrained to the in-house closed infrastructure.__*

## Deploying PowerAuth Admin outside the container

You can also execute WAR file directly using the following command:

```bash
java -jar powerauth-admin.war
```

_Note: You can overwrite the port using `-Dserver.port=8090` parameter to avoid port conflicts._

*__Important note: Since PowerAuth Admin is a very simple application with direct access to the PowerAuth Server SOAP services, it must not be under any circumstances published publicly and must be constrained to the in-house closed infrastructure.__*
