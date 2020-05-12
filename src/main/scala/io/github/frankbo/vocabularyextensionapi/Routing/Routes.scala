package io.github.frankbo.vocabularyextensionapi.Routing

import cats.effect.Sync
import cats.implicits._
import io.github.frankbo.vocabularyextensionapi.Database.VocabularyRepo
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.{
  OptionalQueryParamDecoderMatcher,
  QueryParamDecoderMatcher
}

object Routes {
  object OptionalIdParam extends OptionalQueryParamDecoderMatcher[Int]("id")
  object LangParam extends QueryParamDecoderMatcher[String]("lang")

  def vocabulariesRoute[F[_]: Sync](v: Vocabularies[F],
                                    vr: VocabularyRepo[F]): HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "vocabularies" :? LangParam(language) +& OptionalIdParam(
            id) =>
        for {
          vocabulary <- v.getVocabulary(language, id, vr)
          resp <- Ok(vocabulary)
        } yield resp
    }
  }

  object InputParam extends QueryParamDecoderMatcher[String]("input")
  object WordIdParam extends QueryParamDecoderMatcher[Int]("word-id")

  def validationRoute[F[_]: Sync](v: Validation[F],
                                  vr: VocabularyRepo[F]): HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "validation" :? LangParam(language) +& InputParam(
            input) +& WordIdParam(wordId) =>
        for {
          translation <- v.validateTranslation(language, input, wordId, vr)
          resp <- Ok(translation)
        } yield resp
    }
  }
}
