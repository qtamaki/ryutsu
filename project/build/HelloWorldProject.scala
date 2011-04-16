import sbt._

class HelloWorldProject(info: ProjectInfo) extends DefaultProject(info)
{
  override def libraryDependencies = Set(
    "org.apache.httpcomponents" % "httpclient" % "4.0-beta1",
    "commons-lang" % "commons-lang" % "2.3"
  ) ++ super.libraryDependencies

}

