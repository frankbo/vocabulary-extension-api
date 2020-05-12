package io.github.frankbo.vocabularyextensionapi

import cats.effect._
import cats.implicits._
import doobie._
import doobie.util.transactor.Transactor
import io.github.frankbo.vocabularyextensionapi.Database.VocabularyRepo
import io.github.frankbo.vocabularyextensionapi.Routing.CollectedRoutes
import org.http4s.HttpRoutes
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder

object Main extends IOApp {
  def transactor[F[_]: Async: ContextShift](): Resource[F, Transactor[F]] = {
    ExecutionContexts
      .cachedThreadPool[F]
      .map(
        ec =>
          Transactor.fromDriverManager[F](
            "org.postgresql.Driver", // driver classname
            "jdbc:postgresql://localhost:5432/vocabularies", // connect URL (driver-specific)
            "postgres", // user
            "mysecretpassword", // password
            Blocker.liftExecutionContext(ec)
        ))
  }

  def server[F[_]: ContextShift: ConcurrentEffect: Timer](
      routes: HttpRoutes[F]
  ): Resource[F, Server[F]] =
    BlazeServerBuilder[F]
      .bindHttp(8080, "localhost")
      .withHttpApp(routes.orNotFound)
      .resource

  def resource[F[_]: Sync: ContextShift: ConcurrentEffect: Timer]
    : Resource[F, Server[F]] = {
    for {
      aux <- transactor()
      vocabularyRepo = VocabularyRepo.fromTransactor(aux)
      routes = CollectedRoutes.httpRoutes[F](vocabularyRepo)
      srv <- server[F](routes)
    } yield srv
  }

  def run(args: List[String]): IO[ExitCode] =
    resource[IO].use(_ => IO.never.as(ExitCode.Success))
}
