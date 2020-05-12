package io.github.frankbo.vocabularyextensionapi

import cats.effect._
import cats.implicits._
import doobie._
import doobie.util.transactor.Transactor
import io.github.frankbo.vocabularyextensionapi.Database.VocabularyRepo
import io.github.frankbo.vocabularyextensionapi.Routing.RouteCollection
import org.http4s.HttpRoutes
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder

object Main extends IOApp {
  def transactor(): Resource[IO, Transactor[IO]] = {
    ExecutionContexts
      .cachedThreadPool[IO]
      .map(
        ec =>
          Transactor.fromDriverManager[IO](
            "org.postgresql.Driver", // driver classname
            "jdbc:postgresql://localhost:5432/vocabularies", // connect URL (driver-specific) TODO change to "jdbc:postgresql://localhost:5432/vocabularydb"
            "postgres", // user
            "mysecretpassword", // password
            Blocker.liftExecutionContext(ec)
        ))
  }

  def server[F[_]: ConcurrentEffect: ContextShift: Timer](
      routes: HttpRoutes[F]
  ): Resource[F, Server[F]] =
    BlazeServerBuilder[F]
      .bindHttp(8080, "localhost")
      .withHttpApp(routes.orNotFound)
      .resource

  def resource: Resource[IO, Server[IO]] = {
    for {
      aux <- transactor()
      vocabularyRepo = VocabularyRepo.fromTransactor(aux)
      routes = RouteCollection.httpRoutes[IO](vocabularyRepo)
      srv <- server[IO](routes)
    } yield srv
  }

  def run(args: List[String]): IO[ExitCode] =
    resource.use(_ => IO.never.as(ExitCode.Success))
}
