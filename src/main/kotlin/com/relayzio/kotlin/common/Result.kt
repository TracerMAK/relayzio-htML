package com.relayzio.kotlin.common

import java.io.Serializable

/**
 * Represents the value of a successful result or of a failure. The class is
 * covariant on A.
 */
sealed class Result<out A>: Serializable {

  /**
   * Maps the Result value to another using the provided function parameter.
   *
   * @param func  The mapping function.
   * @return      A Result with the new mapped value.
   */
  abstract fun <B> map(func: (A) -> B): Result<B>
  
  /**
   * Maps the Result value to a new Result using the provided function
   * parameter.
   *
   * @param func  The mapping function.
   * @return      A Result with the mapped value.
   */
  abstract fun <B> flatMap(func: (A) -> Result<B>): Result<B>
  
  /**
   * Map a Failure into a different one as a means for altering an error 
   * message or to add additional information into an existing message.
   *
   * @param msg  A String message for the error.
   * @return     A Failure with the passed in message.
   */
  abstract fun mapFailure(msg: String): Result<A>
  
  /**
   * Applies the effect to the wrapped result value. The return type of the
   * function can be omitted in the declaration because it is a Unit type.
   * Default values are given so that the function can be called using any of
   * three subtypes where two of the parameters are ignored.
   *
   * @param onSuccess  Effect function for Success.
   * @param onFailure  Effect function for Failure.
   * @param onEmpty    Effect function for Empty
   */
  abstract fun forEach(onSuccess: (A) -> Unit = {},
                       onFailure: (RuntimeException) -> Unit = {},
                       onEmpty: () -> Unit = {})
  
  /**
   * Singleton object that represents the an empty Result. This is to
   * differentiate from a Failure because the abscence of a result is not
   * necessarily a failure.
   */
  internal object Empty: Result<Nothing>() {
    /**
     * Maps the value of an Empty to a new value. For a empty result, the
     * mapping is always another Empty object.
     *
     * @param func  The mapping function.
     * @return      An Empty object.
     */
    override fun <B> map(func: (Nothing) -> B): Result<B> = Empty
    
    /**
     * Maps the value of an Empty to a new Empty. Like the map function, this
     * will return another Empty.
     *
     * @param func  The mapping function.
     * @return      An Empty object.
     */
    override fun <B> flatMap(func: (Nothing) -> Result<B>): Result<B> = Empty
    
    /**
     * Implements the mapFailure function which just returns itself.
     */
    override fun mapFailure(msg: String): Result<Nothing> = this
    
    /**
     * Implements the forEach function.
     */
    override fun forEach(onSuccess: (Nothing) -> Unit,
                         onFailure: (RuntimeException) -> Unit,
                         onEmpty: () -> Unit) = onEmpty()
        
    override fun toString(): String = "Empty"
  }    

  /**
   * A Result subtype that represents a failure. It has a RuntimeException
   * property that stores an exception as its value.
   */
  internal class Failure<out A>(internal val exception: RuntimeException):
                                                                 Result<A>() {
    override fun toString(): String = "Failure(${exception.message})"
    
    /**
     * Returns a new Failure with the same exception contained as the value.
     *
     * @param func  The mapping function.
     * @return      A Failure with the same exception.
     */
    override fun <B> map(func: (A) -> B): Result<B> =
      Failure(exception)
      
    /**
     * Returns a new Failure through mapping using the same exception value.
     *
     * @param func  The mapping function.
     * @return      A Failure containing the same exception.
     */
    override fun <B> flatMap(func: (A) -> Result<B>): Result<B> =
      Failure(exception)
      
    /**
     * Maps a Failure to a new Failure with the changed message. The original
     * exception will be wrapped in a new RuntimeException object.
     *
     * @param msg  The new error message.
     * @return     A Failure with the new error message.
     */
    override fun mapFailure(msg: String): Result<A> =
      Failure(RuntimeException(msg, exception))
      
    /**
     * Implements the forEach function.
     */
    override fun forEach(onSuccess: (A) -> Unit,
                         onFailure: (RuntimeException) -> Unit,
                         onEmpty: () -> Unit): Unit = onFailure(exception)
  }
  
