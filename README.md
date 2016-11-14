#Dolphin Voice Language

使用Java语言参照JVM实现的一门解释型语言.

```dv
var a = 1 + 3 * 4;
var b = 2;
var c = function() {
  var a = 3;
  println("inner a " + a);
  println("inner b " + b);
};
c();
println("outer a " +  a);
println("outer b " + b);
```
