package io.github.frankbo.vocabularyextensionapi

import cats.Applicative
import cats.implicits._
import io.github.frankbo.vocabularyextensionapi.Models.Model._
import io.github.frankbo.vocabularyextensionapi.Static.Static._

trait Validation[F[_]] {
  def getTranslation(language: String,
                     input: String,
                     wordId: Int): F[Translation]
}

object Validation {
  def apply[F[_]](implicit en: Validation[F]): Validation[F] = en

  def impl[F[_]: Applicative]: Validation[F] = new Validation[F] {
    override def getTranslation(language: String,
                                input: String,
                                wordId: Int): F[Translation] = {
      val origVocabulary = staticVocabularies.filter(_.id == wordId).head
      val translation = staticVocabularies
        .filter(v => v.groupId == origVocabulary.id && v.lang == language)
        .head // TODO unsafe replace with headOption
      Translation(text = translation.word,
                  correct = (input == translation.word)).pure[F]
    }
  }
}
