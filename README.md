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
```

#### Specifying as detekt plugin

###### CLI

```
detekt-cli" -p "aleksandersh-detekt-ruleset-1.0.jar"
```

###### Gradle

TBD

## Rules

#### SpecifyFunctionExplicitReturnType SpecifyPropertyExplicitReturnType

Checks whether the type of function/property return value is explicitly specified.

###### parameters

* checkPublic (default: true) - apply rule for public declarations
* checkInternal (default: true) - apply rule for internal declarations
* checkProtected (default: false) - apply rule for protected declarations
* checkPrivate (default: false) - apply rule for private declarations

The visibility of a declaration is based on its modifier and the modifier of the class/object in which it is found. A
common case is disabling the rule for private or protected declaration.