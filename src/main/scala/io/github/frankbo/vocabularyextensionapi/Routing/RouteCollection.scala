package io.github.frankbo.vocabularyextensionapi.Routing

import cats.effect.ConcurrentEffect
import io.github.frankbo.vocabularyextensionapi.Database.VocabularyRepo
import org.http4s.HttpRoutes
import cats.implicits._

object RouteCollection {
  def httpRoutes[F[_]: ConcurrentEffect](
      vr: VocabularyRepo[F]): HttpRoutes[F] = {
    val vocabulariesAlg = Vocabularies.impl[F]
    val validationAlg = Validation.impl[F]

    // Combine Service Routes into an HttpApp.
    // Can also be done via a Router if you
    // want to extract a segments not checked
    // in the underlying routes.

    Routes.vocabulariesRoute[F](vocabulariesAlg, vr) <+>
      Routes.validationRoute[F](validationAlg, vr)
  }
}
