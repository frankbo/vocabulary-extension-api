package io.github.frankbo.vocabularyextensionapi.Models

import cats.Applicative
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

object Model {
  case class Vocabulary(word: String, lang: String, groupId: Int)
  case class Translation(text: String, correct: Boolean)

  // Encoding
  implicit def vocabularyEntityEncoder[F[_]: Applicative]
    : EntityEncoder[F, Vocabulary] =
    jsonEncoderOf[F, Vocabulary]

  implicit val vocabularyEncoder: Encoder[Vocabulary] =
    Encoder.forProduct3("word", "lang", "id")(v => (v.word, v.lang, v.groupId))

  implicit def translationEntityEncoder[F[_]: Applicative]
    : EntityEncoder[F, Translation] =
    jsonEncoderOf[F, Translation]

  implicit val translationEncoder: Encoder[Translation] =
    new Encoder[Translation] {
      final def apply(a: Translation): Json = Json.obj(
        ("text", Json.fromString(a.text)),
        ("correct", Json.fromBoolean(a.correct)),
      )
    }
}
