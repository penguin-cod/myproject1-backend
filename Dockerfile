# 使用 OpenJDK 22 作為基底映像
FROM openjdk:22-jdk

# 設定工作目錄
WORKDIR /app

# 複製 JAR 到容器內 /app 目錄並命名為 app.jar
COPY target/myproject1-1.0-SNAPSHOT.jar app.jar

# 容器啟動時執行 jar
ENTRYPOINT ["java", "-jar", "app.jar"]
