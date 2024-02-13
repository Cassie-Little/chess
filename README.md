# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```


Phase 2 Diagram URL
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYCamYAAzXQlP7ZTC3fYPbY9Tp9FBRNB6BAIDbULY7eRQw4wECDQQoc6US7FYBlSrVOZ1G5Y+5SJ5qBRRACSADl3lZfv8ydNKfNFssWjA2Ul4mDaHCMaFIXSJFE8SgCcI9GBPCTJjyaXtZQyXsL2W9OeKNeSYMAVZ4khAANbofWis0WiG0g6PQKwzb9R2qq22tBo+Ew0JwyLey029AByhB+DIdBgKIAJgADMm8vlzT6I2h2ugZJodPpDEZoLxjjAPhA7G4jF4fH440Fg6xQ3FEqkMiopC40Onuaa+XV2iHusFJf0EFWkGh1VMKbN+etx6pMdrXUcTkSxv3eQvqc619CQnr3l8fgAqIEg40wLyUVQ73EQBAwAC8xoBwlZfBmNTqb9-KkFgDKUYBlddcXxXxlVVWdNQPO5XSePU2Q5Lkrh5SpM3DP17TFbCEOxN0PXRL1sN9SNlxjEconI7Mo26Hp-HjJNU3TOi-VzNB820XQDGMHQUDtSstH0Zha28XxMGYptelbPhPjeJI3jSdIuwkHs8g4yiWygEJlyiSdRJVUZtLQJdPRXaUXQeKIZBQBAThQGC1TM9Z1BgAAeQJwNssDFOUsMKLcAAxKx4gAWTDQjZXdYIZITGAU2TTA8wLPji0GGQK2GGAAHEeUeCT62kxtmBImgoCiaI8qU1StB5LSLWChj9MswzhgKsoJFM5rsws0iUBCXycUqpBAXMFzeqzP0tUQh5jyZchFJ+ILsxgMLIuikbiLHdqYEcPQOGXYabJxZAHC6yRRjmojkKW09viSS9Nqihqyhi104t6dgjkKkDxCDGiYHeyQGKDBLWOTGB8hgAAiUGJDh6r8gRnlmT4ZGYAARkTABmAAWSo4brXwtymZG4bh4mFAQUBrXJnlKep+HQdZHkIip1oYBSLieMLfijGwPQoGwRz4Cg1Qro8SSGwCcrmy6aqEmSerGtMPq-XTNmeWHXS2sG+VJau6acPQSodbKAb4VOw9RsocbJotU3gtunVFteFbfjMjbwqigiTuCHbaJkGQTctlA3aQnoUINI1QYxyodwkC2eXZso8JBnlPqPA34T+soMYBlc89DBO+HBuLIaS1MYdZ9HMaiXHCZgPn0qLYxzAcyd3BgAApCBp3ynljFp+nSvlhlgbiU4O3SUGmpm9B0wSuBn2gVPC74PWuieAyYAAK0HtBw4brDNfNqo6a7qA18nKAo4ebyDOOMBT639yFC8nyzrlZaz1+C9P2WcM4AHUAASho3ggJQBjAC5di623mvbKAjsppuRzvSD2-8nprVwq9aKVEkFEWLk8YOINNwYTKCFEE6CL7mUwW6YIJ4vYfhHgQgOlkyG-yiCoMAsRr7EnLpUEA68oBJyoZHRhuomSnFiHwYQgVSaEkkZUMeIAGaSMzqDGAECoEwLge+BBRDS5VUriEauyVNBpV4h3IwOhgCWEQIqWAwBsBi0IM4VwMsSoJT3rpaqCkvjKVUuoSuit864kcngG6pDiGynlNEsYj8sHMKWkEpSbxLzAggKCAiO17pRAycpbJ15QbSOwcUrJV5cnGgqRYsqUNUrcUwEAA
