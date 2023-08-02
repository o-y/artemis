package wtf.zv.artemis.compiler.api.internal

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.TypeSpec.Companion.classBuilder
import wtf.zv.artemis.compiler.api.ArtemisFileSpec
import wtf.zv.artemis.compiler.api.toArtemisFileSpec
import wtf.zv.artemis.compiler.parser.KOTLIN_FILE_EXTENSION

/** Provides utilities to generate Artemis build graph [FileSpec]-scoped classes. */
internal class ArtemisClassGenerator {
    /** Generates the root "ArtemisBuildGraphRoot" class from which all sub-build graphs extend from. */
    internal fun generateArtemisBuildGraphRootFile(): FileSpec {
        val functionName = createStringPropertySpec(
            functionName = generatedFunctionNameDef,
            additionalModifiers = setOf(KModifier.OPEN)
        )

        val packageName = createStringPropertySpec(
            functionName = generatedPackageNameDef,
            additionalModifiers = setOf(KModifier.OPEN)
        )

        val functionHash = createStringPropertySpec(
            functionName = generatedFunctionHashDef,
            additionalModifiers = setOf(KModifier.OPEN)
        )

        val constructor = FunSpec.constructorBuilder()
            .addParameter(createStringParameter(generatedFunctionNameDef))
            .addParameter(createStringParameter(generatedPackageNameDef))
            .addParameter(createStringParameter(generatedFunctionHashDef))
            .build()

        val artemisBuildGraphRootClass = classBuilder(rootClassNameDef)
            .addModifiers(KModifier.OPEN)
            .primaryConstructor(constructor)
            .addProperty(functionName)
            .addProperty(packageName)
            .addProperty(functionHash)
            .build()

        val artemisBuildGraphRootFile = FileSpec.builder(
            packageName = rootPackageNameDef,
            fileName = "${rootClassNameDef}.${KOTLIN_FILE_EXTENSION}"
        )

        return artemisBuildGraphRootFile
            .addType(artemisBuildGraphRootClass)
            .addKotlinDefaultImports(includeJvm = false)
            .build()
    }

    /** Generates a sub-classed build graph representation for the given [GroupedArtemisFunctionKey]. */
    internal fun generateSubClassedArtemisBuildGraphFile(artemisFunctionGroup: GroupedArtemisFunctionKey): FileSpec {
        val generatedFileName = artemisFunctionGroup
            .rawFileName
            .toPascalCase(generatedClassNamePrefix)

        val generatedClassName = generatedFileName.substringBeforeLast(".$KOTLIN_FILE_EXTENSION")

        val generatedFunctionKeys: List<GeneratedArtemisFunctionKey> = artemisFunctionGroup
            .artemisFunctionKeys
            .map(::GeneratedArtemisFunctionKey)

        val artemisBuildGraphRootClass = ClassName(rootPackageNameDef, rootClassNameDef)

        val generatedFunctionKeysTypeSpecs = generatedFunctionKeys.map {
            TypeSpec.objectBuilder(it.generatedFunctionKey)
                .addModifiers(KModifier.DATA)
                .superclass(artemisBuildGraphRootClass)
                .addSuperclassConstructorParameter(
                    createConstructorDefinition(generatedFunctionNameDef to it.javaScriptFunctionKey))
                .addSuperclassConstructorParameter(
                    createConstructorDefinition(generatedPackageNameDef to it.javaScriptFunctionPackage))
                .addSuperclassConstructorParameter(
                    createConstructorDefinition(generatedFunctionHashDef to it.generatedHash))
                .build()
        }

        val generatedBuildGraphClass = classBuilder(generatedClassName)
            .addModifiers(KModifier.PUBLIC)
            .addTypes(generatedFunctionKeysTypeSpecs)
            .build()

        val generatedBuildGraphFile = FileSpec.builder(
            packageName = artemisFunctionGroup.rawPackageName,
            fileName = generatedFileName
        )

        return generatedBuildGraphFile
            .addType(generatedBuildGraphClass)
            .addKotlinDefaultImports(includeJvm = false)
            .build()
    }

    internal companion object {
        /////////////////////////////////////////
        ////// GENERATED BUILD GRAPH DECLARATIONS
        /////////////////////////////////////////
        const val generatedFunctionNameDef = "functionName"
        const val generatedPackageNameDef = "packageName"
        const val generatedFunctionHashDef = "functionHash"
        const val generatedClassNamePrefix = "ArtemisBuildGraph"

        ////////////////////////////////////
        ////// ARTEMIS INTERNAL DECLARATIONS
        ////////////////////////////////////
        const val rootClassNameDef = "ArtemisBuildGraphRoot"
        const val rootPackageNameDef = "wtf.zv.artemis.root"
    }
}

/** Shorthand method: creates a [PropertySpec] where the property [TypeName] matches [T]. */
private inline fun <reified T> createPropertySpec(
    functionName: String,
    kDoc: String = "",
    functionVisibility: KModifier = KModifier.PUBLIC,
    additionalModifiers: Set<KModifier> = setOf()
): PropertySpec {
    return PropertySpec.builder(functionName, T::class, functionVisibility)
        .addModifiers(additionalModifiers)
        .initializer(functionName)
        .addKdoc(kDoc)
        .build()
}

/** Shorthand method: creates a [PropertySpec] where the property [TypeName] matches [String]. */
private fun createStringPropertySpec(
    functionName: String,
    kDoc: String = "",
    functionVisibility: KModifier = KModifier.PUBLIC,
    additionalModifiers: Set<KModifier> = setOf()
) = createPropertySpec<String>(
    functionName = functionName,
    kDoc = kDoc,
    functionVisibility = functionVisibility,
    additionalModifiers = additionalModifiers
)

/** Shorthand method: creates a [ParameterSpec] where the property [TypeName] matches [T]. */
private inline fun <reified T> createParameter(
    functionName: String,
    kDoc: String = "",
    additionalModifiers: Set<KModifier> = setOf()
) = ParameterSpec.builder(
    name = functionName,
    type = T::class,
    modifiers = additionalModifiers,
).addKdoc(kDoc).build()

/** Shorthand method: creates a [ParameterSpec] where the property [TypeName] matches [String]. */
private fun createStringParameter(
    functionName: String,
    kDoc: String = "",
    additionalModifiers: Set<KModifier> = setOf()
) = createParameter<String>(
    functionName = functionName,
    additionalModifiers = additionalModifiers,
    kDoc = kDoc
)

/** Shorthand method: given a [propertyDefinition] pair returns the format expected by inline-object classes. */
private fun createConstructorDefinition(propertyDefinition: Pair<Any, Any>) = createConstructorDefinition(
    property = propertyDefinition.second,
    namedPropertyDef = propertyDefinition.first
)

/**
 * Shorthand method: given a [property]/[namedPropertyDef] returns the format expected by inline-object classes.
 *
 * These are formatted in either:
 * - `func([namedPropertyDef] = "[property]", ...)`
 * - `func("[property]", ...)`
 *
 * Note: The [property] value will always be formatted as a [String].
 */
private fun createConstructorDefinition(
    property: Any,
    namedPropertyDef: Any = ""
) = if (namedPropertyDef.toString().isNotEmpty())
        """$namedPropertyDef = "$property""""
    else
        """"$property""""
