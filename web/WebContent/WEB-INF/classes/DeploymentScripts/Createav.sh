#!/bin/bash

case $DEPLOYMENT_GROUP_NAME in 

   aws|classic|accounts|prelive|app)
	touch /mnt/webapps/projects/app.workforcetrack.com/ROOT/av.html && echo 'available' > /mnt/webapps/projects/app.workforcetrack.com/ROOT/av.html
   ;;

   *)
	touch /mnt/webapps/projects/newui.kpi.com/ROOT/av.html && echo 'available' > /mnt/webapps/projects/newui.kpi.com/ROOT/av.html
   ;;

esac
