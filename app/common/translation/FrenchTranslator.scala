package common.translation

class FrenchTranslatorImpl extends Translator {
  /*  Obviously, this is not a real translation service. But in real life, 
   *  you may be calling remote APIs, which are potentially slow and would 
   *  adversely affect your testing speed.
   */

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

class FakeFrenchTranslator extends Translator {
  /*  You could even mock out a specific Translator
   */

  def translate(input: String): String = input
}