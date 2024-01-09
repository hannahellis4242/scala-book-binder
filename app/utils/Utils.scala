package utils

object Utils {
  def collectElements[T](xs: Seq[Option[T]]): Option[Seq[T]] =
    if (xs.contains(None)) None
    else Some(xs.flatMap(_.toList))
}