# buildステージ
FROM maven:3-eclipse-temurin-17 AS build  # buildステージを開始、Eclipse Temurin 17 JDKを適応したMavenということを設定
WORKDIR /app
COPY . .  # ファイルをコピー
RUN mvn clean package -Dmaven.test.skip=true  # アプリケーションのビルド（テストはスキップ）

# 実行ステージ
FROM eclipse-temurin:17-alpine  # 新しいビルドステージの開始
WORKDIR /app
COPY --from=build /app/target/kakeibo-app-0.0.1-SNAPSHOT.jar kakeibo-app.jar  # ビルドされたjarをコピー
EXPOSE 8080  # ポート8080を開放
ENTRYPOINT ["java", "-jar", "kakeibo-app.jar"]  # アプリケーションの実行