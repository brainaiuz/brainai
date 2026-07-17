#!/bin/bash

case $DEPLOYMENT_GROUP_NAME in 

   aws|classic|accounts|prelive|app|Light|KPI-Dev|KPI-Stage|KPI-Prod)
	/bin/systemctl restart tomcat
   ;;

   *)
	/bin/systemctl restart tomcatnew
   ;;

esac
