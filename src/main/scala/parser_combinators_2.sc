import scala.util.parsing.combinator.RegexParsers


trait Obst {
  def IN(kiste: List[Obst]): Boolean = kiste.contains(this)
}


case class Apfel(rotten: Boolean = false) extends Obst

case class Birne(rotten: Boolean = false) extends Obst

case class Kirsche(rotten: Boolean = false) extends Obst

case class anderesObst(rotten: Boolean = false) extends Obst


/*
(Apfel ODER Birne) IN Obstkiste
=> Apfel.IN(Obstkiste) || Birne.IN(Obstkiste)
 */


case class Obstkiste(kiste: List[Obst]) extends RegexParsers {

  override def skipWhitespace = true

  override val whiteSpace = "[ \t\r\f]+".r

  val or = "ODER"
  val and = "UND"
  val rotten = "VERDORBEN"
  val identifier = rep("[a-z]".r).map(_.mkString).map {
    case "apfel" => classOf[Apfel]
    case "birne" => classOf[Birne]
    case "kirsche" => classOf[Kirsche]
    case _ => classOf[anderesObst]
  }

  val obst: Parser[Boolean] = identifier | identifier ~ "(" ~> rotten <~ ")" map {
    case o: Obst => kiste.contains(o)
    case i: classOf[Apfel] ~ "(ROTTEN)" => kiste.contains(Apfel(true))
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


val kiste1 = Obstkiste(List(Apfel(rotten = true), Birne()))

kiste1.erfüllt("apfel(ROTTEN) ODER birne")
kiste1.erfüllt("apfel UND birne")
kiste1.erfüllt("apfel UND kirsche")
kiste1.erfüllt("apfel ODER kirsche")
kiste1.erfüllt("kirsche ODER ((kirsche UND birne) ODER apfel)")