  /**
   * A Result subtype that represents a success. It contains a property for the
   * value of the result.
   */
  internal class Success<out A>(internal val value: A): Result<A>() {
    override fun toString(): String = "Success($value)"
    
    /**
     * Returns the new Success with the mapped value unless an exception is
     * thrown calling 'func'. In the case of an exception, the exception will
     * be passed as a parameter to a newly constructed Failure.
     *
     * @param func  The mapping function.
     * @return      Either a new mapped Success or a Failure.
     */
    override fun <B> map(func: (A) -> B): Result<B> =
      try {
        Success(func(value))
      } catch (e: RuntimeException) {
        Failure(e)
      } catch (e: Exception) {
        Failure(RuntimeException(e))
      }
      
    /**
     * Maps the value to a new Success object using the provided function
     * parameter.
     *
     * @param func  The mapping function.
     * @return      Either a new mapped Success or a Failure
     */
    override fun <B> flatMap(func: (A) -> Result<B>): Result<B> =
      try {
        func(value)
      } catch (e: RuntimeException) {
        Failure(e)
      } catch (e: Exception) {
        Failure(RuntimeException(e))
      }
      
    /**
     * Implements the mapFailure function which just returns itself.
     */
    override fun mapFailure(msg: String): Result<A> = this
    
    /**
     * Implements the forEach function.
     */
    override fun forEach(onSuccess: (A) -> Unit,
                         onFailure: (RuntimeException) -> Unit,
                         onEmpty: () -> Unit): Unit = onSuccess(value)
  }
  
  /**
   * Companion operator overloads and class functions.
   */
  companion object {  
    /**
     * Overloads the () operator to create a new instance of Result as a
     * Success or Failure.
     */
    operator fun <A> invoke(a: A? = null): Result<A> =
      when (a) {
        null -> Failure(NullPointerException())
        else -> Success(a)
      }
      
    /**
     * Overloads the () operator which takes a value which may be null and a
     * String error message for this case.
     *
     * @param a    The Result value or null.
     * @param msg  An error message.
     * @return     A Success or Failure with an error message.
     */
    operator fun <A> invoke(a: A? = null, msg: String): Result<A> =
      when (a) {
        null -> Failure(NullPointerException(msg))
        else -> Success(a)
      }
      
    /**
     * Overloads the () operator which takes a value which may be null and a
     * predicate which when true creates a Success object or Failure when false.
     *
     * @param a     The possible Result value or null.
     * @param pred  The predicate function to determine if the result is valid.
     * @return      A Result object determined by the arguments.
     */
    operator fun <A> invoke(a: A? = null, pred: (A) -> Boolean): Result<A> =
      when (a) {
        null -> Failure(NullPointerException())
        else -> if (pred(a)) Success(a)
                else Empty
      }
      
    /**
     * Overloads the () operator which takes a value which may be null and an
     * error message. If the provided predicate returns true then a Success
     * object is created, otherwise false returns a Failure with the message.
     *
     * @param a     The possible Result value or null.
     * @param msg   An error message.
     * @param pred  The predicate function to determine if the result is valid.
     * @return      A Result object determined by the arguments.
     */
    operator fun <A> invoke(a: A? = null, msg: String,
                            pred: (A) -> Boolean): Result<A> =
      when (a) {
        null -> Failure(NullPointerException())
        else -> if (pred(a)) Success(a)
                else Failure(IllegalArgumentException(
                  "Argument $a does not match condition: $msg"))
      }
      
    /**
     * Overloads the () operator to return an Empty when no value is present.
     */
    operator fun <A> invoke(): Result<A> = Empty
    
    /**
     * Creates a new Result from calling the function parameter.
     *
     * @param f  A function of the form () -> A.
     * @return   A Success result if no exception occurs else Failure.
     */
    fun <A> of(f: () -> A): Result<A> =
      try {
        Result(f())
      } catch (e: RuntimeException) {
        Result.failure(e)
      } catch (e: Exception) {
        Result.failure(e)
      }
    
    /**
     * Creates a new Result from calling the predicate function parameter.
     *
     * @param pred   A predicate that determines the Result value.
     * @param value  The Result value if the predicate is true.
     * @param msg    A message value used on a false predicate.
     * @return       A Success or Failure result, depending on the predicate.
     */
    fun <T> of(predicate: (T) -> Boolean, value: T, msg: String): Result<T> =
      try {
        if (predicate(value))
          Result(value)
        else
          Result.failure("Assertion failed for value $value with message: $msg")
      } catch (e: Exception) {
        Result.failure(IllegalStateException("Exception while validating $value", e))
      }
    
    /**
     * Creates a Failure result with an error message encapsulated in an
     * IllegalStateException.
     */
    fun <A> failure(message: String): Result<A> =
      Failure(IllegalStateException(message))
    
    /**
     * Creates a Failure result with the provided RuntimeException.
     */
    fun <A> failure(exception: RuntimeException): Result<A> =
      Failure(exception)
      
    /**
     * Creates a Failure result with a generic Exception encapsulated in an
     * IllegalStateException.
     */
    fun <A> failure(exception: Exception): Result<A> =
      Failure(IllegalStateException(exception))
  }
  
