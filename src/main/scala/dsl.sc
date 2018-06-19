type ObstKiste = List[Obst]

trait Comparable {
  def erfülltMetaAnforderung(metaAnforderung: Comparable): Boolean
}

trait Zustand extends Comparable

case object verdorben extends Zustand {
  override def erfülltMetaAnforderung(metaAnforderung: Comparable): Boolean = {
    metaAnforderung == unset ||
      identity(metaAnforderung) == this
  }
}

case object unverdorben extends Zustand {
  override def erfülltMetaAnforderung(metaAnforderung: Comparable): Boolean = {
    metaAnforderung == unset ||
      identity(metaAnforderung) == this
  }
}

case object unset extends Zustand {
  override def erfülltMetaAnforderung(metaAnforderung: Comparable): Boolean = true
}

trait Obst {
  val rotten: Zustand
  val haltbarBis: Option[String]

  def IN(kiste: ObstKiste): Boolean = kiste.contains(this)

  def erfüllt(anforderung: Obst): Boolean = {
    this.rotten.erfülltMetaAnforderung(anforderung.rotten)
  }

}


case class Apfel(override val rotten: Zustand) extends Obst {
  override val haltbarBis: Option[String] = Some("01-01-2010")
}

object Apfel {
  def apply(): Apfel = Apfel(unset)

  def apply(zustand: Zustand): Apfel = Apfel(zustand)
}

case class Birne(override val rotten: Zustand) extends Obst {
  override val haltbarBis: Option[String] = Some("01-01-2010")
}

object Birne {
  def apply(): Birne = Birne(unset)

  def apply(zustand: Zustand): Birne = Birne(zustand)
}

case class Kirsche(override val rotten: Zustand) extends Obst {
  override val haltbarBis: Option[String] = None
}

object Kirsche {
  def apply(): Kirsche = Kirsche(unset)

  def apply(zustand: Zustand): Kirsche = Kirsche(zustand)
}


val obstkiste = List(Apfel(verdorben), Kirsche())

Apfel(verdorben) == Apfel()
Apfel(verdorben) == Apfel()
Apfel() == Apfel(unverdorben)
Apfel(verdorben) == Apfel(unverdorben)
Apfel(unverdorben) == Apfel(unverdorben)
Apfel(verdorben) == Apfel(verdorben)


Apfel(unverdorben).erfüllt(Apfel(verdorben))
Apfel(unverdorben).erfüllt(Apfel(unverdorben))
Apfel(unverdorben).erfüllt(Apfel())
Apfel(verdorben).erfüllt(Apfel())
Apfel().erfüllt(Apfel(unverdorben))
Apfel().erfüllt(Apfel(verdorben))
Apfel().erfüllt(Apfel())
