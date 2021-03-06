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
package org.hawkular.datamining.dist;

import javax.annotation.PostConstruct;
import javax.ejb.ApplicationException;
import javax.inject.Inject;

import org.hawkular.datamining.api.SubscriptionManager;
import org.hawkular.datamining.cdi.qualifiers.Eager;
import org.hawkular.datamining.dist.integration.Configuration;
import org.hawkular.datamining.dist.integration.metrics.JMSMetricDataListener;
import org.hawkular.datamining.dist.integration.metrics.JMSPredictionSender;

/**
 * @author Pavol Loffay
 */
@Eager
@ApplicationException
public class HawkularIntegration {

    @Inject
    private SubscriptionManager subscriptionManager;


    @PostConstruct
    public void postConstruct() {

        JMSMetricDataListener jmsMetricDataListener = new JMSMetricDataListener(subscriptionManager);
        JMSPredictionSender jmsPredictionSender = new JMSPredictionSender(Configuration.TOPIC_METRIC_DATA);

        subscriptionManager.setPredictionListener(jmsPredictionSender);

        Logger.LOGGER.infof("Datamining Hawkular Integration successful");
    }
}
