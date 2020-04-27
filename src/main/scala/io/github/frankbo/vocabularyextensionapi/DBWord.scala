package io.github.frankbo.vocabularyextensionapi

object DBWord {
  // see https://github.com/tpolecat/doobie/issues/1061
  //  private implicit def listFactoryCompat[A]: FactoryCompat[A, List[A]] =
  //    FactoryCompat.fromFactor(List.iterableFactory)

//  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)
//  val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
//    "org.postgresql.Driver", // driver classname
//    "jdbc:postgresql://localhost:5432/world", // connect URL (driver-specific) TODO change to "jdbc:postgresql://localhost:5432/vocabularydb"
//    "postgres", // user
//    "mysecretpassword", // password
//    Blocker.liftExecutionContext(ExecutionContexts.synchronous)
//  )
//
//  case class Country(code: Int, name: String, pop: Int, gnp: Double)
//
//  def biggerThan(minPop: Short) =
//    sql"""
//    select code, name, population, gnp, indepyear
//    from country
//    where population > $minPop
//  """.query[Country]
//
//  def chapter6() = {
//    val y = xa.yolo
//    import y._
//
//    biggerThan(0).check.unsafeRunSync
//  }
//
//  def connect(): Unit = {
//    chapter6()
//  }
}
