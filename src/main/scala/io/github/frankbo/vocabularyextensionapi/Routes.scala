package io.github.frankbo.vocabularyextensionapi

import cats.effect.Sync
import cats.implicits._
import org.http4s.{HttpRoutes, QueryParamDecoder}
import org.http4s.dsl.Http4sDsl
import io.github.frankbo.vocabularyextensionapi.Models.Model._
import org.http4s.dsl.impl.{
  OptionalQueryParamDecoderMatcher,
  QueryParamDecoderMatcher
}

object Routes {
  def jokeRoutes[F[_]: Sync](J: Jokes[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "joke" =>
        for {
          joke <- J.get
          resp <- Ok(joke)
        } yield resp
    }
  }

  object OptionalIdParam extends OptionalQueryParamDecoderMatcher[Int]("id")
  object LangParam extends QueryParamDecoderMatcher[String]("lang") // TODO Refined types

  def vocabulariesRoute[F[_]: Sync](V: Vocabularies[F]): HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "vocabularies" :? LangParam(language) +& OptionalIdParam(
            id) =>
        for {
          vocabulary <- V.getVocabulary(language, id)
          resp <- Ok(vocabulary)
        } yield resp
    }
  }

  object InputParam extends QueryParamDecoderMatcher[String]("input")
  object WordIdParam extends QueryParamDecoderMatcher[Int]("word-id")

  def validationRoute[F[_]: Sync](V: Validation[F]): HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "validation" :? LangParam(language) +& InputParam(
            input) +& WordIdParam(wordId) =>
        for {
          translation <- V.getTranslation(language, input, wordId)
          resp <- Ok(translation)
        } yield resp
    }
  }
}
