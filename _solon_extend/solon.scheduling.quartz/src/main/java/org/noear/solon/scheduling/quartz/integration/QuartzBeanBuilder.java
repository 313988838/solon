package org.noear.solon.scheduling.quartz.integration;

import org.noear.solon.Utils;
import org.noear.solon.core.BeanBuilder;
import org.noear.solon.core.BeanExtractor;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.scheduling.ScheduledAnno;
import org.noear.solon.scheduling.annotation.Scheduled;
import org.noear.solon.scheduling.quartz.AbstractJob;
import org.noear.solon.scheduling.quartz.JobManager;
import org.noear.solon.scheduling.utils.ScheduledHelper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.lang.reflect.Method;

/**
 * @author noear
 * @since 1.11
 */
public class QuartzBeanBuilder implements BeanBuilder<Scheduled>, BeanExtractor<Scheduled> {
    @Override
    public void doBuild(Class<?> clz, BeanWrap bw, Scheduled anno) throws Throwable {
        if (!(bw.raw() instanceof Job) && !(bw.raw() instanceof Runnable)) {
            throw new IllegalStateException("Quartz job only supports Runnable or Job types!");
        }

        ScheduledAnno warpper = new ScheduledAnno(anno);
        ScheduledHelper.configScheduled(warpper);

        AbstractJob job = new BeanJob(bw.raw());
        String name = Utils.annoAlias(warpper.name(), job.getJobId());


        JobManager.addJob(name, warpper, job);
    }

    @Override
    public void doExtract(BeanWrap bw, Method method, Scheduled anno) throws Throwable {
        if (method.getParameterCount() > 1) {
            throw new IllegalStateException("Scheduling quartz job supports only one JobExecutionContext parameter!");
        }

        if (method.getParameterCount() == 1) {
            Class<?> tmp = method.getParameterTypes()[0];
            if (tmp != JobExecutionContext.class) {
                throw new IllegalStateException("Scheduling quartz supports only one JobExecutionContext parameter!");
            }
        }

        ScheduledAnno warpper = new ScheduledAnno(anno);
        ScheduledHelper.configScheduled(warpper);

        AbstractJob job = new MethodJob(bw.raw(), method);
        String name = Utils.annoAlias(warpper.name(), job.getJobId());

        JobManager.addJob(name, warpper, job);
    }
}