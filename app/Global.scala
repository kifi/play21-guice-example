import play.api.{GlobalSettings, Application}
import common.modules._
import com.google.inject._
import play.api.Play
import play.api.Play.current


object Global extends GlobalSettings {
  private lazy val injector = {
    Play.isProd match {
      case true => Guice.createInjector(new ProdModule)
      case false => Guice.createInjector(new DevModule)
    }
  }

  override def getControllerInstance[A](clazz: Class[A]) = {
    injector.getInstance(clazz)
  }
}