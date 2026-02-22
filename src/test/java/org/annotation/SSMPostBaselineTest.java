package org.annotation;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.*;

import static org.junit.jupiter.api.Assertions.*;

public class SSMPostBaselineTest {
    @Test
    public void testHandle(){
        SSMPostBaseline callback;
        try {
            callback = new SSMPostBaseline("FlywayBaseline");
        }catch (Exception e){
            fail(e);
            return;
        }
        Flyway flyway = Flyway.configure().
                dataSource("jdbc:h2:file:./target/foobar", "sa", null).
                cleanDisabled(false).
                baselineDescription("TestDescription").
                baselineVersion("0.0.0").
                callbacks(callback).
                load();
        flyway.clean();
        assertDoesNotThrow(flyway::migrate);
        try (SsmClient client = SsmClient.create()) {
            GetParameterRequest request = GetParameterRequest.builder().name("FlywayBaseline").build();
            GetParameterResponse response = client.getParameter(request);
            assertEquals("0.0.0",response.parameter().value());
            ParameterStringFilter filter = ParameterStringFilter.builder()
                    .key("Name")
                    .values("FlywayBaseline")
                    .build();
            DescribeParametersRequest describeRequest = DescribeParametersRequest.builder().parameterFilters(filter).build();
            DescribeParametersResponse describeParametersResponse = client.describeParameters(describeRequest);
            ParameterMetadata param = describeParametersResponse.parameters().getFirst();
            assertEquals("TestDescription", param.description());
        }
    }
}
