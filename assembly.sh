#!/bin/bash
mvn clean install -Dmcmanager.version=0.1 && cd monitor/target && unzip *.zip