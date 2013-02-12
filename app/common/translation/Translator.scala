package common.translation

trait Translator {
  def translate(input: String): String
}

class FakeTranslator extends Translator {
  /*  Instead, you'd use this translation service in tests where you do not 
   *  need a correct translation, and would prefer something fast.
   */
  def translate(input: String): String = input
}