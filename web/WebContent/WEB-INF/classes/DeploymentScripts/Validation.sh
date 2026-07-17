#!/bin/bash

INSTANCE_ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)

sleep 40

case $DEPLOYMENT_GROUP_NAME in 

   aws|classic|accounts|prelive|app|Light|KPI-Dev|KPI-Stage|KPI-Prod)

	result=$(curl -s http://127.0.0.1:8080/av.html)
	if [[ "$result" =~ "available" ]]; then
	    case $INSTANCE_ID in
          i-0062b158268ac8d2e)
              rm -rf /mnt/webapps/projects/newui.kpi.com/ROOT/av.html
          ;;
      esac
	    exit 0
	else
	    case $INSTANCE_ID in
          i-0062b158268ac8d2e)
              rm -rf /mnt/webapps/projects/newui.kpi.com/ROOT/av.html
          ;;
      esac
	    exit 1
	fi
	;;

   *)

	result=$(curl -s http://127.0.0.1:8081/av.html)
	if [[ "$result" =~ "available" ]]; then
	    exit 0
	else
	    exit 1
	fi

   ;;

esac
