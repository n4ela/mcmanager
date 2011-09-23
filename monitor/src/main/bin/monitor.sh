#!/bin/bash
ABSOLUTE_FILENAME=`readlink -e "$0"`                                                                                                                           
CATALINA_HOME=`dirname "$ABSOLUTE_FILENAME"`/..
java -cp "$CATALINA_HOME/lib/*" -Dcatalina.home=$CATALINA_HOME mcmanager.monitor.StartupMonitor