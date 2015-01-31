package com.mangofactory.documentation.schema
import com.mangofactory.documentation.schema.mixins.TypesForTestingSupport

import static com.mangofactory.documentation.schema.Collections.*
import static com.mangofactory.documentation.spi.DocumentationType.*
import static com.mangofactory.documentation.spi.schema.contexts.ModelContext.*

@Mixin([TypesForTestingSupport, AlternateTypesSupport])
class ContainerTypesSpec extends SchemaSpecification {
  def "Response class for container types are inferred correctly"() {
    given:
      def context = returnValue(containerType, SWAGGER_12, alternateTypeProvider())
    expect:
      typeNameExtractor.typeName(context) == name

    where:
      containerType                  | name
      genericListOfSimpleType()      | "List[SimpleType]"
      genericListOfInteger()         | "List[int]"
      erasedList()                   | "List"
      genericSetOfSimpleType()       | "Set[SimpleType]"
      genericSetOfInteger()          | "Set[int]"
      erasedSet()                    | "Set"
      genericClassWithGenericField() | "GenericType«ResponseEntityAlternative«SimpleType»»"

  }

  def "Model properties of type List, are inferred correctly"() {
    given:
      def sut = typeWithLists()
      Model asInput = modelProvider.modelFor(inputParam(sut, SWAGGER_12, alternateTypeProvider())).get()
      Model asReturn = modelProvider.modelFor(returnValue(sut, SWAGGER_12, alternateTypeProvider())).get()

    expect:
      asInput.getName() == "ListsContainer"
      asInput.getProperties().containsKey(property)
      def modelProperty = asInput.getProperties().get(property)
      modelProperty.type.erasedType == name
      modelProperty.getItems()
      ModelRef item = modelProperty.getItems()
      item.type == itemType

      asReturn.getName() == "ListsContainer"
      asReturn.getProperties().containsKey(property)
      def retModelProperty = asReturn.getProperties().get(property)
      retModelProperty.type.erasedType == name
      retModelProperty.getItems()
      def retItem = retModelProperty.getItems()
      retItem.type == itemType

    where:
      property          | name      | itemType      | itemQualifiedType
      "complexTypes"    | List      | 'ComplexType' | "com.mangofactory.documentation.schema.ComplexType"
      "enums"           | List      | "string"      | "com.mangofactory.documentation.schema.ExampleEnum"
      "aliasOfIntegers" | List      | "int"         | "java.lang.Integer"
      "strings"         | ArrayList | "string"      | "java.lang.String"
      "objects"         | List      | "object"      | "java.lang.Object"
  }

  def "Model properties are inferred correctly"() {
    given:
      def sut = typeWithSets()
      Model asInput = modelProvider.modelFor(inputParam(sut, SWAGGER_12, alternateTypeProvider())).get()
      Model asReturn = modelProvider.modelFor(returnValue(sut, SWAGGER_12, alternateTypeProvider())).get()

    expect:
      asInput.getName() == "SetsContainer"
      asInput.getProperties().containsKey(property)
      def modelProperty = asInput.getProperties().get(property)
      containerType(modelProperty.getType()) == type
      modelProperty.getItems()
      ModelRef item = modelProperty.getItems()
      item.type == itemType

      asReturn.getName() == "SetsContainer"
      asReturn.getProperties().containsKey(property)
      def retModelProperty = asReturn.getProperties().get(property)
      containerType(retModelProperty.type) == type
      retModelProperty.getItems()
      def retItem = retModelProperty.getItems()
      retItem.type == itemType

    where:
      property          | type  | itemType      | itemQualifiedType
      "complexTypes"    | "Set" | 'ComplexType' | "com.mangofactory.documentation.schema.ComplexType"
      "enums"           | "Set" | "string"      | "com.mangofactory.documentation.schema.ExampleEnum"
      "aliasOfIntegers" | "Set" | "int"         | "java.lang.Integer"
      "strings"         | "Set" | "string"      | "java.lang.String"
      "objects"         | "Set" | "object"      | "java.lang.Object"
  }

