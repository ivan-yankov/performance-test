package yankov.test

package object performance {
  type TestedFunction = () => Unit
  type CreateFunction[T] = T => TestedFunction

  case class PerformanceSeed[T](parameters: T, expectedTimeMillis: Double)

  case class PerformanceResult[T](parameters: T,
                                  objectName: String,
                                  functionName: String,
                                  executionTimeMillis: Double,
                                  executionTimeMillisStd: Double)
}
