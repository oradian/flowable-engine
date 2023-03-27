/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.eventregistry.impl;

import java.util.Collection;

import org.flowable.eventregistry.api.*;
import org.flowable.eventregistry.model.InboundChannelModel;

/**
 * @author Joram Barrez
 * @author Filip Hrisafov
 */
public class DefaultInboundEventProcessor implements InboundEventProcessor {

    protected EventRepositoryService eventRepositoryService;
    protected EventRegistry eventRegistry;

    public DefaultInboundEventProcessor(EventRepositoryService eventRepositoryService, EventRegistry eventRegistry) {
        this.eventRepositoryService = eventRepositoryService;
        this.eventRegistry = eventRegistry;
    }

    @Override
    public void eventReceived(InboundChannelModel channelModel, InboundEvent event) {
        InboundEventProcessingPipeline inboundEventProcessingPipeline = (InboundEventProcessingPipeline) channelModel.getInboundEventProcessingPipeline();
        Collection<EventRegistryEvent> eventRegistryEvents = inboundEventProcessingPipeline.run(eventRepositoryService, channelModel, event);

        for (EventRegistryEvent eventRegistryEvent : eventRegistryEvents) {
            eventRegistry.sendEventToConsumers(eventRegistryEvent);
        }

    }

}
