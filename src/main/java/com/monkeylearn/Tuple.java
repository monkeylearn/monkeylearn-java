package com.monkeylearn;

public class Tuple<T1,T2> {
  private T1 f1;
  private T2 f2;
  public Tuple(T1 f1, T2 f2) {
    this.f1 = f1; this.f2 = f2;
  }
  public T1 getF1() {return f1;}
  public T2 getF2() {return f2;}
}
