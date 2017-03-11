#Dolphin Voice Language

使用Java语言参照JVM实现的一门解释型语言.

## Features

1. 函数多参数支持
2. 无缝植入Java类特性

## Examples

1. 简单语法

**Input：**

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

**Output：**

```dv
inner a 3
inner b 2
inner c 12 , d=2
outer a 3
outer b 2
```

2. define-as语法

**Input：**

```dv
define java.util.ArrayList as list
define java.util.Date as date

list->add(1);
list->add("1332");

println(date->getTime());

println(list);
```

**Output：**

```dv
1489235605433
[1, 1332]
```

