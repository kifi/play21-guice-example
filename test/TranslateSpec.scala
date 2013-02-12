package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import play.api._
import play.api.mvc._
import common.translation.{Translator, FakeTranslator}
import com.google.inject._
import controllers.Translate

class TranslateSpec extends Specification {
  
  "Translate" should {
    
    // The normal Play! way
    "accept a name, and return a proper greeting" in {
      running(FakeApplication()) {
        val translated = route(FakeRequest(GET, "/greet/Barney")).get
        
        status(translated) must equalTo(OK)
        contentType(translated) must beSome.which(_ == "text/html")
        contentAsString(translated) must contain ("Barney")     
      }
    }

    // Providing a fake Global, to explitly mock out the injector
    object FakeTranslatorGlobal extends play.api.GlobalSettings {
      override def getControllerInstance[A](clazz: Class[A]) = {
        new Translate(new FakeTranslator).asInstanceOf[A]
      }
    }
    "accept a name, and return a proper greeting (explicitly mocking module)" in {
      running(FakeApplication(withGlobal = Some(FakeTranslatorGlobal))) {
        val home = route(FakeRequest(GET, "/greet/Barney")).get
        contentAsString(home) must contain ("Hello Barney")
      }
    }

    // Calling the controller directly, without creating a new FakeApplication
    // (This is the fastest)
    "accept a name, and return a proper greeting (controller directly, no FakeApplication!)" in {
      val controller = new Translate(new FakeTranslator)
      val result = controller.greet(name = "Barney")(FakeRequest())
      contentAsString(result) must contain ("Hello Barney")
    }
  }
}