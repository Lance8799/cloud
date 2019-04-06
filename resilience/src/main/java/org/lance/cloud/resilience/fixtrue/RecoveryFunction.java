package org.lance.cloud.resilience.fixtrue;

import java.util.function.Function;

public interface RecoveryFunction<O> extends Function<Throwable, O> {

}
