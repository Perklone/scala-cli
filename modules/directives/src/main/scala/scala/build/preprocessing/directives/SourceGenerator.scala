package scala.build.preprocessing.directives

import scala.cli.commands.SpecificationLevel
import scala.build.directives.*
import scala.build.EitherCps.{either, value}
import scala.build.Ops.*
import scala.build.errors.{BuildException, CompositeBuildException}
import scala.build.options.{BuildOptions, SourceGeneratorOptions, GeneratorConfig}
import scala.build.options.GeneratorConfig
import scala.build.{Positioned, options}

@DirectiveUsage("//> using sourceGenerator", "`//> using sourceGenerator`")
@DirectiveDescription("Generate code using Source Generator")
@DirectiveLevel(SpecificationLevel.EXPERIMENTAL)
final case class SourceGenerator(
  sourceGenerator: Option[Positioned[String]] = None
) extends HasBuildOptions {
  def buildOptions: Either[BuildException, BuildOptions] = either {
    val maybeGenerateSource = sourceGenerator
      .map(GeneratorConfig.parse)
      .sequence

    // val buildOpt = BuildOptions(
    //   generateSource = Some(generateSource0)
    // )

    val generateSource = maybeGenerateSource match {
      case Left(buildException) => throw buildException
      case Right(config) => config
    }

    BuildOptions(sourceGeneratorOptions =
      SourceGeneratorOptions(generatorConfig = generateSource)
    )
  }
}

object SourceGenerator {
  val handler: DirectiveHandler[SourceGenerator] = DirectiveHandler.derive
}
