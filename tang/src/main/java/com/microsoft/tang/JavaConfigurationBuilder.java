package com.microsoft.tang;


import java.util.Set;

import com.microsoft.tang.annotations.Name;
import com.microsoft.tang.exceptions.BindException;
import com.microsoft.tang.exceptions.NameResolutionException;

/**
 * Convenience methods that extend the ConfigurationBuilder but assume that
 * the underlying ClassHierarchy delegates to the default Java classloader. 
 *
 * In addition to being less verbose, this interface expresses many of Tang's
 * type checks in Java's generic type system.  This improves IDE
 * auto-completion.  It also allows the errors to be caught at compile time
 * instead of later on in the build process, or at runtime.
 * 
 * @see ConfigurationModule which pushes additional type checks to class load
 *    time.  This allows Tint, Tang's static analysis tool, to detect a wide
 *    range of runtime configuration errors at build time.   
 */
public interface JavaConfigurationBuilder extends ConfigurationBuilder {

  /**
   * Bind named parameters, implementations or external constructors, depending
   * on the types of the classes passed in.
   * 
   * @param iface
   * @param impl
   */
  public <T> void bind(Class<T> iface, Class<?> impl) throws BindException;

  /**
   * Binds the Class impl as the implementation of the interface iface
   * 
   * @param <T>
   * @param iface
   * @param impl
   */
  public <T> void bindImplementation(Class<T> iface, Class<? extends T> impl)
      throws BindException;

  /**
   * Bind iface to impl, ensuring that all injections of iface return the same
   * instance of impl. Note that directly injecting an impl (and injecting
   * classes not bound "through" iface") will still create additional impl
   * instances.
   * 
   * If you want to ensure that impl is a singleton instead, use the single
   * argument version.
   * 
   * @param <T>
   * @param iface
   * @param impl
   * @throws BindException
   */
  @Deprecated
  public <T> void bindSingletonImplementation(Class<T> iface,
      Class<? extends T> impl) throws BindException;

  /**
   * Same as bindSingletonImplementation, except that the singleton class is
   * bound to itself.
   * 
   * @param <T>
   * @param impl
   * @throws BindException
   */
  @Deprecated
  public <T> void bindSingleton(Class<T> iface) throws BindException;

  /**
   * Set the value of a named parameter.
   * 
   * @param name
   *          The dummy class that serves as the name of this parameter.
   * @param value
   *          A string representing the value of the parameter. Reef must know
   *          how to parse the parameter's type.
   * @throws NameResolutionException
   */
  public void bindNamedParameter(Class<? extends Name<?>> name, String value)
      throws BindException;

  public <T> void bindNamedParameter(Class<? extends Name<T>> iface,
      Class<? extends T> impl) throws BindException;

  public <T> void bindConstructor(Class<T> c,
      Class<? extends ExternalConstructor<? extends T>> v) throws BindException;
  
  public <T> void bindSetEntry(Class<? extends Name<Set<T>>> iface, String value) throws BindException;
  public <T> void bindSetEntry(Class<? extends Name<Set<T>>> iface, Class<? extends T> impl) throws BindException;
}