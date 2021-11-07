# Additional rule set for detekt

Repository with additional rules for detekt static analysis tool. Visit the main
detekt [repository](https://github.com/detekt/detekt) for details.

## Setup in project

1. Compile jar artifact `gradlew assemble`
1. Move the artifact to a convenient place to use (for example, the project folder)
1. Add the rule set properties to detekt configuration file (yml)
1. Specify the artifact as detekt plugin

#### Detekt configuration file properties

```
AleksanderShRuleSet:
  SpecifyFunctionExplicitReturnType:
    active: true
  SpecifyPropertyExplicitReturnType:
    active: true
  AvoidFunctionTupleReturnType:
    active: true
  AvoidPropertyTupleReturnType:
    active: true
```

#### Specifying as detekt plugin

###### CLI

```
detekt-cli" --plugins "aleksandersh-detekt-ruleset-0.1.0.jar"
```

###### Gradle

TBD

###### CLI type resolution

```
detekt-cli" --jvm-target 11 --classpath "lib/kotlin-stdlib-1.5.21.jar"
```

## Rules

### SpecifyFunctionExplicitReturnType SpecifyPropertyExplicitReturnType

In most cases, functions or properties without an explicit type are harder to understand, and in some cases due to an
implicit contract, it can lead to errors. Rule checks whether the type of function/property return value is explicitly
specified.

Noncompliant Code

```
fun getProperties() = delegate.getProperties()
```

Compliant Code

```
fun getProperties(): Map<String, String> = delegate.getProperties()
```

parameters

* `checkPublic` (default: true)
* `checkInternal` (default: true)
* `checkProtected` (default: false)
* `checkPrivate` (default: false)

unit-tests

* [Function](https://github.com/aleksandersh/detekt-ruleset/blob/master/src/test/kotlin/io/github/aleksandersh/detekt/ruleset/rule/SpecifyFunctionExplicitReturnTypeTest.kt)
* [Property](https://github.com/aleksandersh/detekt-ruleset/blob/master/src/test/kotlin/io/github/aleksandersh/detekt/ruleset/rule/SpecifyPropertyExplicitReturnTypeTest.kt)

### AvoidFunctionTupleReturnType AvoidPropertyTupleReturnType

**[Requires Type Resolution](https://detekt.github.io/detekt/type-resolution.html)**

It is more difficult to understand the return values if they are composed in a tuple, prefer to use custom classes
instead.

Noncompliant Code

```
fun getUser(): Pair<String, String> = createUser()
```

Compliant Code

```
class User(val name: String, val email: String)
fun getUser(): User = createUser()
```

parameters

* `checkPublic` (default: true)
* `checkInternal` (default: true)
* `checkProtected` (default: false)
* `checkPrivate` (default: false)

unit-tests

* [Function](https://github.com/aleksandersh/detekt-ruleset/blob/master/src/test/kotlin/io/github/aleksandersh/detekt/ruleset/rule/AvoidFunctionTupleReturnTypeTest.kt)
* [Property](https://github.com/aleksandersh/detekt-ruleset/blob/master/src/test/kotlin/io/github/aleksandersh/detekt/ruleset/rule/AvoidPropertyTupleReturnTypeTest.kt)

### Visibility parameters

* `checkPublic` - apply rule for public declarations
* `checkInternal` - apply rule for internal declarations
* `checkProtected` - apply rule for protected declarations
* `checkPrivate` - apply rule for private declarations

The visibility of a declaration is based on its modifier and the modifier of the class/object in which it is found. A
common case is disabling the rule for private or protected declaration.

unit-tests

* [Visibility](https://github.com/aleksandersh/detekt-ruleset/blob/master/src/test/kotlin/io/github/aleksandersh/detekt/ruleset/rule/DeclarationVisibilityCheckTest.kt)
