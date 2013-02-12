Using Guice from within a Play 2.1 application
===============================================

This is a simple application demonstrating how to integrate a Play 2.1 application components with <a href="https://code.google.com/p/google-guice/">Google Guice framework</a>.


## How does it work?

See the FortyTwo Engineering blog for a more in-depth explaination of how to use Guice with Play 2.1. The basic steps:

### First, add the Guice dependency

In the `project/Build.scala` file, add a dependency to `spring-context`:

```scala
import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
  val appName         = "play21guicedemo"
  val appVersion      = "0.1-SNAPSHOT"

  val appDependencies = Seq(
    "com.google.inject" % "guice" % "3.0",
    "com.tzavellas" % "sse-guice" % "0.7.1"
  )
  val main = play.Project(appName, appVersion, appDependencies).settings()
}
```

### Using controllers instances

We wiil use _dynamic_ controller dispatching instead of the _statically compiled_ dispatching used by default in Play framework. To do that, just prefix your controller class name with the `@` symbol in the `routes` file:

```
GET    /       @controllers.Translator.greet()
```

### Injectable classes

We define example `Translator` classes as follows:

```scala
trait Translator {
  def translate(input: String): String
}
 
class FrenchTranslatorImpl extends Translator {
  val wordReplacements = Map(
    "hello" -> "bonjour",
    "hi" -> "salut",
    "greetings" -> "salutations"
  )
  def translate(input: String): String = {
    input.split("""\s+""").map(word => wordReplacements.get(word.toLowerCase).map(newWord => word match {
        case _ if word(0).isUpper => newWord.capitalize
        case _ => newWord
      }).getOrElse(word)).mkString(" ")
  }
}

class FakeTranslator extends Translator {
  def translate(input: String): String = input
}
```

### Managing controllers instances

The controllers instance management will be delegated to the `Global` object of your application. Here is an implementation of the `Global` using Guice:

```scala
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
```

Here, we used two different modules based on Play's mode.

```scala
class ProdModule extends ScalaModule {
  def configure() {
    bind[Translator].to[FrenchTranslatorImpl]
  }
}
 
class DevModule extends ScalaModule {
  def configure() {
    bind[Translator].to[FakeTranslator]
  }
}
```

### Using Guice with Play 2.1 controllers


```scala
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
```
