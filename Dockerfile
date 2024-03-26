# gradle:8.5-jdk21 이미지를 기반으로 함
FROM gradle:8.5-jdk21

VOLUME /usr/src/app
WORKDIR /usr/src/app
COPY ./back.sh /usr/src/run.sh

# Spring 소스 코드를 이미지에 복사
COPY . .

# gradle 빌드 시 proxy 설정을 gradle.properties에 추가
RUN ./gradlew build && echo "systemProp.http.proxyHost=krmp-proxy.9rum.cc\nsystemProp.http.proxyPort=3128\nsystemProp.https.proxyHost=krmp-proxy.9rum.cc\nsystemProp.https.proxyPort=3128" > /root/.gradle/gradle.properties

CMD ["bash", "/usr/src/run.sh"]
