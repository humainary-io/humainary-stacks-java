/*
 * Copyright © 2022 JINSPIRED B.V.
 */

package io.humainary.stacks.testkit;

import io.humainary.stacks.Stacks.Site;
import io.humainary.stacks.Stacks.Stack.Kind;
import io.humainary.stacks.Stacks.Stack;
import org.junit.jupiter.api.Test;

import static io.humainary.devkit.testkit.TestKit.capture;
import static io.humainary.devkit.testkit.TestKit.recorder;
import static io.humainary.stacks.Stacks.Context.COLLECTOR;
import static io.humainary.stacks.Stacks.Stack.Kind.*;
import static io.humainary.stacks.Stacks.context;
import static io.humainary.substrates.Substrates.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The test class for the {@link Subscriber} and {@link Subscription} interfaces.
 *
 * @author wlouth
 * @since 1.0
 */

final class SubscriberTest {

  private static final Name S1_NAME = name ( "site#1" );
  private static final Name S2_NAME = name ( "site#2" );

  private static final Name CALLER = name ( SubscriberTest.class ).name ( "emit" );

  @FunctionalInterface
  private interface Check {

    boolean verify (
      final Reference reference,
      final Stack stack
    );

  }

  private static final Check CHECK_CALLER = ( reference, stack ) ->
    CALLER == stack.name ();

  private static final Check CHECK_SITE = ( reference, stack ) ->
    reference.name () == stack.name ();

  @Test
  void baseline () {

    test (
      null,
      CHECK_CALLER,
      FRAME
    );

  }


  @Test
  void walker () {

    test (
      "walker",
      CHECK_CALLER,
      FRAME
    );

  }

  @Test
  void throwable () {

    test (
      "throwable",
      CHECK_CALLER,
      FRAME
    );

  }

  @Test
  void thread () {

    test (
      "thread",
      CHECK_CALLER,
      FRAME
    );

  }

  @Test
  void caller () {

    test (
      "caller",
      CHECK_CALLER,
      FRAME
    );

  }

  @Test
  void simple () {

    test (
      "simple",
      CHECK_SITE,
      SITE
    );

  }

  private void test (
    final String simple,
    final Check checkSite,
    final Kind site
  ) {

    call (
      simple,
      checkSite,
      site
    );

    exception (
      simple
    );

  }

  private void call (
    final String collector,
    final Check check,
    final Kind kind
  ) {

    final var context =
      context (
        environment (
          COLLECTOR,
          collector
        )
      );

    final var s1 =
      context.site (
        S1_NAME
      );


    final var s2 =
      context.site (
        S2_NAME
      );


    final var recorder =
      recorder (
        context,
        Stack::kind
      );

    final var closure =
      new boolean[]{true};

    try (
      final var ignored =
        context.consume (
          event ->
            closure[0] =
              check.verify (
                event.emitter (),
                event.emittance ()
              )
        )
    ) {

      recorder.start ();

      emit (
        s1,
        closure
      );

      emit (
        s2,
        closure
      );

      emit (
        s1,
        closure
      );

    }


    assertEquals (
      capture (
        s1,
        kind
      ).to (
        s2,
        kind
      ).to (
        s1,
        kind
      ),
      recorder
        .stop ()
        .orElseThrow (
          AssertionError::new
        )
    );

  }


  private void emit (
    final Site site,
    final boolean[] closure
  ) {

    site.emit ();

    assertTrue (
      closure[0]
    );

  }


  private void exception (
    final String collector
  ) {

    final var context =
      context (
        environment (
          COLLECTOR,
          collector
        )
      );

    final var s1 =
      context.site (
        S1_NAME
      );


    final var s2 =
      context.site (
        S2_NAME
      );


    final var recorder =
      recorder (
        context,
        Stack::kind
      );

    recorder.start ();

    final var exception =
      new Exception ();

    s1.emit (
      exception
    );

    s2.emit (
      exception
    );

    assertEquals (
      capture (
        s1,
        EXCEPTION
      ).to (
        s2,
        EXCEPTION
      ),
      recorder
        .stop ()
        .orElseThrow (
          AssertionError::new
        )
    );

  }


}
