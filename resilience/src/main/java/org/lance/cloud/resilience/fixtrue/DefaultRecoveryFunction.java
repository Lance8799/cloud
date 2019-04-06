package org.lance.cloud.resilience.fixtrue;


import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;

public class DefaultRecoveryFunction implements RecoveryFunction<HttpResult> {

    @Override
    public HttpResult apply(Throwable throwable) {
        return HttpResultBuilder.fail("Default fallback");
    }
}
