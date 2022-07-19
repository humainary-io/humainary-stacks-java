
/*
 * Copyright © 2022 JINSPIRED B.V.
 */

package io.humainary.stacks.perfkit;

import io.humainary.devkit.perfkit.PerfKit;
import io.humainary.stacks.Stacks.Site;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;

import static io.humainary.stacks.Stacks.context;
import static io.humainary.substrates.Substrates.*;

@State (
  Scope.Benchmark
)
public class Driver
  implements PerfKit.Driver {

  private Site site;

  @Setup (
    Level.Trial
  )
  public final void setup ()
  throws IOException {

    final var configuration =
      configuration ();

    site =
      context (
        environment (
          lookup (
            name ->
              configuration.apply (
                name.toString ()
              )
          )
        )
      ).site (
        name (
          "site#1"
        )
      );

  }


  @Benchmark
  public void site_called () {

    site.emit ();

  }

}