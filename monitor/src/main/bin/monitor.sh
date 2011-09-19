#!/bin/bash
CATALINA_HOME=../`dirname $0`
java -Dcatalina.home=$CATALINA_HOME mcmanager.monitor.StartupMonitor