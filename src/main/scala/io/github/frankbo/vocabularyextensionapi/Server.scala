package io.github.frankbo.vocabularyextensionapi

import cats.effect.{ConcurrentEffect, Timer}
import cats.implicits._
import org.http4s.HttpApp
import org.http4s.implicits._
import org.http4s.server.middleware.Logger


object Server {

  def app[F[_]: ConcurrentEffect](implicit T: Timer[F]): HttpApp[F] = {
    val vocabulariesAlg = Vocabularies.impl[F]
    val validationAlg = Validation.impl[F]

    // Combine Service Routes into an HttpApp.
    // Can also be done via a Router if you
    // want to extract a segments not checked
    // in the underlying routes.
    val httpApp = (
      Routes.vocabulariesRoute[F](vocabulariesAlg) <+>
        Routes.validationRoute[F](validationAlg)
    ).orNotFound

    // With Middlewares in place
    val app = Logger.httpApp(true, true)(httpApp)
    app
}
