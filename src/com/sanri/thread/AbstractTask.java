package com.sanri.thread;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import sanri.utils.Validate;
/**
 * 
 * @author sanri
 *
 * @param <IN> 输入任务类型
 * @param <OUT> 输入结果类型
 * 
 * 任务超类,这里面定义了任务切割的具体操作,具体的任务由子类实现 
 */
public abstract class AbstractTask<IN,OUT> extends Thread {
	protected static int SINGLE_THREAD_RECORD = 100;          //单个线程每次处理100 条数据
    protected static int MAX_THREAD_COUNT = 100;              //最多启用 100 个线程,设 0 表示无限制
    protected static int CONCURRENT_THREAD = 20;              //系统最多同时运行的线程数,设 0 表示同时运行所有线程(注:如果超过了最大线程数,那么此设置相当于无用,但不影响程序,相当于同时运行所有线程)
    
    protected int threadNum;                                 //计算出的线程数
    protected Queue<List<AbstractTaskThread<IN,OUT>>> taskQueue;       //线程队列
    protected List<IN> taskDataList;                         //任务数据,由子类提供
    
    protected static ExecutorService executorService;
    static{
        if(executorService == null){
            executorService = Executors.newCachedThreadPool();
        }
    }
    /**
     * 
     * 功能:执行任务<br/>
     * 创建时间:2016-9-25下午3:25:14<br/>
     * 作者：sanri<br/>
     */
    public void execute(){
        if(!Validate.isEmpty(taskDataList)){
            calcThreadNum();
            if(threadNum == 0)return ;
            //分配任务
            List<List<IN>> taskDataSplitList = new ArrayList<List<IN>>();		//外层 List 为子线程列表,里层List为每个子线程的任务列表
            for (int i=0; i<threadNum;i++) {
                List<IN> singleThreadTaskList = new ArrayList<IN>();
                taskDataSplitList.add(singleThreadTaskList);
                
                if(i == threadNum -1){ //最后一个子线程
                    //最后当数量不足单个线程处理的任务数时单启一个线程处理剩下所有任务
                    for(int k=i * SINGLE_THREAD_RECORD;k<taskDataList.size();k++){
                        singleThreadTaskList.add(taskDataList.get(k));
                    }
                }else{
                    for(int j=i * SINGLE_THREAD_RECORD;j<(i+1)*SINGLE_THREAD_RECORD;j++){
                        singleThreadTaskList.add(taskDataList.get(j));
                    }
                }
            }
            //分配线程
            if(!Validate.isEmpty(taskDataSplitList)){
                taskQueue = new ArrayDeque<List<AbstractTaskThread<IN,OUT>>>();	//里层的List为每次弹出的元素,设置为 20 个;然后外层的为弹出队列
                List<AbstractTaskThread<IN,OUT>> queueElemet = null;
                if(CONCURRENT_THREAD == 0){								//这个参数为 0 时表示同时运行所有线程,只需要一个 queueElement 
                    queueElemet = new ArrayList<AbstractTaskThread<IN,OUT>>();
                    taskQueue.offer(queueElemet);
                }
                //队列中加入线程列表
                for (int i = 0;i<taskDataSplitList.size();i++) {
                    List<IN> taskDataList = taskDataSplitList.get(i);
                    
                    AbstractTaskThread<IN,OUT> taskThread = newInstance();		//单个任务实例
                    taskThread.setTaskDataList(taskDataList);
                    
                    if(i % CONCURRENT_THREAD == 0 && CONCURRENT_THREAD != 0){
                        queueElemet = new ArrayList<AbstractTaskThread<IN,OUT>>();
                        taskQueue.offer(queueElemet);
                    }
                    queueElemet.add(taskThread);
                }
            }
            
            //处理任务
            while(!taskQueue.isEmpty()){
                List<AbstractTaskThread<IN, OUT>> singleThreadList = taskQueue.poll();
                if(!Validate.isEmpty(singleThreadList)){
                    List<Future<OUT>> resultList = new ArrayList<Future<OUT>>();
                    for (AbstractTaskThread<IN, OUT> iTaskThread : singleThreadList) {
                        Future<OUT> executorResult = executorService.submit(iTaskThread);
                        resultList.add(executorResult);
                    }
                    
//                    for (Future<OUT> future : resultList) {
//                        try {
//                            //以单个任务列表出错为单元,一组数据出错了,这组数据就不做了,接着下一组数据
//                            while (!future.isDone()) {		//直到 future 做完了,才获取结果
//                                OUT out = future.get();
//                                afterHandle(out);
//                            }
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    //输出结果
        			Map<Future<OUT>,Boolean> doneList = new HashMap<Future<OUT>, Boolean>();
        			while(doneList.size() != resultList.size()){	//一遍一遍的去看所有任务,当有任务完成了就标记完成,直到所有任务完成后才退出
        				for (Future<OUT> future : resultList) {
        					try {
	        					if (future.isDone() && doneList.get(future) == null) {
	        						OUT out = future.get(); // 这个会一直阻塞,直到有结果返回
	        						doneList.put(future, true);
	        						afterHandle(out);
	        					}
        					} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (ExecutionException e) {
								e.printStackTrace();
							}
        				}
        			}
                }
            }
            if(executorService != null){
                executorService.shutdown();
            }
        }
    }
    
    /**
     * 
     * 功能:计算线程数目<br/>
     * 规则 :<br/>
     * <ul>
     * 	<li>先根据记录数计算线程数</li>
     * 	<li>如果最大线程数不做限制,则为计算出来的线程数</li>
     * 	<li>如果最大线程数有限制,并且计算出的线程数大于最大线程数,则取最大线程数</li>
     * 	<li>如果有最大线程数限制,则计算每个线程要处理的任务,否则根据设置的任务数来</li>
     * </ul>
     * 创建时间:2016-9-25下午3:24:34<br/>
     * 作者：sanri<br/>
     */
    public void calcThreadNum(){
        long recordNum = taskDataList==null ? 0 : taskDataList.size();
        this.threadNum = (int) (recordNum % SINGLE_THREAD_RECORD == 0 ? recordNum/SINGLE_THREAD_RECORD :recordNum/SINGLE_THREAD_RECORD + 1);
        if(threadNum > MAX_THREAD_COUNT && MAX_THREAD_COUNT != 0){
            threadNum = MAX_THREAD_COUNT;
            SINGLE_THREAD_RECORD =  (int) Math.ceil((double)recordNum / MAX_THREAD_COUNT);
        }
    }
    
    protected abstract AbstractTaskThread<IN,OUT> newInstance();
    public boolean beforeHandle(){return true;};
    public void afterHandle(OUT out){};
    public abstract List<IN> getTaskDataList();
    
    @Override
    public void run() {
        boolean beforeHandle = this.beforeHandle();
        if(beforeHandle){
            this.taskDataList = getTaskDataList();
            this.execute();
        }
    }
}
