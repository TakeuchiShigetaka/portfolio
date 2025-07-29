#buildステージを開始、Eclipse Temurin 17 JDKを適応したMavenということを設定
FROM maven:3-eclipse-temurin-17 AS build

#ファイルをコピー
COPY . .

#アプリケーションの実行と、テストのスキップ。
RUN mvn clean package -Dmaven.test.skip=true

#新しいビルドステージの開始
FROM eclipse-temurin:17-alpine

#前のビルドステージからビルドされたjarファイル(kakeibo-app.jar)を新しいビルドステージにコピー
COPY --from=build /target/kakeibo-app-0.0.1-SNAPSHOT.jar kakeibo-app.jar

#ポート8080を宣言
EXPOSE 8080

#実行するファイル(kakeibo-app.jar)を指定
ENTRYPOINT ["java", "-jar", "kakeibo-app.jar"]