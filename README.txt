mvn deploy:deploy-file -Dfile=lib/bee-encode-0.2.jar -DgroupId=beencode -DartifactId=beencode -Dversion=0.2 -Dpackaging=jar -DrepositoryId=localRepository -Durl=file:///$HOME/.m2/repository

1. install jsoup
    cd jsoup
    mvn clean install
2. install mcmanager
    cd mcmanager

