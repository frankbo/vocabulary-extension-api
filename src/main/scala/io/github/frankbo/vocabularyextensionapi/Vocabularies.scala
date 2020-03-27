package io.github.frankbo.vocabularyextensionapi

import cats.{Applicative}
import cats.implicits._
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe._
import io.circe.generic.auto._
import io.circe.syntax._

trait Vocabularies[F[_]] {
  def getVocabulary: F[Vocabularies.Vocabulary]
}

object Vocabularies {
  implicit def apply[F[_]](implicit ev: Vocabularies[F]): Vocabularies[F] = ev

  final case class Vocabulary(word: String, lang: String, groupId: Int, id: Int)
  object Vocabulary {
    implicit val vocabularyEncoder: Encoder[Vocabulary] = new Encoder[Vocabulary] {
      final def apply(v: Vocabulary): Json = v.asJson
    }

    implicit def vocabularyEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Vocabulary] =
      jsonEncoderOf[F, Vocabulary]
  }

  def impl[F[_]: Applicative]: Vocabularies[F] = new Vocabularies[F]{
    def getVocabulary: F[Vocabulary] =
      Vocabulary("Hola", "es",1,3).pure[F]
  }

}