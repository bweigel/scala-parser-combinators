import scala.util.parsing.combinator.RegexParsers


trait Obst

case object Apfel extends Obst

case object Birne extends Obst

case object Kirsche extends Obst

case object anderesObst extends Obst


/*
(Apfel ODER Birne) IN Obstkiste
=> Apfel.IN(Obstkiste) || Birne.IN(Obstkiste)
 */


case class Obstkiste(kiste: List[Obst]) extends RegexParsers {

  override def skipWhitespace = true

  override val whiteSpace = "[ \t\r\f]+".r

  val or = "ODER"
  val and = "UND"
  val obst: Parser[Boolean] = rep("[a-z]".r).map(_.mkString).map {
    case "apfel" => kiste.contains(Apfel)
    case "birne" => kiste.contains(Birne)
    case "kirsche" => kiste.contains(Kirsche)
    case _ => kiste.contains(anderesObst)
  }

  val anteil: Parser[Boolean] =  "(" ~> expr <~ ")" | obst

  val expr: Parser[Boolean] = anteil ~ (and|or) ~ anteil map {
    case l ~ "ODER" ~ r => l || r
    case l ~ "UND" ~ r => l && r
  }


  def erfüllt(obstAnforderung: String): Boolean = {
    this.parseAll(this.expr, obstAnforderung).get
  }

}


val kiste1 = Obstkiste(List(Apfel, Birne))

kiste1.erfüllt("apfel ODER birne")
kiste1.erfüllt("apfel UND birne")
kiste1.erfüllt("apfel UND kirsche")
kiste1.erfüllt("apfel ODER kirsche")
kiste1.erfüllt("kirsche ODER ((kirsche UND birne) ODER apfel)")