  def "Model properties of type Arrays are inferred correctly"() {
    given:
      def sut = typeWithArrays()
      Model asInput = modelProvider.modelFor(inputParam(sut, SWAGGER_12, alternateTypeProvider())).get()
      Model asReturn = modelProvider.modelFor(returnValue(sut, SWAGGER_12, alternateTypeProvider())).get()

    expect:
      asInput.getName() == "ArraysContainer"
      asInput.getProperties().containsKey(property)
      def modelProperty = asInput.getProperties().get(property)
      modelProperty.type.erasedType == type
      modelProperty.getItems()
      ModelRef item = modelProperty.getItems()
      item.type == itemType

      asReturn.getName() == "ArraysContainer"
      asReturn.getProperties().containsKey(property)
      def retModelProperty = asReturn.getProperties().get(property)
      retModelProperty.type.erasedType == type
      retModelProperty.getItems()
      def retItem = retModelProperty.getItems()
      retItem.type == itemType

    where:
      property          | type          | itemType      | itemQualifiedType
      "complexTypes"    | ComplexType[] | 'ComplexType' | "com.mangofactory.documentation.schema.ComplexType"
      "enums"           | ExampleEnum[] | "string"      | "com.mangofactory.documentation.schema.ExampleEnum"
      "aliasOfIntegers" | Integer[]     | "int"         | "java.lang.Integer"
      "strings"         | String[]      | "string"      | "java.lang.String"
      "objects"         | Object[]      | "object"      | "java.lang.Object"
      "bytes"           | byte[]        | "byte"        | "byte"
  }

  def "Model properties of type Map are inferred correctly"() {
    given:
      def sut = mapsContainer()
      Model asInput = modelProvider.modelFor(inputParam(sut, SWAGGER_12, alternateTypeProvider())).get()
      Model asReturn = modelProvider.modelFor(returnValue(sut, SWAGGER_12, alternateTypeProvider())).get()

    expect:
      asInput.getName() == "MapsContainer"
      asInput.getProperties().containsKey(property)
      def modelProperty = asInput.getProperties().get(property)
      modelProperty.type.erasedType == type
      modelProperty.getItems()
      ModelRef item = modelProperty.getItems()
      item.type == itemRef

      asReturn.getName() == "MapsContainer"
      asReturn.getProperties().containsKey(property)
      def retModelProperty = asReturn.getProperties().get(property)
      retModelProperty.type.erasedType == type
      retModelProperty.getItems()
      def retItem = retModelProperty.getItems()
      retItem.type == itemRef

    where:
      property              | type   | itemRef                      | itemQualifiedType
      "enumToSimpleType"    | List | "Entry«string,SimpleType»"   | "com.mangofactory.documentation.schema.Entry"
      "stringToSimpleType"  | List | "Entry«string,SimpleType»"   | "com.mangofactory.documentation.schema.Entry"
      "complexToSimpleType" | List | "Entry«Category,SimpleType»" | "com.mangofactory.documentation.schema.Entry"
  }

  def "Model properties of type Map are inferred correctly on generic host"() {
    given:
      def sut = genericTypeOfMapsContainer()

      def modelContext = inputParam(sut, SWAGGER_12, alternateTypeProvider())
      Model asInput = modelProvider.dependencies(modelContext).get("MapsContainer")

      def returnContext = returnValue(sut, SWAGGER_12, alternateTypeProvider())
      Model asReturn = modelProvider.dependencies(returnContext).get("MapsContainer")

    expect:
      asInput.getName() == "MapsContainer"
      asInput.getProperties().containsKey(property)
      def modelProperty = asInput.getProperties().get(property)
      modelProperty.type.erasedType == type
      modelProperty.getItems()
      ModelRef item = modelProperty.getItems()
      item.type == itemRef

      asReturn.getName() == "MapsContainer"
      asReturn.getProperties().containsKey(property)
      def retModelProperty = asReturn.getProperties().get(property)
      retModelProperty.type.erasedType == type
      retModelProperty.getItems()
      def retItem = retModelProperty.getItems()
      retItem.type == itemRef

    where:
      property              | type   | itemRef                      | itemQualifiedType
      "enumToSimpleType"    | List | "Entry«string,SimpleType»"   | "com.mangofactory.documentation.schema.Entry"
      "stringToSimpleType"  | List | "Entry«string,SimpleType»"   | "com.mangofactory.documentation.schema.Entry"
      "complexToSimpleType" | List | "Entry«Category,SimpleType»" | "com.mangofactory.documentation.schema.Entry"
  }
}