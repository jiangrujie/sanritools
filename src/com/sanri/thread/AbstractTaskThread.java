package com.sanri.thread;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 
 * @author sanri
 *
 * @param <I> 入参类型
 * @param <O> 出参类型
 */
public abstract class AbstractTaskThread<I,O> implements Callable<O> {
    protected List<I> taskDataList;
    
    void setTaskDataList(List<I> taskDataList){
        this.taskDataList = taskDataList;
    }
}
