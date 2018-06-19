import scala.util.parsing.combinator.RegexParsers

type ObstKiste = List[Obst]

trait Obst {
  def IN(kiste: ObstKiste): MyBool = MyBool(kiste.contains(this))
}

case class MyBool(x: Boolean) {
  def AND(other: MyBool) = if (x) other.x else this.x

  def OR(other: MyBool) = if (x) this.x else other.x
}


case object Apfel extends Obst

case object Birne extends Obst

case object Kirsche extends Obst

case object anderesObst extends Obst

val obstkiste = List(Apfel, Kirsche)

(Apfel IN obstkiste) AND (Kirsche IN obstkiste)
(Apfel IN obstkiste) AND (Birne IN obstkiste)
(Apfel IN obstkiste) OR (Birne IN obstkiste)


Apfel.IN(obstkiste).AND(Birne.IN(obstkiste))

