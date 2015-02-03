package me.imoko.job;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Job Service
 * <p/>
 * Created by sutao on 15/1/20.
 */
@Service
public class JobService {
    private static final Logger LOG = LoggerFactory.getLogger(JobService.class);

    @Autowired
    QueueDao queueDao;
    private List<Worker> workerList;

    private ExecutorService workerThreadPool;


    private static final String ALIPAY_CHECK_QUEUE_NAME = "alipay_transfer";
    private static final String ALIPAY_REFUND_CHECK_QUEUE_NAME = "alipay_refund";


    public void registerProcessor(String qName, int workerCnt, IProcessor processor) {

    }


    @PostConstruct
    private void initWorkers() {
        workerThreadPool = Executors.newCachedThreadPool();
        workerList = Lists.newArrayList();
    }

}
