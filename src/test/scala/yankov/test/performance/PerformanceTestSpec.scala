package yankov.test.performance

class PerformanceTestSpec extends PerformanceTest {
  "performance test pass" in {
    performanceTest[Int](Seq(1, 2, 3), x => x.toString)("Object")("function", _ => () => Thread.sleep(5), Seq(10.0, 20.0, 30.0))
  }

  "performance test fail" in {
    try {
      performanceTest[Int](Seq(1, 2, 3), x => x.toString)("Object")("function", _ => () => Thread.sleep(25), Seq(10.0, 20.0, 30.0))
      throw new Exception("Expected exception was not thrown.")
    } catch {
      case e: Exception =>
        val msg = "Performance test failed for [Object.function] parameters [1]. Expected execution time [10.0], actual execution time [25.0]"
        e.getMessage shouldBe msg
    }
  }
}
