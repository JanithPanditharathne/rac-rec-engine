package com.zone24x7.ibrac.recengine.pojo.algoparams;

import java.util.List;

/**
 * Class to represent algorithm param information.
 */
public class AlgoParamInfo {
    private List<AlgoParams> algorithmParams;

    /**
     * Method to get the algorithm params list.
     *
     * @return the algorithm params list.
     */
    public List<AlgoParams> getAlgorithmParams() {
        return algorithmParams;
    }

    /**
     * Method to set the algorithm params list.
     *
     * @param algorithmParams the algorithm params list.
     */
    public void setAlgorithmParams(List<AlgoParams> algorithmParams) {
        this.algorithmParams = algorithmParams;
    }
}
