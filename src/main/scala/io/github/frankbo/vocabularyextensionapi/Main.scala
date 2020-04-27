package io.github.frankbo.vocabularyextensionapi

import cats.effect._
import doobie._
import doobie.util.transactor.Transactor
import org.http4s.HttpRoutes
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._

object Main extends IOApp {
  def transactor(): IO[Transactor[IO]] = {
    val xa = Transactor.fromDriverManager[IO](
      "org.postgresql.Driver", // driver classname
      "jdbc:postgresql://localhost:5432/vocabularies", // connect URL (driver-specific) TODO change to "jdbc:postgresql://localhost:5432/vocabularydb"
      "postgres", // user
      "mysecretpassword", // password
      Blocker.liftExecutionContext(ExecutionContexts.synchronous)
    )
    IO.pure(xa)
  }

  def server[F[_]: ConcurrentEffect: ContextShift: Timer](
      routes: HttpRoutes[F]
  ): Resource[F, Server[F]] =
    BlazeServerBuilder[F]
      .bindHttp(8080, "localhost")
      .withHttpApp(routes.orNotFound)
      .resource

  def run(args: List[String]): IO[ExitCode] = {
    val v = for {
      //TODO pass execution context
      aux <- transactor()
      vocabularies <- VocabularyRepo.fromTransactor[IO](aux).fetchVocabulary()
    } yield vocabularies
    v.map(va => {
      println(va)
      ExitCode.Success
    })

  }

  //      Server.stream[IO].compile.drain.as(ExitCode.Success)
}
