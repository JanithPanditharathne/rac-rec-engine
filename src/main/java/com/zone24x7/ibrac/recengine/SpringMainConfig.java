package com.zone24x7.ibrac.recengine;

import com.zone24x7.ibrac.recengine.dao.DatasourceAdapter;
import com.zone24x7.ibrac.recengine.dao.HBaseAdapterImpl;
import com.zone24x7.ibrac.recengine.service.AlgorithmTaskFactory;
import com.zone24x7.ibrac.recengine.strategy.FlatRecStrategy;
import com.zone24x7.ibrac.recengine.strategy.PlacementTaskFactory;
import com.zone24x7.ibrac.recengine.strategy.StrategyExecutor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class SpringMainConfig {

    @Bean
    @Qualifier("cachedThreadPoolTaskExecutor")
    public ExecutorService getTaskExecutor() {
         return Executors.newCachedThreadPool();
    }

    @Bean
    @Qualifier("strategyExecutors")
    public StrategyExecutor provideStrategyExecutors(FlatRecStrategy flatRecStrategy){
        flatRecStrategy.setNextExecutor(null);
        return flatRecStrategy;
    }

    @Bean
    public PlacementTaskFactory placementTaskFactory() {
       return new PlacementTaskFactory();
    }

    @Bean
    public AlgorithmTaskFactory algorithmTaskFactory() {
        return new AlgorithmTaskFactory();
    }
    @Bean
    public DatasourceAdapter getDataSource(){
        //TODO: if else according to data store
        return new HBaseAdapterImpl();
    }

}
