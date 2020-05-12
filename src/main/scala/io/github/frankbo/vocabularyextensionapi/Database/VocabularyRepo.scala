package io.github.frankbo.vocabularyextensionapi.Database

import cats.effect.Sync
import doobie._
import doobie.implicits._
import io.github.frankbo.vocabularyextensionapi.Models.Model.Vocabulary

trait VocabularyRepo[F[_]] {
  def getVocabularyByIdAndLang(id: Int, language: String): F[Option[Vocabulary]]
}

object VocabularyRepo {
  def fromTransactor[F[_]: Sync](xa: Transactor[F]): VocabularyRepo[F] =
    new VocabularyRepo[F] {
      def searchByIdAndLang(id: Int,
                            language: String): doobie.Query0[Vocabulary] = {
        sql"""
             |SELECT word, language.language_short, group_id, vocabulary_id
             |FROM vocabulary
             |INNER JOIN language ON language.language_id = vocabulary.group_id
             |WHERE vocabulary.language_id = $id
             |AND group_id = (SELECT language_id FROM language WHERE language_short = $language)
             |LIMIT 1
             |""".stripMargin
          .query[Vocabulary]
      }

      def getVocabularyByIdAndLang(id: Int,
                                   language: String): F[Option[Vocabulary]] =
        searchByIdAndLang(id, language).option.transact(xa)
    }
}
