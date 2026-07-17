#!/bin/bash

case $DEPLOYMENT_GROUP_NAME in 

   aws|classic|accounts|prelive|app)
	   /bin/rm -rf /mnt/webapps/projects/app.workforcetrack.com/ROOT/*
   ;;

   KPI-Prod)
     /bin/rm -rf /mnt/webapps/projects/newui.kpi.com/ROOT/av.html
     sleep 60
	   /bin/rm -rf /mnt/webapps/projects/newui.kpi.com/ROOT/*
   ;;

   *)
	   /bin/rm -rf /mnt/webapps/projects/newui.kpi.com/ROOT/*
   ;;

esac
