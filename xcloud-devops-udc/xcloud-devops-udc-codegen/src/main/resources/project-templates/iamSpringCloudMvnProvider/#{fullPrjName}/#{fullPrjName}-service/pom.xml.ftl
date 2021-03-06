<!-- ${watermark} -->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<artifactId>${organName}-${projectName}-service</artifactId>
	<packaging>jar</packaging>
	<name>${organName?cap_first} ${projectName?cap_first} Service</name>
	<url>http://${projectName?lower_case}.${organName?lower_case}.${organType}</url>
	<parent>
		<groupId>${organType}.${organName}</groupId>
		<artifactId>${organName}-${projectName}</artifactId>
		<version>${version}</version>
	</parent>
	<properties>
		<java.version>1.8</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>${organType?uncap_first}.${organName?uncap_first}</groupId>
			<artifactId>${organName?uncap_first}-${projectName?uncap_first}-common</artifactId>
			<version>${version?uncap_first}</version>
		</dependency>
		<dependency>
			<groupId>${organType?uncap_first}.${organName?uncap_first}</groupId>
			<artifactId>${organName?uncap_first}-${projectName?uncap_first}-dao</artifactId>
			<version>${version?uncap_first}</version>
		</dependency>
		<!-- <dependency> -->
		<!-- <groupId>com.wl4g</groupId> -->
		<!-- <artifactId>xcloud-shell-cli</artifactId> -->
		<!-- </dependency> -->
		<!-- <dependency> -->
		<!-- <groupId>com.wl4g</groupId> -->
		<!-- <artifactId>xcloud-shell-springboot</artifactId> -->
		<!-- </dependency> -->
		<dependency>
			<groupId>com.wl4g</groupId>
			<artifactId>xcloud-components-support</artifactId>
		</dependency>
		<#-- refer: Run entry: XxxServer.java -->
		<#if javaSpecs.isConf(extOpts, "iam.mode", "cluster")>
		<#-- Authentication center services and business applications are deployed separately and 
			run in different JVM processes.(e.g: Microservice cluster architecture) -->
		<dependency>
			<groupId>com.wl4g</groupId>
			<artifactId>xcloud-iam-client</artifactId>
		</dependency>
		<#elseif javaSpecs.isConf(extOpts, "iam.mode", "local")>
		<#-- Authentication logic and business application depend on deployment and are coupled in 
			the same JVM process.(e.g: Local monomer application) -->
		<dependency>
			<groupId>com.wl4g</groupId>
			<artifactId>xcloud-iam-core</artifactId>
		</dependency>
		<dependency>
			<groupId>com.wl4g</groupId>
			<artifactId>xcloud-iam-captcha</artifactId>
		</dependency>
		</#if>
	</dependencies>
	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					${r'<source>${java.version}</source>'}
					${r'<target>${java.version}</target>'}
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-source-plugin</artifactId>
				<executions>
					<execution>
						<id>attach-sources</id>
						<phase>verify</phase>
						<goals>
							<goal>jar-no-fork</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-checkstyle-plugin</artifactId>
				<configuration>
					<configLocation>${basedir}/../checkstyle/google_checks.xml</configLocation>
				</configuration>
				<executions>
					<!-- Solutions to maven checkstyle errors in Eclipse: https://gitee.com/wl4g/xcloud-blogs/blob/master/articles/maven.checkstyle-eclipse-error/README_CN.md -->
					<!-- <execution> -->
					<!-- <id>checkstyle</id> -->
					<!-- <phase>validate</phase> -->
					<!-- <goals> -->
					<!-- <goal>check</goal> -->
					<!-- </goals> -->
					<!-- <configuration> -->
					<!-- <failOnViolation>true</failOnViolation> -->
					<!-- </configuration> -->
					<!-- </execution> -->
				</executions>
			</plugin>
		</plugins>
	</build>
</project>