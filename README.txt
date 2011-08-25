mvn deploy:deploy-file \
    -Dfile=bee-encode-0.2.jar \
    -DgroupId=beencode \
    -DartifactId=beencode \
    -Dversion=0.2 \
    -Dpackaging=jar \
    -Durl=file:///$localRepository \
    -DrepositoryId=localRepository