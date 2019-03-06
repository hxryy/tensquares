package com.tensquare.search.job;

/**
 * 定期做数据同步的工作：
 *      把mysql数据库的数据，同步到elasticsearch中去
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
//@Component
public class SynchDataJob {

    private static final String cmd = " e:\n " +
            " cd E:\\logstash-5.6.8\\bin\n " +
            " logstash -f E:\\logstash-5.6.8\\mysqletc\\mysql.conf ";

    /**
     * 执行同步
     */
//    @Scheduled(fixedRate = 100000)
//    @Scheduled(cron = "0 40 11 21 12 ?")
//    @Scheduled(cron = "0 0 3 * * ?")
    public void synchData()throws Exception{
        System.out.println("执行数据同步");
        Runtime.getRuntime().exec("cmd /c start "+cmd);
    }
}
