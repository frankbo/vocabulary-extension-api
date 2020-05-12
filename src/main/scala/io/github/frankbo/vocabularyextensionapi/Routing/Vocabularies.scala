package io.github.frankbo.vocabularyextensionapi.Routing

import cats.Applicative
import cats.data.OptionT
import io.github.frankbo.vocabularyextensionapi.Database.VocabularyRepo
import io.github.frankbo.vocabularyextensionapi.Models.Model._

trait Vocabularies[F[_]] {
  def getVocabulary(language: String,
                    id: Option[Int],
                    vocabularyRepo: VocabularyRepo[F]): F[Option[Vocabulary]]
}

object Vocabularies {

  def apply[F[_]](implicit ev: Vocabularies[F]): Vocabularies[F] = ev

  def impl[F[_]: Applicative]: Vocabularies[F] = new Vocabularies[F] {
    override def getVocabulary(
        language: String,
        id: Option[Int],
        vocabularyRepo: VocabularyRepo[F]): F[Option[Vocabulary]] =
      id match {
        case Some(value) =>
          OptionT(
            vocabularyRepo
              .getVocabularyByIdAndLang(value, language)
          ).value
        case None => Applicative[F].pure(None)
      }

  }
}
