package org.annotation;

import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.ParameterType;
import software.amazon.awssdk.services.ssm.model.PutParameterRequest;

/**
 * Uploads baseline version string as an AWS ParamStore parameter,
 * name specified at construction and description of the parameter is description of the baseline.
 */
public class SSMPostBaseline  implements Callback {
    private final SsmClient client;
    private final String paramName;

    /**
     * @param paramName the name of the parameter to set in AWS ParamStore.
     * @throws RuntimeException if there is an error communicating or authenticating with AWS.
     */
    public SSMPostBaseline(String paramName){
        this.paramName = paramName;
        client = SsmClient.create();
    }

    @Override
    public boolean supports(Event event, Context context) {
        return event.equals(Event.AFTER_BASELINE);
    }

    @Override
    public boolean canHandleInTransaction(Event event, Context context) {
        return true;
    }

    @Override
    public void handle(Event event, Context context) {
        String description = context.getConfiguration().getBaselineDescription();
        String version = context.getConfiguration().getBaselineVersion().getVersion();
        PutParameterRequest request = PutParameterRequest.builder()
                .name(paramName)
                .value(version)
                .description(description)
                .type(ParameterType.SECURE_STRING)
                .build();
        try{
            client.putParameter(request);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getCallbackName() {
        return "SSMPostBaseline";
    }
}
