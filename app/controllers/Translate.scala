package controllers

import play.api._
import play.api.mvc._
import common.translation.Translator
import com.google.inject._

@Singleton
class Translate @Inject()(translator: Translator) extends Controller {
  def greet(name: String) = Action {
    Ok(views.html.greet(translator.translate(s"Hello $name!")))
  }
}