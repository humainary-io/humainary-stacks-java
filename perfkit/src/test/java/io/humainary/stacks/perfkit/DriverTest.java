/*
 * Copyright © 2022 JINSPIRED B.V.
 */

package io.humainary.stacks.perfkit;

import io.humainary.devkit.perfkit.PerfKit.Target;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.humainary.devkit.perfkit.PerfKit.Driver.ALL;
import static io.humainary.devkit.perfkit.PerfKit.execute;
import static io.humainary.devkit.perfkit.PerfKit.target;

@TestMethodOrder (
  OrderAnnotation.class
)
final class DriverTest {

  private static final Target TARGET =
    target (
      Driver.class,
      "stacks",
      "io.stakks.stacks.spi.alpha.ProviderFactory"
    );

  @Test
  void bench () {

    execute (
      TARGET,
      "default",
      ALL,
      1,
      10000.0,
      Assertions::fail
    );

    execute (
      TARGET,
      "walker",
      ALL,
      1,
      10000.0,
      Assertions::fail
    );

    execute (
      TARGET,
      "throwable",
      ALL,
      1,
      10000.0,
      Assertions::fail
    );

    execute (
      TARGET,
      "thread",
      ALL,
      1,
      10000.0,
      Assertions::fail
    );

    execute (
      TARGET,
      "caller",
      ALL,
      1,
      10000.0,
      Assertions::fail
    );

    execute (
      TARGET,
      "simple",
      ALL,
      1,
      10000.0,
      Assertions::fail
    );

  }


}
