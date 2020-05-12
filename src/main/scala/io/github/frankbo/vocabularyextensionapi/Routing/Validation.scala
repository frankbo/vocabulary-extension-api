package io.github.frankbo.vocabularyextensionapi.Routing

import cats.Applicative
import cats.data.OptionT
import io.github.frankbo.vocabularyextensionapi.Database.VocabularyRepo
import io.github.frankbo.vocabularyextensionapi.Models.Model._

trait Validation[F[_]] {
  def validateTranslation(
      language: String,
      input: String,
      wordId: Int,
      vocabularyRepo: VocabularyRepo[F]): F[Option[Translation]]
}

object Validation {
  def apply[F[_]](implicit en: Validation[F]): Validation[F] = en

  def impl[F[_]: Applicative]: Validation[F] = new Validation[F] {
    override def validateTranslation(
        language: String,
        input: String,
        wordId: Int,
        vocabularyRepo: VocabularyRepo[F]): F[Option[Translation]] = {
      OptionT(
        vocabularyRepo
          .getVocabularyByIdAndLang(wordId, language))
        .map(v => {
          Translation(
            text = v.word,
            correct = v.word.toLowerCase.trim == input.toLowerCase.trim)
        })
        .value
    }
  }
}
