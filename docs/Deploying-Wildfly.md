# Deploying PowerAuth Admin on JBoss / Wildfly

## JBoss Deployment Descriptor 

PowerAuth admin contains the following configuration in `jboss-deployment-structure.xml` file for JBoss:

```
<?xml version="1.0"?>
<jboss-deployment-structure xmlns="urn:jboss:deployment-structure:1.2">
	<deployment>
		<exclude-subsystems>
			<!-- disable the logging subsystem because the application manages its own logging independently -->
			<subsystem name="logging" />
		</exclude-subsystems>

		<dependencies>
			<module name="com.wultra.powerauth.admin.conf" />
		</dependencies>
		<local-last value="true" />
	</deployment>
</jboss-deployment-structure>
```

## JBoss Module for PowerAuth Admin Configuration

You need to configure the `com.wultra.powerauth.admin.conf` module in `modules/system/layers/base/com/wultra/powerauth/admin/conf/main`.

### Main Module Configuration

The `module.xml` configuration adds resources from module to classpath:
```
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.3" name="com.wultra.powerauth.admin.conf">
    <resources>
        <resource-root path="." />
    </resources>
</module>
```

### Logging Configuration

The `logback.xml` file configures logging, for example:
```
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="30 seconds">

        <property name="LOG_FILE_DIR" value="/var/log/powerauth" />
        <property name="LOG_FILE_NAME" value="powerauth-admin" />
        <property name="INSTANCE_ID" value="${jboss.server.name}" />

        <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
                <file>${LOG_FILE_DIR}/${LOG_FILE_NAME}-${INSTANCE_ID}.log</file>
                <immediateFlush>true</immediateFlush>
                <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
                        <fileNamePattern>${LOG_FILE_DIR}/${LOG_FILE_NAME}-${INSTANCE_ID}-%d{yyyy-MM-dd}-%i.log</fileNamePattern>
                        <maxFileSize>10MB</maxFileSize>
                        <maxHistory>5</maxHistory>
                        <totalSizeCap>100MB</totalSizeCap>
                </rollingPolicy>
                <encoder>
                        <charset>UTF-8</charset>
                        <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
                </encoder>
        </appender>

        <logger name="com.wultra" level="INFO" />
        <logger name="io.getlime" level="INFO" />

        <root level="INFO">
                <appender-ref ref="FILE" />
        </root>
</configuration>
```

### Application Configuration

The `application-ext.properties` file is used to override default configuration properties, for example:
```
# PowerAuth 2.0 Client configuration
powerauth.service.url=http://[host]:[port]/powerauth-java-server/soap

# Application Service Configuration
powerauth.admin.service.applicationEnvironment=TEST
```

## Enabling External Application Configuration 

In order to enabled external configuration for the `application.properties` configuration file, set the following environment property before starting JBoss:

```
SPRING_CONFIG_LOCATION=classpath:/application.properties,classpath:/application-ext.properties
```

The properties configured in the `application-ext.properties` file take precedence over default configuration. Changes in this file are applied after application restart.
