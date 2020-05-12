package io.github.frankbo.vocabularyextensionapi.Routing

import cats.Applicative
import cats.implicits._
import io.github.frankbo.vocabularyextensionapi.Database.VocabularyRepo
import io.github.frankbo.vocabularyextensionapi.Models.Model._

trait Vocabularies[F[_]] {
  def getVocabulary(language: String,
                    id: Option[Int],
                    vocabularyRepo: VocabularyRepo[F]): F[Vocabulary]
}

object Vocabularies {

  def apply[F[_]](implicit ev: Vocabularies[F]): Vocabularies[F] = ev

  def impl[F[_]: Applicative]: Vocabularies[F] = new Vocabularies[F] {
    override def getVocabulary(
        language: String,
        id: Option[Int],
        vocabularyRepo: VocabularyRepo[F]): F[Vocabulary] =
      id match {
        case Some(v) =>
          vocabularyRepo
            .getVocabularyByIdAndLang(v, language)
            .map({
              case Some(v) => v
              case None    => Vocabulary("sfsf", "fsdfd", 32, 22)
            })

        case None =>
          Vocabulary("sfsf", "fsdfd", 32, 22)
            .pure[F] // TODO REPLACE THE CRAP RESULT
      }

  }
}
