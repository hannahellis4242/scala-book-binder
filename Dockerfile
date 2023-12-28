FROM eclipse-temurin
RUN apt update
RUN apt install -y zip
WORKDIR page
COPY target/universal/book-binder-scala-1.0-SNAPSHOT.zip .
RUN unzip book-binder-scala-1.0-SNAPSHOT.zip
CMD book-binder-scala-1.0-SNAPSHOT/bin/book-binder-scala -Dplay.http.secret.key=ad31779d4ee49d5ad5162bf1429c32e2e9933f3b -Dhttp.port=8080