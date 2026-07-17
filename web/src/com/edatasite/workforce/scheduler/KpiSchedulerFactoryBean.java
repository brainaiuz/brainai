package com.edatasite.workforce.scheduler;

import org.quartz.Trigger;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sherali on 3/28/2016.
 * Project web
 */
public class KpiSchedulerFactoryBean extends SchedulerFactoryBean {

    @Override
    public void setTriggers(Trigger... triggers) {
        List<Trigger> allTriggers = Arrays.asList(triggers);
        Iterator<Trigger> itr = allTriggers.iterator();
        while (itr.hasNext()) {
            Trigger trigger = itr.next();
//            if (trigger instanceof CronTriggerImpl) {
//                CronTriggerImpl cronTrigger = (CronTriggerImpl) trigger;
//                if ("defaultTriggerJobClass".equals(cronTrigger.getJobName())) {
//                    itr.remove();
//                }
//            }
        }
        super.setTriggers(allTriggers.toArray(new Trigger[0]));

    }
}
