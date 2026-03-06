package org.sonar.plugins.powershell.ast;

import java.util.LinkedList;
import java.util.List;

public class Tokens {

  private int complexity;

  private final List<Token> tokenList = new LinkedList<>();

  public List<Token> getTokens() {
    return tokenList;
  }

  public void setComplexity(int complexity) {
    this.complexity = complexity;
  }

  public int getComplexity() {
    return complexity;
  }

  public record Token(
      String text,
      String value,
      String tokenFlags,
      String kind,
      String cType,
      int startLineNumber,
      int endLineNumber,
      int startColumnNumber,
      int endColumnNumber,
      long startOffset,
      long endOffset) {}
}
