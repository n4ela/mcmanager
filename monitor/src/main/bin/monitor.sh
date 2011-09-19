#!/bin/bash
CATALINA_HOME=../`dirname $0`                                                                                                         
java -cp "$CATALINA_HOME/lib/*" -Dcatalina.home=$CATALINA_HOME mcmanager.monitor.StartupMonitor