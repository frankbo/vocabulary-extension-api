package io.github.frankbo.vocabularyextensionapi

import cats.effect.Sync
import doobie._
import doobie.implicits._
import doobie.util.compat.FactoryCompat
import io.github.frankbo.vocabularyextensionapi.Models.Model.DBVocabulary

trait VocabularyRepo[F[_]] {
  def fetchVocabulary(): F[List[DBVocabulary]]
}

object VocabularyRepo {
  def fromTransactor[F[_]: Sync](xa: Transactor[F]): VocabularyRepo[F] =
    new VocabularyRepo[F] {
      // see https://github.com/tpolecat/doobie/issues/1061
      private implicit def listFactoryCompat[A]: FactoryCompat[A, List[A]] =
        FactoryCompat.fromFactor(List.iterableFactory)

      val select: Fragment =
        fr"""
        SELECT vocabulary_id, word, language_id, group_id
        FROM   vocabulary
      """

      def fetchVocabulary(): F[List[DBVocabulary]] =
        select.query[DBVocabulary].to[List].transact(xa)
    }
}
