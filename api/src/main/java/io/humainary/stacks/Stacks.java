/*
 * Copyright © 2022 JINSPIRED B.V.
 */

package io.humainary.stacks;

import io.humainary.spi.Providers;
import io.humainary.stacks.spi.StacksProvider;
import io.humainary.substrates.Substrates;

import static io.humainary.substrates.Substrates.*;

/**
 * An open and extensible interface that offers a highly efficient implementation of contextual stack capturing and collection.
 */

public final class Stacks {

  private static final StacksProvider PROVIDER =
    Providers.create (
      "io.humainary.stacks.spi.factory",
      "io.stakks.stacks.spi.alpha.ProviderFactory",
      StacksProvider.class
    );

  private static final Name STACK = name ( "io.humainary.stacks.stack" );

  private Stacks () {
  }


  /**
   * Returns the default {@link Context}.
   *
   * @return The default {@link Context}
   * @see StacksProvider#context()
   */

  public static Context context () {

    return
      PROVIDER.context ();

  }


  /**
   * Returns a {@link Context} mapped based on one or more properties within {@link Environment} provided.
   *
   * @param environment the configuration used in the mapping and construction of a {@link Context}
   * @return A {@link Context} constructed from and mapped to the {@link Environment}
   */

  public static Context context (
    final Environment environment
  ) {

    return
      PROVIDER.context (
        environment
      );

  }


  /**
   * A context represents some configured boundary within a process space.
   *
   * @see Stacks#context()
   * @see Stacks#context(Environment)
   */

  public interface Context
    extends Substrates.Context< Site, Stack > {

    /**
     * A proposed environment property name for setting and getting
     * the collector strategy employed by the underlying runtime.
     */

    Name COLLECTOR = STACK.name ( "collector" );


    /**
     * A proposed environment property name for setting and getting
     * the maximum stack depth collected by the underlying runtime.
     */

    Name LIMIT = STACK.name ( "depth" ).name ( "limit" );


    /**
     * Returns a (call)site instrument associated with a provided name within the scope of this context.
     *
     * @param name the name used by all change events published by the returned site
     * @return A site, specific to this context, that can be used for collecting stacks at call sites
     */

    Site site (
      final Name name
    );

  }


  /**
   * An interface that represents an instrument used for the capture and emittance of contextual stacks at call sites.
   *
   * @see Stack
   * @see Context#site(Name) Context.site(Name)
   */

  public interface Site
    extends Instrument {

    /**
     * The {@link Type} used to identify the interface of this referent type
     */

    Type TYPE = type ( Site.class );


    /**
     * Instructs the site instrument to record the current thread call stack at this point.
     * <p>
     * This will publish a call stack captured from the underlying collector.
     */

    void emit ();


    /**
     * Instructs the site instrument to record the current thread call stack at this point, skipping over a number of frames.
     * <p>
     * This will publish a call stack captured from the underlying collector.
     *
     * @param skip the number of call stack frames to skip before collecting
     */

    void emit (
      int skip
    );


    /**
     * Instructs the site instrument to record an exception including its associated call stack.
     * <p>
     * This will publish a stacked extent with initial extent being of type {@link Stack.Kind#EXCEPTION}
     *
     * @param exception the exception thrown or caught at the (call) site.
     */

    void emit (
      Exception exception
    );

  }

  /**
   * Represents the one of more typed "stacked" extent (nested) instances.
   * <p>
   * Implementation Note: To allow the greatest flexibility in performance (low cost) and collection coverage
   * any type specific attributes are to be exposed via the associated {@link Environment}.
   * <p>
   *
   * @see Extent
   * @see Site#emit()
   * @see Substrate#environment()
   */

  public interface Stack
    extends Substrate,
            Extent< Stack > {

    /**
     * The name to be used for looking up the {@code String} value of the source file name.
     */

    Name FILE = STACK.name ( "frame" ).name ( "source" ).name ( "file" );

    /**
     * The name to be used for looking up the {@code int} value of the source line.
     */

    Name LINE = STACK.name ( "frame" ).name ( "source" ).name ( "lineno" );


    /**
     * The {@link Name} identifying the stack extent element.
     *
     * @return A non-null {@link Name} reference
     */

    Name name ();


    /**
     * Returns the current {@link Kind} of this stack instance extent.
     *
     * @return The current type of this stack instance extent.
     */

    Kind kind ();


    /**
     * Return {@code this} stack extent instance.
     *
     * @return The stack extent instance called.
     */

    @Override
    default Stack extent () {

      return
        this;

    }

    /**
     * An enum representing the different possible stacked extents captured and collected.
     *
     * @see Stack#kind()
     */

    enum Kind {

      /**
       * When of this type the {@link Stack#name()} method will return
       * the {@link Name Name} representation of the threads class.
       * <p>
       */

      THREAD,

      /**
       * When of this type the {@link Stack#name()} method will return
       * the {@link Name Name} representation of the method invocation.
       */

      FRAME,

      /**
       * When of this type the {@link Stack#name()} method will return
       * the {@link Name Name} representation of {@code Exception} class.
       */

      EXCEPTION,

      /**
       * When of this type the {@link Stack#name()} method will return
       * the {@link Name Name} of the {@link Site Site} instrument.
       */

      SITE;


    }

  }


}
