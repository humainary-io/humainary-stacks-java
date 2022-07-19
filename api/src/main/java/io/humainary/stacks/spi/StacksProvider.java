/*
 * Copyright © 2022 JINSPIRED B.V.
 */

package io.humainary.stacks.spi;

import io.humainary.spi.Providers.Provider;
import io.humainary.stacks.Stacks.Context;
import io.humainary.substrates.Substrates.Environment;

/**
 * The service provider interface for the humainary stacks runtime.
 * <p>
 * Note: An SPI implementation of this interface is free to override
 * the default methods implementation included here.
 */

public interface StacksProvider
  extends Provider {

  Context context ();

  Context context (
    Environment environment
  );

}
