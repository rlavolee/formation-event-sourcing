package infra.repository

sealed trait StorageResult

object StorageResult{
  case object ACK extends StorageResult
  case object ERR extends StorageResult
}
