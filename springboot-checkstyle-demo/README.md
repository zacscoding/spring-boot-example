## Check style  

> config files  

- config/checkstyle/checkstyle.xml
- config/checkstyle/suppressions.xml  

> plugins  

```aidl
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-checkstyle-plugin</artifactId>
  <version>3.0.0</version>
  <dependencies>
    <dependency>
      <groupId>com.puppycrawl.tools</groupId>
      <artifactId>checkstyle</artifactId>
      <version>8.11</version>
    </dependency>
  </dependencies>

  <executions>
    <execution>
      <id>checkstyle</id>
      <phase>validate</phase>
      <goals>
      <goal>check</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <configLocation>config/checkstyle/checkstyle.xml</configLocation>
      <encoding>UTF-8</encoding>
      <consoleOutput>true</consoleOutput>
      <failsOnError>true</failsOnError>
      </configuration>
</plugin>
```  

> Checks  

```aidl
> ./mvnw.cmd checkstyle:checkstyle
or
> ./mvnw checkstyle:checkstyle
```

