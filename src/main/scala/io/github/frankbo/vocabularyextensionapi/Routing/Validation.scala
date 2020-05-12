package io.github.frankbo.vocabularyextensionapi.Routing

import cats.Applicative
import cats.implicits._
import io.github.frankbo.vocabularyextensionapi.Database.VocabularyRepo
import io.github.frankbo.vocabularyextensionapi.Models.Model._

trait Validation[F[_]] {
  def validateTranslation(language: String,
                          input: String,
                          wordId: Int,
                          vocabularyRepo: VocabularyRepo[F]): F[Translation]
}

object Validation {
  def apply[F[_]](implicit en: Validation[F]): Validation[F] = en

  def impl[F[_]: Applicative]: Validation[F] = new Validation[F] {
    override def validateTranslation(
        language: String,
        input: String,
        wordId: Int,
        vocabularyRepo: VocabularyRepo[F]): F[Translation] = {
      vocabularyRepo
        .getVocabularyByIdAndLang(wordId, language)
        .map({
          case Some(v) =>
            Translation(
              text = v.word,
              correct = (v.word.toLowerCase.trim == input.toLowerCase.trim))
          case None =>
            Translation("wrong", false) // TODO replace with sth meanningful
        })
    }
  }
}
