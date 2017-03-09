#Dolphin Voice Language

使用Java语言参照JVM实现的一门解释型语言.

## Features

1. 函数多参数支持
2. 无缝植入Java类特性

## Examples

```dv
var a = 1 + 3 * 4;
var b = 2;
function cc(d){
	println("d=" + d);
};
var c = function() {
  var a = 3;
  println("inner a " + a);
  println("inner b " + b);
};
c();
cc(12);
println("outer a " +  a);
println("outer b " + b);
```
