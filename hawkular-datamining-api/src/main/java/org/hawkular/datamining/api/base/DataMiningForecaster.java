/*
 * Copyright 2015-2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hawkular.datamining.api.base;

import java.util.List;

import org.hawkular.datamining.api.PredictionListener;
import org.hawkular.datamining.api.model.Metric;
import org.hawkular.datamining.forecast.AutomaticForecaster;
import org.hawkular.datamining.forecast.DataPoint;

/**
 * Forecaster which on each learning invocation automatically sends predicted points to listener (JMS, HTTP...)
 *
 * @author Pavol Loffay
 */
public class DataMiningForecaster extends AutomaticForecaster {

    private PredictionListener predictionListener;


    /**
     * @param context metric meta data
     */
    public DataMiningForecaster(Metric context) {
        super(context);
    }


    @Override
    public void learn(DataPoint dataPoint) {
        super.learn(dataPoint);

        automaticPrediction();
    }

    @Override
    public void learn(List<DataPoint> data) {
        super.learn(data);

        automaticPrediction();
    }

    @Override
    public Metric context() {
        return (Metric) super.context();
    }

    public void setPredictionListener(PredictionListener predictionListener) {
        this.predictionListener = predictionListener;
    }

    private void automaticPrediction() {
        if (initialized()) {
            final Long forecastingHorizon = context().getForecastingHorizon();

            if (predictionListener != null && forecastingHorizon != null) {
                int nAhead = (int) (forecastingHorizon / context().getCollectionInterval()) + 1;
                List<DataPoint> prediction = forecast(nAhead);

                predictionListener.send(prediction, context().getTenant(), context().getMetricId());
            }
        }
    }
}
