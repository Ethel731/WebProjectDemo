# WebProjectDemo
Intellij-IDEA-maven+springMVC+mybatis整合DEMO

## 前言 ##

之前都是在已经建立好的项目基础上开发，没有自己建立过IDEA的maven+springMVC+Mybatis项目，今天刚好学习一下。

maven的本地安装自行度娘~

## 步骤 ##

### 新建项目 ###

首先，打开IDEA后新建一个project：

![](http://i.imgur.com/xdOiiVX.png)

选择maven项目和JDK版本后，勾选Create from archetype创建原型，因为我们这是一个web项目，因此选择maven-archetype-webapp，点击“next”：

![](http://i.imgur.com/7BOAhrS.png)

此时跳出的窗口是对maven仓库的基本配置，groupID中填写本项目仓库路径（默认小写），artifactID为项目名称。Version一般分两种，SNAPSHOT为快照版本，RELEASE为最终发布版本，前面的编号为版本号：

![](http://i.imgur.com/1cGR9uZ.png)

接下来进入maven配置阶段，我的本地安装路径为D:\apache-maven-3.5.0，接下来两个分别是maven配置文件地址和本地仓库地址：

![](http://i.imgur.com/vgiYybR.png)

最后审查一下我们的配置，点击"finish"：

![](http://i.imgur.com/y7WUo6Y.png)

### 添加源码文件夹 ###

IDEA生成的项目目录src下并没有源码文件夹，需要在main下新建一个Java文件夹：

![](http://i.imgur.com/e4VK13y.png)

新建文件夹后，IDEA仍然不能识别源码和resources文件地址，需要我们手动设置一下。

### 设置Source Root和Resource Root ###

右键新建的Java文件夹，mark directory as sources root:

![](http://i.imgur.com/0UfoNa0.png)

对同目录下的resources文件夹也是同样的操作,mark directory as resources root：

![](http://i.imgur.com/a6dh1xN.png)

完成后项目目录是这样的：

![](http://i.imgur.com/FNV8Cwm.png)

### 配置web.xml ###

web.xml为JavaWEB核心配置文件，也是程序的入口，因此我们首要配置它。IDEA自动生成的web.xml并不是规范的，是DOCTYPE规范，而我们WEB-INF下的web.xml是XML文件。这里需要清空后全部覆盖，此处贴上我的一个初始配置：

	<?xml version="1.0" encoding="UTF-8"?>
	<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	 xmlns="http://java.sun.com/xml/ns/javaee" xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
	 xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
	 id="ssm" version="2.5">
	
	  <!-- 初始化spring容器 -->
	  <context-param>
	<param-name>contextConfigLocation</param-name>
	<param-value>classpath:applicationContext.xml</param-value>
	  </context-param>
	  <listener>
	<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	  </listener>
	
	
	  <!-- 解决post乱码 -->
	  <filter>
	<filter-name>CharacterEncodingFilter</filter-name>
	<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
	<init-param>
	  <param-name>encoding</param-name>
	  <param-value>utf-8</param-value>
	</init-param>
	  </filter>
	  <filter-mapping>
	<filter-name>CharacterEncodingFilter</filter-name>
	<url-pattern>/*</url-pattern>
	  </filter-mapping>
	
	  <!-- 解决jsp include html 乱码问题 -->
	  <jsp-config>
	<jsp-property-group>
	  <description>html encoding</description>
	  <display-name>JSPConfiguration</display-name>
	  <url-pattern>*.html</url-pattern>
	  <el-ignored>true</el-ignored>
	  <page-encoding>UTF-8</page-encoding>
	  <scripting-invalid>false</scripting-invalid>
	  <include-prelude></include-prelude>
	  <include-coda></include-coda>
	</jsp-property-group>
	  </jsp-config>
	
	  <!-- springmvc的前端控制器 -->
	  <servlet>
	<servlet-name>springmvc</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	<init-param>
	  <param-name>contextConfigLocation</param-name>
	  <param-value>classpath:springmvc.xml</param-value>
	</init-param>
	<load-on-startup>1</load-on-startup>
	  </servlet>
	  <servlet-mapping>
	<servlet-name>springmvc</servlet-name>
	<url-pattern>/</url-pattern>
	  </servlet-mapping>
	</web-app>

### pom.xml ###

配置完web.xml之后，紧接着就是项目根目录下的pom文件，该文件用于管理源代码、配置文件、开发者的信息和角色、问题追踪系统、组织信息、项目授权、项目的url、项目的依赖关系等等。

配置pom前，首先是技术选型，也就是这个项目所需要用到哪些技术栈。如果不知道自己dependency怎么写，可以直接去[https://mvnrepository.com/](https://mvnrepository.com/)中搜索。eg:搜索mybatis弹出mybatis相关，点击mybatis后进入版本列表，点击其中一个版本，出现其配置方法：

![](http://i.imgur.com/3svhCKu.png)

现在贴入本DEMO的pom.xml（**注意MyBatis Generator的配置，下面会讲到**）：

	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	  <modelVersion>4.0.0</modelVersion>
	  <groupId>com.zyx.freemarkerworddemo</groupId>
	  <artifactId>FreemarkerWordDemo</artifactId>
	  <packaging>war</packaging>
	  <version>1.0-SNAPSHOT</version>
	  <name>FreemarkerWordDemo Maven Webapp</name>
	  <url>http://maven.apache.org</url>
	
	  <!-- 集中定义依赖版本号 -->
	  <properties>
	<spring.version>4.1.3.RELEASE</spring.version>
	<mybatis.version>3.2.8</mybatis.version>
	<mybatis.spring.version>1.2.2</mybatis.spring.version>
	<mybatis-generator.version>1.3.5</mybatis-generator.version>
	<mysql.version>5.1.32</mysql.version>
	<druid.version>1.0.9</druid.version>
	<slf4j.version>1.6.4</slf4j.version>
	<jstl.version>1.2</jstl.version>
	<servlet-api.version>2.5</servlet-api.version>
	<jsp-api.version>2.0</jsp-api.version>
	<commons-lang3.version>3.3.2</commons-lang3.version>
	<commons-io.version>2.5</commons-io.version>
	<commons-net.version>3.3</commons-net.version>
	<freemarker.version>2.3.23</freemarker.version>
	<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	  </properties>
	
	  <dependencies>
	<!-- Apache工具组件 -->
	<dependency>
	  <groupId>org.apache.commons</groupId>
	  <artifactId>commons-lang3</artifactId>
	  <version>${commons-lang3.version}</version>
	</dependency>
	<dependency>
	  <groupId>commons-io</groupId>
	  <artifactId>commons-io</artifactId>
	  <version>${commons-io.version}</version>
	</dependency>
	<dependency>
	  <groupId>commons-net</groupId>
	  <artifactId>commons-net</artifactId>
	  <version>${commons-net.version}</version>
	</dependency>
	<!-- 日志处理 -->
	<dependency>
	  <groupId>org.slf4j</groupId>
	  <artifactId>slf4j-log4j12</artifactId>
	  <version>${slf4j.version}</version>
	</dependency>
	<!-- Mybatis -->
	<dependency>
	  <groupId>org.mybatis</groupId>
	  <artifactId>mybatis</artifactId>
	  <version>${mybatis.version}</version>
	</dependency>
	<dependency>
	  <groupId>org.mybatis</groupId>
	  <artifactId>mybatis-spring</artifactId>
	  <version>${mybatis.spring.version}</version>
	</dependency>
	<!-- MySql -->
	<dependency>
	  <groupId>mysql</groupId>
	  <artifactId>mysql-connector-java</artifactId>
	  <version>${mysql.version}</version>
	</dependency>
	<!-- 连接池 -->
	<dependency>
	  <groupId>com.alibaba</groupId>
	  <artifactId>druid</artifactId>
	  <version>${druid.version}</version>
	</dependency>
	
	<!-- Spring -->
	<dependency>
	  <groupId>org.springframework</groupId>
	  <artifactId>spring-context</artifactId>
	  <version>${spring.version}</version>
	</dependency>
	<dependency>
	  <groupId>org.springframework</groupId>
	  <artifactId>spring-beans</artifactId>
	  <version>${spring.version}</version>
	</dependency>
	<dependency>
	  <groupId>org.springframework</groupId>
	  <artifactId>spring-webmvc</artifactId>
	  <version>${spring.version}</version>
	</dependency>
	<dependency>
	  <groupId>org.springframework</groupId>
	  <artifactId>spring-jdbc</artifactId>
	  <version>${spring.version}</version>
	</dependency>
	<dependency>
	  <groupId>org.springframework</groupId>
	  <artifactId>spring-aspects</artifactId>
	  <version>${spring.version}</version>
	</dependency>
	<dependency>
	  <groupId>org.springframework</groupId>
	  <artifactId>spring-test</artifactId>
	  <version>${spring.version}</version>
	</dependency>
	<!-- freemarker -->
	<dependency>
	  <groupId>org.springframework</groupId>
	  <artifactId>spring-context-support</artifactId>
	  <version>${spring.version}</version>
	</dependency>
	<!-- JSP相关 -->
	<dependency>
	  <groupId>jstl</groupId>
	  <artifactId>jstl</artifactId>
	  <version>${jstl.version}</version>
	</dependency>
	<dependency>
	  <groupId>javax.servlet</groupId>
	  <artifactId>servlet-api</artifactId>
	  <version>${servlet-api.version}</version>
	  <scope>provided</scope>
	</dependency>
	<dependency>
	  <groupId>javax.servlet</groupId>
	  <artifactId>jsp-api</artifactId>
	  <version>${jsp-api.version}</version>
	  <scope>provided</scope>
	</dependency>
	<dependency>
	  <groupId>org.freemarker</groupId>
	  <artifactId>freemarker</artifactId>
	  <version>${freemarker.version}</version>
	</dependency>
	
	<!-- mybatis generator -->
	<dependency>
	  <groupId>org.mybatis.generator</groupId>
	  <artifactId>mybatis-generator-core</artifactId>
	  <version>${mybatis-generator.version}</version>
	</dependency>
	
	<dependency>
	  <groupId>junit</groupId>
	  <artifactId>junit</artifactId>
	  <version>3.8.1</version>
	  <scope>test</scope>
	</dependency>
	  </dependencies>
	  <build>
	<finalName>FreemarkerWordDemo</finalName>
	<pluginManagement>
	  <plugins>
	<!-- 配置Tomcat插件 -->
	<plugin>
	  <groupId>org.apache.tomcat.maven</groupId>
	  <artifactId>tomcat7-maven-plugin</artifactId>
	  <version>2.2</version>
	  <configuration>
	<port>80</port>
	  </configuration>
	</plugin>
	<!-- MyBatis Generator -->
	<plugin>
	  <groupId>org.mybatis.generator</groupId>
	  <artifactId>mybatis-generator-maven-plugin</artifactId>
	  <version>1.3.2</version>
	</plugin>
	  </plugins>
	</pluginManagement>
	
	<!-- 解决配置文件不拷贝的问题 -->
	<resources>
	  <resource>
	<directory>src/main/java</directory>
	<includes>
	  <include>**/*.properties</include>
	  <include>**/*.xml</include>
	</includes>
	<filtering>false</filtering>
	  </resource>
	  <resource>
	<directory>src/main/resources</directory>
	<includes>
	  <include>**/*.properties</include>
	  <include>**/*.xml</include>
	</includes>
	<excludes>
	  <exclude>generatorConfig.xml</exclude>
	</excludes>
	<filtering>false</filtering>
	  </resource>
	</resources>
	  </build>
	</project>

### resources资源目录 ###

一般resources文件夹下存放的都是我们项目集成的配置文件，springMVC、Mybatis等文件配置都放在这里。本DEMO中的配置文件目录如下：

![](http://i.imgur.com/mr0U1nd.png)

首先是spring配置文件*applicationContext.xml*，此处有两个地方需要注意，一个是阿里的druid数据库连接池，我们的jdbc.properties在这里被加载；一个是整合mybatis和spring，**需要在MapperScannerConfigurer中配置DAO路径以便创建MapperFactoryBean**。

*jdbc.properties：*

    jdbc.driver=com.mysql.jdbc.Driver
    jdbc.url=jdbc:mysql://localhost:3306/demofreeword?characterEncoding=utf-8
    jdbc.username=root
    jdbc.password=123456

*applicationContext.xml*：

	    <?xml version="1.0" encoding="UTF-8"?>
	    <beans xmlns="http://www.springframework.org/schema/beans"
	    	xmlns:context="http://www.springframework.org/schema/context" xmlns:p="http://www.springframework.org/schema/p"
	    	xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
	    	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	    	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
	    	http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd
	    	http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.0.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.0.xsd
	    	http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-4.0.xsd">
	    
	    	<!-- 加载配置文件 -->
	    	 <context:property-placeholder location="classpath:jdbc.properties"/>
	    	<!-- 数据库连接池 -->
	    	<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource"
	    		  destroy-method="close">
	    		<property name="url" value="${jdbc.url}" />
	    		<property name="username" value="${jdbc.username}" />
	    		<property name="password" value="${jdbc.password}" />
	    		<property name="driverClassName" value="${jdbc.driver}" />
	    		<!-- 配置初始化大小、最小、最大 -->
	    		<property name="initialSize">
	    			<value>1</value>
	    		</property>
	    		<property name="maxActive">
	    			<value>5</value>
	    		</property>
	    		<property name="minIdle">
	    			<value>1</value>
	    		</property>
	    		<!-- 配置获取连接等待超时的时间 -->
	    		<property name="maxWait">
	    			<value>60000</value>
	    		</property>
	    		<!-- 配置监控统计拦截的filters -->
	    		<property name="filters">
	    			<value>stat</value>
	    		</property>
	    		<!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
	    		<property name="timeBetweenEvictionRunsMillis">
	    			<value>60000</value>
	    		</property>
	    		<!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
	    		<property name="minEvictableIdleTimeMillis">
	    			<value>300000</value>
	    		</property>
	    	</bean>
	    
	    	<!-- 让spring管理sqlsessionfactory 使用mybatis和spring整合包中的 -->
	    	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
	    		<!-- 数据库连接池 -->
	    		<property name="dataSource" ref="dataSource" />
	    		<!-- 加载mybatis的全局配置文件 -->
	    		<property name="configLocation" value="classpath:mybatis-config.xml" />
	    	</bean>
	    	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
	    		<property name="basePackage" value="com.zyx.dao" />
	    	</bean>
	    
	    	<!-- aop -->
	    	<aop:aspectj-autoproxy/>
	    </beans>

其余配置文件大同小异，在此就不一一列举了。详情可下载源码。

### 源码文件夹下的项目目录 ###

配置文件完成后就是建立J2EE的项目目录了，源码文件都在Java文件夹下，具体目录见下图：

![](http://i.imgur.com/KSbZ1gW.png)

到此，项目目录已完成。

### mybatis-genterator插件配置： ###

mybatis-generator是mybatis的一个插件，用于自动生成model及DAO（mapper）层的代码，需要开发者事前在数据库中设置好数据库表及其字段、属性等，以便自动映射JAVA对象。此处只写其配置及使用。

先在pom.xml文件中配置Tomcat插件和MyBatis Generator（上文pom.xml已给出），并定义包含generatorConfig.xml，此处直接贴入generatorConfiguration：

*generatorConfig.xml*：

	<?xml version="1.0" encoding="UTF-8" ?>
	<!DOCTYPE generatorConfiguration PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN" "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd" >
	<generatorConfiguration>
	<classPathEntry
	location="D:\apache-maven-3.5.0\repo\mysql\mysql-connector-java\5.1.32\mysql-connector-java-5.1.32.jar" />
	<context id="context1" targetRuntime="MyBatis3">
	<!-- 序列化pojo -->
	<plugin type="org.mybatis.generator.plugins.SerializablePlugin" />
	<commentGenerator>
	<property name="suppressDate" value="true" />
	<property name="suppressAllComments" value="true" />
	</commentGenerator>
	<jdbcConnection driverClass="com.mysql.jdbc.Driver"
	connectionURL="jdbc:mysql://localhost:3306/demofreeword?characterEncoding=utf-8"
	userId="root" password="123456" />
	<javaModelGenerator targetPackage="com.zyx.model"
	targetProject="src\main\java" />
	<sqlMapGenerator targetPackage="com.zyx.dao"
	targetProject="src\main\java" />
	<javaClientGenerator targetPackage="com.zyx.dao"
	targetProject="src\main\java" type="XMLMAPPER" />
	<table schema="" tableName="resume">
	<!-- 实体类中的成员变量和表的列明一一对应 -->
	   <property name="useActualColumnNames" value="true"/>
	</table>
	</context>
	</generatorConfiguration>


在generatorConfiguration中:

- location为本地MySQL包的地址
- jdbcConnection配置数据库连接
- javaModelGenerator targetPackage配置需要生成model文件的地址
- sqlMapGenerator targetPackage配置需要生成的mapper.xml文件地址
- javaClientGenerator targetPackage配置需要生成的DAO文件地址

配置完成后，点击右上角

![](http://i.imgur.com/3TE3VjK.png),

点击“+”添加一个新的configuration，选择“maven”:

![](http://i.imgur.com/cDOR6vn.png)

在弹出的对话框Name填写插件的名字mybatis-generator（这里是自己定义的，最好写上插件的名字），command line中填写mybatis-generator:generate -e（这里**-e后面不能出现空格**，-e表示在控制台显示错误信息）：

![](http://i.imgur.com/2cbplVC.png)

点击确定后，可看出右上角已显示配置好的mybatis-generator，先点击右侧的maven project弹出maven相关操作按钮，进入run configurations，选择mybatis-generator。双击运行或者右键run：

![](http://i.imgur.com/Lkel38m.png)

出现下图结果后，表示项目建成：

![](http://i.imgur.com/z1e1XEF.png)

**备注：在上述生成文件中，dao目录下接口文件会以Mapper为后缀，可选择手动更改为DAO文件。当rename文件成功后，mapper.xml中namespace将自动更改，无需更多操作。**
