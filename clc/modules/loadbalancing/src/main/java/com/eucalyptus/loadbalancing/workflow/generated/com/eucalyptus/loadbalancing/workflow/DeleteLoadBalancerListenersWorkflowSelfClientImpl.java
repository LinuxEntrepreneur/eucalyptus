/*
 * This code was generated by AWS Flow Framework Annotation Processor.
 * Refer to Amazon Simple Workflow Service documentation at http://aws.amazon.com/documentation/swf 
 *
 * Any changes made directly to this file will be lost when 
 * the code is regenerated.
 */
 package com.eucalyptus.loadbalancing.workflow;

import com.amazonaws.services.simpleworkflow.flow.core.AndPromise;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.core.Task;
import com.amazonaws.services.simpleworkflow.flow.DataConverter;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.WorkflowSelfClientBase;
import com.amazonaws.services.simpleworkflow.flow.generic.ContinueAsNewWorkflowExecutionParameters;
import com.amazonaws.services.simpleworkflow.flow.generic.GenericWorkflowClient;

public class DeleteLoadBalancerListenersWorkflowSelfClientImpl extends WorkflowSelfClientBase implements DeleteLoadBalancerListenersWorkflowSelfClient {

    public DeleteLoadBalancerListenersWorkflowSelfClientImpl() {
        this(null, new com.amazonaws.services.simpleworkflow.flow.JsonDataConverter(), null);
    }

    public DeleteLoadBalancerListenersWorkflowSelfClientImpl(GenericWorkflowClient genericClient) {
        this(genericClient, new com.amazonaws.services.simpleworkflow.flow.JsonDataConverter(), null);
    }

    public DeleteLoadBalancerListenersWorkflowSelfClientImpl(GenericWorkflowClient genericClient, 
            DataConverter dataConverter, StartWorkflowOptions schedulingOptions) {
            
        super(genericClient, dataConverter, schedulingOptions);
    }

    @Override
    public final void deleteLoadBalancerListeners(String accountId, String loadbalancer, Integer[] ports) { 
        deleteLoadBalancerListenersImpl(Promise.asPromise(accountId), Promise.asPromise(loadbalancer), Promise.asPromise(ports), (StartWorkflowOptions)null);
    }

    @Override
    public final void deleteLoadBalancerListeners(String accountId, String loadbalancer, Integer[] ports, Promise<?>... waitFor) { 
        deleteLoadBalancerListenersImpl(Promise.asPromise(accountId), Promise.asPromise(loadbalancer), Promise.asPromise(ports), (StartWorkflowOptions)null, waitFor);
    }
    
    @Override
    public final void deleteLoadBalancerListeners(String accountId, String loadbalancer, Integer[] ports, StartWorkflowOptions optionsOverride, Promise<?>... waitFor) {
        deleteLoadBalancerListenersImpl(Promise.asPromise(accountId), Promise.asPromise(loadbalancer), Promise.asPromise(ports), optionsOverride, waitFor);
    }
    
    @Override
    public final void deleteLoadBalancerListeners(Promise<String> accountId, Promise<String> loadbalancer, Promise<Integer[]> ports) {
        deleteLoadBalancerListenersImpl(accountId, loadbalancer, ports, (StartWorkflowOptions)null);
    }

    @Override
    public final void deleteLoadBalancerListeners(Promise<String> accountId, Promise<String> loadbalancer, Promise<Integer[]> ports, Promise<?>... waitFor) {
        deleteLoadBalancerListenersImpl(accountId, loadbalancer, ports, (StartWorkflowOptions)null, waitFor);
    }

    @Override
    public final void deleteLoadBalancerListeners(Promise<String> accountId, Promise<String> loadbalancer, Promise<Integer[]> ports, StartWorkflowOptions optionsOverride, Promise<?>... waitFor) {
        deleteLoadBalancerListenersImpl(accountId, loadbalancer, ports, optionsOverride, waitFor);
    }
    
    protected void deleteLoadBalancerListenersImpl(final Promise<String> accountId, final Promise<String> loadbalancer, final Promise<Integer[]> ports, final StartWorkflowOptions schedulingOptionsOverride, Promise<?>... waitFor) {
        new Task(new Promise[] { accountId, loadbalancer, ports, new AndPromise(waitFor) }) {
    		@Override
			protected void doExecute() throws Throwable {
                ContinueAsNewWorkflowExecutionParameters _parameters_ = new ContinueAsNewWorkflowExecutionParameters();
                Object[] _input_ = new Object[3];
                _input_[0] = accountId.get();
                _input_[1] = loadbalancer.get();
                _input_[2] = ports.get();
                String _stringInput_ = dataConverter.toData(_input_);
				_parameters_.setInput(_stringInput_);
				_parameters_ = _parameters_.createContinueAsNewParametersFromOptions(schedulingOptions, schedulingOptionsOverride);
                
                if (genericClient == null) {
                    genericClient = decisionContextProvider.getDecisionContext().getWorkflowClient();
                }
                genericClient.continueAsNewOnCompletion(_parameters_);
			}
		};
    }
}