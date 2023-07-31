package wtf.zv.artemis.compiler.api

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

/** Internal class which provides utilities to generate ArtemisBuildGraph enum representations. */
internal class ArtemisClassGenerator {
    private val primaryConstructor = FunSpec.constructorBuilder()
        .addParameter("functionName", String::class)
        .addParameter("packageName", String::class)
        .build()

    fun createArtemisSkeletonClass(jvmFileName: String): Builder {
        val jvmClassName = jvmFileName.substringBeforeLast(".kt")
        val typeSpecBuilder = TypeSpec.enumBuilder(jvmClassName)
            .primaryConstructor(primaryConstructor)
            .addProperty(
                PropertySpec.builder("functionName", String::class)
                .initializer("functionName")
                .build())
            .addProperty(
                PropertySpec.builder("packageName", String::class)
                .initializer("packageName")
                .build())

        return Builder(
            typeSpecBuilder = typeSpecBuilder,
            jvmFileName = jvmFileName
        )
    }

    internal inner class Builder constructor(
        private val typeSpecBuilder: TypeSpec.Builder,
        private val jvmFileName: String
    ) {
        private var packageName = ""

        internal fun addConstants(jvmFunctionKeys: List<ArtemisFunctionKeyJvmRepresentation>): Builder {
            jvmFunctionKeys.forEach(::addConstant)
            return this
        }

        internal fun toFile(): FileSpec {
            assert(packageName.isEmpty()) {
                "#packageName empty - ensure either #addConstant or #addConstants is called prior to #toFile!"
            }

            val fileSpec = FileSpec.builder(packageName, jvmFileName)
            fileSpec.addType(typeSpecBuilder.build())

            return fileSpec.build()
        }

        private fun addConstant(jvmFunctionKey: ArtemisFunctionKeyJvmRepresentation): Builder {
            packageName = jvmFunctionKey.artemisFunctionKey.rawPackageName

            typeSpecBuilder.addEnumConstant(jvmFunctionKey.jvmFunctionKey,
                TypeSpec.anonymousClassBuilder()
                    .addSuperclassConstructorParameter("%S",
                        jvmFunctionKey.artemisFunctionKey.rawFunctionName)
                    .addSuperclassConstructorParameter("%S",
                        jvmFunctionKey.artemisFunctionKey.rawPackageName)
                    .build())

            return this
        }
    }
}