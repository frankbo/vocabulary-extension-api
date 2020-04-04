package io.github.frankbo.vocabularyextensionapi

import cats.Applicative
import cats.implicits._
import io.github.frankbo.vocabularyextensionapi.Models.Model._
import io.github.frankbo.vocabularyextensionapi.Static.Static._

trait Vocabularies[F[_]] {
  def getVocabulary(language: String, id: Option[Int]): F[Vocabulary]
}

object Vocabularies {

  def apply[F[_]](implicit ev: Vocabularies[F]): Vocabularies[F] = ev

  def impl[F[_]: Applicative]: Vocabularies[F] = new Vocabularies[F] {
    override def getVocabulary(_language: String,
                               id: Option[Int]): F[Vocabulary] =
      id match {
        case Some(v) =>
          staticVocabularies
            .filter(_.id == v)
            .head
            .pure[F] // TODO Replace with headOption
        case None =>
          staticVocabularies.head.pure[F] // TODO Replace with headOption
      }

  }
}