  /**
   * Gets the value of the Result or provides a default value given as the
   * argument to the function.
   *
   * @param defaultValue  A default value if one does not exist.
   * @return              The Success value or default if a Failure.
   */
  fun getOrElse(defaultValue: @UnsafeVariance A): A =
    when (this) {
      is Success -> value
      else -> defaultValue
    }
    
  /**
   * Gets the value of the Result or provides a default value by calling the
   * function parameter.
   *
   * @param defaultValue  Default value mapping function.
   * @return              The Success value or default if a Failure
   */
  fun getOrElse(defaultValue: () -> @UnsafeVariance A): A =
    when (this) {
      is Success -> value
      else -> defaultValue()
    }
    
  /**
   * Gets the Result object itself or else a default one from calling the
   * provided function parameter.
   *
   * @param defaultValue  The function that provides the default Result.
   * @return              The 'this' object or the default.
   */
  fun orElse(defaultValue: () -> Result< @UnsafeVariance A>): Result<A> =
    when (this) {
      is Success -> this
      else -> try {
        defaultValue()
      } catch (e: RuntimeException) {
        Failure<A>(e)
      } catch (e: Exception) {
        Failure<A>(RuntimeException(e))
      }
    }
    
  /**
   * Filters the Result value and returns a new Result representing success or
   * failure.
   *
   * @param pred  The filtering predicate.
   * @return      A Success or Failure determined by the predicate.
   */
  fun filter(pred: (A) -> Boolean): Result<A> = flatMap {
    if (pred(it))
      this
    else
      failure("Condition not matched")
  }
  
  /**
   * Filters the Result value and returns a new Result representing success or
   * failure. This overload takes a String parameter as a custom message.
   *
   * @param pred  The filtering predicate.
   * @return      A Success or Failure determined by the predicate.
   */
  fun filter(message: String, pred: (A) -> Boolean): Result<A> = flatMap {
    if (pred(it))
      this
    else
      failure(message)
  }
  
  /**
   * Uses the predicate parameter to check if the wrapped value matches the
   * condition. The predicate is called on a Success object which will return
   * true or false. For a Failure object the default value of getOrElse will be
   * returned.
   *
   * @param pred  The matching predicate.
   * @return      True if the condition matches, False otherwise.
   */
  fun exists(pred: (A) -> Boolean): Boolean =
    map(pred).getOrElse(false)
}
