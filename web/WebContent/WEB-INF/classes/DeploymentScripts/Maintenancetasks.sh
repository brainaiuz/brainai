#!/bin/bash

INSTANCE_ID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)

case $DEPLOYMENT_GROUP_NAME in 

   KPI-Prod)
     case $INSTANCE_ID in
      i-0062b158268ac8d2e)
     	  export SPRING_PROFILES_ACTIVE_NEW=apps
     	  export SPRING_PROFILES_ACTIVE=apps
     	  echo `date +%F:%R` 'Schema Update Beginning GroupName : $DEPLOYMENT_GROUP_NAME' >> /var/log/schemaupdate.log
     	  /bin/bash /mnt/webapps/projects/newui.kpi.com/ROOT/WEB-INF/bashSchemaUpdateApp.sh >> /var/log/schemaupdate.log
     	  ;;
    esac
   ;;

   aws)

	export SPRING_PROFILES_ACTIVE_NEW=aws
	export SPRING_PROFILES_ACTIVE=aws
	echo `date +%F:%R` 'Schema Update Beginning GroupName : $DEPLOYMENT_GROUP_NAME' >> /var/log/schemaupdate.log
	/bin/bash /mnt/webapps/projects/app.workforcetrack.com/ROOT/WEB-INF/bashSchemaUpdateApp.sh >> /var/log/schemaupdate.log

	#Update and restart Solr ENV
	cd /home/ebs/solr && git pull && chown solr: /home/ebs/solr/solrCores/* && systemctl restart solr
   ;;
   
   newui)

	export SPRING_PROFILES_ACTIVE_NEW=dev
	export SPRING_PROFILES_ACTIVE=dev
	echo `date +%F:%R` 'Schema Update Beginning GroupName : $DEPLOYMENT_GROUP_NAME' >> /var/log/schemaupdate.log
	/bin/bash /mnt/webapps/projects/newui.kpi.com/ROOT/WEB-INF/bashSchemaUpdateApp.sh >> /var/log/schemaupdate.log

	#Update and restart Solr ENV
	cd /home/ebs/solr && git pull && chown solr: /home/ebs/solr/solrCores/* && systemctl restart solr
   ;;

   dev)

	export SPRING_PROFILES_ACTIVE_NEW=dev
	export SPRING_PROFILES_ACTIVE=dev
	echo `date +%F:%R` 'Schema Update Beginning GroupName : $DEPLOYMENT_GROUP_NAME' >> /var/log/schemaupdate.log
	/bin/bash /mnt/webapps/projects/newui.kpi.com/ROOT/WEB-INF/bashSchemaUpdateApp.sh >> /var/log/schemaupdate.log

	#Update and restart Solr ENV
	cd /home/ebs/solr && git pull && chown solr: /home/ebs/solr/solrCores/* && systemctl restart solr
   ;;

  KPI-Dev)

 	export SPRING_PROFILES_ACTIVE_NEW=dev
 	export SPRING_PROFILES_ACTIVE=dev
# 	echo `date +%F:%R` 'Schema Update Beginning GroupName : $DEPLOYMENT_GROUP_NAME' >> /var/log/schemaupdate.log
# 	/bin/bash /mnt/webapps/projects/newui.kpi.com/ROOT/WEB-INF/bashSchemaUpdateApp.sh >> /var/log/schemaupdate.log

 	#Update and restart Solr ENV
 	cd /home/ebs/solr && git pull && chown solr: /home/ebs/solr/solrCores86/* && systemctl restart solr
    ;;

  classic)

	export SPRING_PROFILES_ACTIVE_NEW=classic
	export SPRING_PROFILES_ACTIVE=classic
	echo `date +%F:%R` 'Schema Update Beginning GroupName : $DEPLOYMENT_GROUP_NAME' >> /var/log/schemaupdate.log
	/bin/bash /mnt/webapps/projects/app.workforcetrack.com/ROOT/WEB-INF/bashSchemaUpdateApp.sh >> /var/log/schemaupdate.log

	#Update and restart Solr ENV
	cd /home/ebs/solr && git pull && chown solr: /home/ebs/solr/solrCores/* && systemctl restart solr
   ;;

   beta)

	export SPRING_PROFILES_ACTIVE_NEW=betanewui
	export SPRING_PROFILES_ACTIVE=betanewui
	echo `date +%F:%R` 'Schema Update Beginning GroupName : $DEPLOYMENT_GROUP_NAME' >> /var/log/schemaupdate.log
	/bin/bash /mnt/webapps/projects/newui.kpi.com/ROOT/WEB-INF/bashSchemaUpdateApp.sh >> /var/log/schemaupdate.log

	#Update and restart Solr ENV
	cd /home/ebs/solr && git pull && chown solr: /home/ebs/solr/solrCores/* && systemctl restart solr
   ;;

   prelive)

	export SPRING_PROFILES_ACTIVE_NEW=prelive
	export SPRING_PROFILES_ACTIVE=prelive
	echo `date +%F:%R` 'Schema Update Beginning GroupName : $DEPLOYMENT_GROUP_NAME' >> /var/log/schemaupdate.log
	/bin/bash /mnt/webapps/projects/app.workforcetrack.com/ROOT/WEB-INF/bashSchemaUpdateApp.sh >> /var/log/schemaupdate.log

	#Update and restart Solr ENV
	cd /home/ebs/solr && git pull && chown solr: /home/ebs/solr/solrCores/* && systemctl restart solr
   ;;

   KPI-Stage)

	export SPRING_PROFILES_ACTIVE_NEW=staging
	export SPRING_PROFILES_ACTIVE=staging
	echo `date +%F:%R` 'Schema Update Beginning GroupName : $DEPLOYMENT_GROUP_NAME' >> /var/log/schemaupdate.log
	/bin/bash /mnt/webapps/projects/newui.kpi.com/ROOT/WEB-INF/bashSchemaUpdateApp.sh >> /var/log/schemaupdate.log

	cd /home/kpi/solr && git pull && chown solr: /home/kpi/solr/solrCores86/* && docker restart solr
   ;;

   accounts)

	export SPRING_PROFILES_ACTIVE_NEW=accounts
	export SPRING_PROFILES_ACTIVE=accounts
	echo `date +%F:%R` 'Schema Update Beginning GroupName : $DEPLOYMENT_GROUP_NAME' >> /var/log/schemaupdate.log
	/bin/bash /mnt/webapps/projects/app.workforcetrack.com/ROOT/WEB-INF/bashSchemaUpdateApp.sh >> /var/log/schemaupdate.log

	#Update and restart Solr ENV
	cd /home/ebs/solr && git pull && chown solr: /home/ebs/solr/solrCores/* && systemctl restart solr
   ;;

   Light)
     case $INSTANCE_ID in
     i-0f0a80c032ccda533)
	     export SPRING_PROFILES_ACTIVE_NEW=light
	     export SPRING_PROFILES_ACTIVE=light
	     echo `date +%F:%R` 'Schema Update Beginning GroupName : $DEPLOYMENT_GROUP_NAME' >> /var/log/schemaupdate.log
	     /bin/bash /mnt/webapps/projects/newui.kpi.com/ROOT/WEB-INF/bashSchemaUpdateApp.sh >> /var/log/schemaupdate.log

	     #Update and restart Solr ENV
	     cd /home/ebs/solr && git pull && chown solr: /home/ebs/solr/solrCores/* && systemctl restart solr
	     ;;
	   esac
   ;;

   kg)

	export SPRING_PROFILES_ACTIVE_NEW=kg
	export SPRING_PROFILES_ACTIVE=kg
	echo `date +%F:%R` 'Schema Update Beginning GroupName : $DEPLOYMENT_GROUP_NAME' >> /var/log/schemaupdate.log
	/bin/bash /mnt/webapps/projects/newui.kpi.com/ROOT/WEB-INF/bashSchemaUpdateApp.sh >> /var/log/schemaupdate.log

	#Update and restart Solr ENV
	cd /home/ebs/docker_volumes/solr/ && git pull && chown -R 8983:8983 /home/ebs/docker_volumes/solr/ && docker restart solr_solr_1
   ;;

   *)
	echo "For This Profile Do Not Need Automatic Maintenance tasks"
   ;;

esac

