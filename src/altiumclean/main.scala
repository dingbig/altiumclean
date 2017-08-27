package altiumclean

import java.io.File

object main extends App {
  def process_dir(dir: File): Unit = {
    if(isAltiumProject(dir)) {
      cleanAltiumProject(dir)
    }

    dir.listFiles().filter(_.isDirectory).foreach(process_dir(_))
  }

  def isAltiumProject(dir: File): Boolean = dir.listFiles().exists(_.getAbsolutePath.toLowerCase().endsWith(".prjpcb"))
  def cleanAltiumProject(dir: File) ={
    val subFiles = dir.listFiles()
    subFiles.filter(_.getName.startsWith("Project Logs for ")).foreach(rmrf)
    subFiles.filter(_.getName.startsWith("Project Outputs ")).foreach(rmrf)
    subFiles.filter(_.getName.startsWith("Design Rule Check -")).foreach(rmrf)
    subFiles.filter(_.getName.endsWith(".PrjPCBStructure")).foreach(rmrf)

    subFiles.filter(_.getName == ("History")).foreach(rmrf)
    subFiles.filter(_.getName == ("__Previews")).foreach(rmrf)



  }
  def rmrf(f: File): Unit = {
    if(f.isDirectory) {
      f.listFiles().foreach(rmrf(_))
    }
    f.delete()
    println(f + " deleted")

  }
  val dirs = if(args.length == 0) List(new File(System.getProperty("user.dir"))) else args.map(new File(_)).toList
  dirs.foreach(process_dir(_))

}
