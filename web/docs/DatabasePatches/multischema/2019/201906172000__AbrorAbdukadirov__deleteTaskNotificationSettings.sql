delete from "0".companyEmailNotificationSettings where category = 'CATEGORY_PM' and notificationname ='TASK_ADD_NOTIFICATION';
delete from "0".emailNotificationSettings  where category = 'CATEGORY_PM' and notificationtype ='TASK_ADD_NOTIFICATION';

delete from "0".companyEmailNotificationSettings where category = 'CATEGORY_PM' and notificationname ='TASK_DELETE_NOTIFICATION';
delete from "0".emailNotificationSettings  where category = 'CATEGORY_PM' and notificationtype ='TASK_DELETE_NOTIFICATION';

delete from "0".companyEmailNotificationSettings where category = 'CATEGORY_PM' and notificationname ='TASK_UPDATE_NOTIFICATION';
delete from "0".emailNotificationSettings  where category = 'CATEGORY_PM' and notificationtype ='TASK_UPDATE_NOTIFICATION';

delete from "0".companyEmailNotificationSettings where category = 'CATEGORY_PM' and notificationname ='TASK_COMPLETED_NOTIFICATION';
delete from "0".emailNotificationSettings  where category = 'CATEGORY_PM' and notificationtype ='TASK_COMPLETED_NOTIFICATION';


delete from "anv".companyEmailNotificationSettings where category = 'CATEGORY_PM' and notificationname ='TASK_ADD_NOTIFICATION';
delete from "anv".emailNotificationSettings  where category = 'CATEGORY_PM' and notificationtype ='TASK_ADD_NOTIFICATION';

delete from "anv".companyEmailNotificationSettings where category = 'CATEGORY_PM' and notificationname ='TASK_DELETE_NOTIFICATION';
delete from "anv".emailNotificationSettings  where category = 'CATEGORY_PM' and notificationtype ='TASK_DELETE_NOTIFICATION';

delete from "anv".companyEmailNotificationSettings where category = 'CATEGORY_PM' and notificationname ='TASK_UPDATE_NOTIFICATION';
delete from "anv".emailNotificationSettings  where category = 'CATEGORY_PM' and notificationtype ='TASK_UPDATE_NOTIFICATION';

delete from "anv".companyEmailNotificationSettings where category = 'CATEGORY_PM' and notificationname ='TASK_COMPLETED_NOTIFICATION';
delete from "anv".emailNotificationSettings  where category = 'CATEGORY_PM' and notificationtype ='TASK_COMPLETED_NOTIFICATION';
