package yankov.test.performance

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class PerformanceTest extends AnyFreeSpec with Matchers {
  private val numberOfExecution = 50
  private val threshold = 3

  def performanceTest[T](parameters: Seq[T], printParameters: T => String)
                        (objectName: String)
                        (functionName: String, createFunction: CreateFunction[T], expectedTimesMillis: Seq[Double]): Unit = {
    def assertResult(r: PerformanceResult[T], expectedTime: Double): Unit = {
      def isPositiveOrZero(x: Double): Boolean = scala.math.signum(x).toInt >= 0

      val delta = expectedTime - r.executionTimeMillis
      val passed = isPositiveOrZero(delta) || (scala.math.abs(delta) <= threshold * r.executionTimeMillisStd)
      if (!passed) {
        def print(x: Double): String = s"%.1f".format(x)

        fail(s"Performance test failed for [$objectName.$functionName] parameters [${printParameters(r.parameters)}]. Expected execution time [${print(expectedTime)}], actual execution time [${print(r.executionTimeMillis)}]")
      }
    }

    parameters
      .map(p => measure(p, createFunction, objectName, functionName))
      .zip(expectedTimesMillis)
      .foreach(x => assertResult(x._1, x._2))
  }

  private def measure[T](parameters: T,
                         createFunction: CreateFunction[T],
                         objectName: String,
                         functionName: String): PerformanceResult[T] = {
    val executionTimes = List.fill(numberOfExecution)(createFunction(parameters)).map {
      f =>
        val start = System.currentTimeMillis()
        f()
        val end = System.currentTimeMillis()
        end - start
    }.map(_.toDouble)
    PerformanceResult(
      parameters,
      objectName,
      functionName,
      scala.math.round(mean(executionTimes)),
      std(executionTimes)
    )
  }

  private def mean(x: Seq[Double]): Double = x.map(y => y / x.size).sum

  private def std(x: Seq[Double]): Double = scala.math.sqrt(mean(x.map(y => y * y)) - scala.math.pow(mean(x), 2.0))
}
