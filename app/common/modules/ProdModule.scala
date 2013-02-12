package common.modules

import com.tzavellas.sse.guice.ScalaModule
import common.translation._

class ProdModule extends ScalaModule {
  def configure() {
    bind[Translator].to[FrenchTranslatorImpl]
  }
}
